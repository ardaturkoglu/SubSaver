package com.ardat.comsubsaverapp.feature.subscription

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ardat.comsubsaverapp.data.model.BillingCycle
import com.ardat.comsubsaverapp.data.model.Category
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubscriptionScreen(
    viewModel: AddEditSubscriptionViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var showDatePicker by remember { mutableStateOf(false) }

    var showSection1 by remember { mutableStateOf(false) }
    var showSection2 by remember { mutableStateOf(false) }
    var showSection3 by remember { mutableStateOf(false) }
    var showSection4 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(60)
        showSection1 = true
        delay(50)
        showSection2 = true
        delay(50)
        showSection3 = true
        delay(50)
        showSection4 = true
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is AddEditSubscriptionEvent.Saved) {
                onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditMode) "Edit Subscription" else "Add Subscription")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(
                visible = showSection1,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text("Subscription Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(visible = uiState.nameError != null) {
                        Text(
                            text = uiState.nameError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = viewModel::onAmountChange,
                        label = { Text("Amount") },
                        prefix = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(visible = uiState.amountError != null) {
                        Text(
                            text = uiState.amountError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showSection2,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Billing Cycle", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BillingCycle.entries.forEach { cycle ->
                            FilterChip(
                                selected = uiState.billingCycle == cycle,
                                onClick = { viewModel.onBillingCycleChange(cycle) },
                                label = { Text(cycle.label) }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showSection3,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Category.entries.forEach { category ->
                            FilterChip(
                                selected = uiState.category == category,
                                onClick = { viewModel.onCategoryChange(category) },
                                label = { Text(category.label) }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showSection4,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.nextBillingDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Next Billing Date") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Pick billing date"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.save()
                        },
                        enabled = !uiState.isSaving,
                        contentPadding = PaddingValues(vertical = 14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnimatedContent(
                            targetState = uiState.isSaving,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "save_button_animation"
                        ) { isSaving ->
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(18.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(if (uiState.isEditMode) "Save Changes" else "Save Subscription")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = uiState.nextBillingDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onDateChange(date)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
