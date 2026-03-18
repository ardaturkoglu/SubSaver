package com.ardat.comsubsaverapp.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ardat.comsubsaverapp.data.model.Category
import com.ardat.comsubsaverapp.data.model.Subscription
import com.ardat.comsubsaverapp.data.repository.SubscriptionRepository
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
    private val repository: SubscriptionRepository
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
        }
    }
}

class DashboardViewModelFactory(
    private val repository: SubscriptionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }
}
