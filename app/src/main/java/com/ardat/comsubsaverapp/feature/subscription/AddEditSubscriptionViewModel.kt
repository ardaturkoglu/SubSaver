package com.ardat.comsubsaverapp.feature.subscription

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ardat.comsubsaverapp.data.model.BillingCycle
import com.ardat.comsubsaverapp.data.model.Category
import com.ardat.comsubsaverapp.data.model.Subscription
import com.ardat.comsubsaverapp.data.repository.SubscriptionRepository
import com.ardat.comsubsaverapp.widget.SubSaverWidgetUpdater
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEditSubscriptionUiState(
    val id: Long? = null,
    val name: String = "",
    val amount: String = "",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val category: Category = Category.OTHER,
    val nextBillingDate: LocalDate = LocalDate.now(),
    val nameError: String? = null,
    val amountError: String? = null,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false
)

sealed interface AddEditSubscriptionEvent {
    data object Saved : AddEditSubscriptionEvent
}

class AddEditSubscriptionViewModel(
    private val repository: SubscriptionRepository,
    private val subscriptionId: Long?,
    private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSubscriptionUiState())
    val uiState: StateFlow<AddEditSubscriptionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddEditSubscriptionEvent>()
    val events: SharedFlow<AddEditSubscriptionEvent> = _events.asSharedFlow()

    init {
        if (subscriptionId != null) {
            viewModelScope.launch {
                repository.getById(subscriptionId)?.let { subscription ->
                    _uiState.value = _uiState.value.copy(
                        id = subscription.id,
                        name = subscription.name,
                        amount = subscription.amount.toString(),
                        billingCycle = subscription.billingCycle,
                        category = subscription.category,
                        nextBillingDate = subscription.nextBillingDate,
                        isEditMode = true
                    )
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value, nameError = null)
    }

    fun onAmountChange(value: String) {
        val filtered = value.filterIndexed { index, c ->
            c.isDigit() || c == '.' && '.' !in value.take(index)
        }
        _uiState.value = _uiState.value.copy(amount = filtered, amountError = null)
    }

    fun onBillingCycleChange(cycle: BillingCycle) {
        _uiState.value = _uiState.value.copy(billingCycle = cycle)
    }

    fun onCategoryChange(category: Category) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun onDateChange(date: LocalDate) {
        _uiState.value = _uiState.value.copy(nextBillingDate = date)
    }

    fun save() {
        val state = _uiState.value
        val name = state.name.trim()
        val amount = state.amount.toDoubleOrNull()

        val nameError = if (name.isBlank()) "Name is required" else null
        val amountError = when {
            state.amount.isBlank() -> "Amount is required"
            amount == null || amount <= 0.0 -> "Enter a valid amount"
            else -> null
        }

        if (nameError != null || amountError != null) {
            _uiState.value = state.copy(nameError = nameError, amountError = amountError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val payload = Subscription(
                id = state.id ?: 0L,
                name = name,
                amount = amount ?: 0.0,
                billingCycle = state.billingCycle,
                category = state.category,
                nextBillingDate = state.nextBillingDate
            )
            if (state.isEditMode) {
                repository.update(payload)
            } else {
                repository.insert(payload)
            }
            SubSaverWidgetUpdater.refresh(appContext)
            _uiState.value = _uiState.value.copy(isSaving = false)
            _events.emit(AddEditSubscriptionEvent.Saved)
        }
    }
}

class AddEditSubscriptionViewModelFactory(
    private val repository: SubscriptionRepository,
    private val subscriptionId: Long?,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditSubscriptionViewModel(repository, subscriptionId, appContext) as T
    }
}
