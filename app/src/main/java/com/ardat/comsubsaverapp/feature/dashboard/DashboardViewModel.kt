package com.ardat.comsubsaverapp.feature.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ardat.comsubsaverapp.data.model.Category
import com.ardat.comsubsaverapp.data.model.Subscription
import com.ardat.comsubsaverapp.data.repository.SubscriptionRepository
import com.ardat.comsubsaverapp.widget.SubSaverWidgetUpdater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val subscriptions: List<Subscription> = emptyList(),
    val totalMonthlySpend: Double = 0.0,
    val totalYearlySpend: Double = 0.0,
    val selectedCategory: Category? = null,
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val repository: SubscriptionRepository,
    private val appContext: Context
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<Category?>(null)

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getAllSubscriptions(),
        selectedCategory
    ) { allSubscriptions, selected ->
        val visibleSubscriptions = selected?.let { category ->
            allSubscriptions.filter { it.category == category }
        } ?: allSubscriptions

        DashboardUiState(
            subscriptions = visibleSubscriptions,
            totalMonthlySpend = visibleSubscriptions.sumOf { it.monthlyAmount },
            totalYearlySpend = visibleSubscriptions.sumOf { it.yearlyAmount },
            selectedCategory = selected,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    fun onCategorySelected(category: Category?) {
        selectedCategory.value = category
    }

    fun deleteSubscription(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
            SubSaverWidgetUpdater.refresh(appContext)
        }
    }
}

class DashboardViewModelFactory(
    private val repository: SubscriptionRepository,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository, appContext) as T
    }
}
