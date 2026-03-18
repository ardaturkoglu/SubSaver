package com.ardat.comsubsaverapp.feature.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ardat.comsubsaverapp.data.model.Category
import com.ardat.comsubsaverapp.data.model.Subscription
import com.ardat.comsubsaverapp.ui.components.CategoryChip
import com.ardat.comsubsaverapp.ui.components.EmptyStateView
import com.ardat.comsubsaverapp.ui.components.SpendSummaryCard
import com.ardat.comsubsaverapp.ui.components.SubscriptionCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSubscriptionClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var pendingDelete by remember { mutableStateOf<Subscription?>(null) }
    var headerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        headerVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SubSaver") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add subscription"
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            return@Scaffold
        }

        if (uiState.subscriptions.isEmpty()) {
            EmptyStateView(
                onAddClick = onAddClick,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "summary") {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
                    ) {
                        SpendSummaryCard(
                            monthlyTotal = uiState.totalMonthlySpend,
                            yearlyTotal = uiState.totalYearlySpend
                        )
                    }
                }

                item(key = "chips") {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 })
                    ) {
                        CategoryFilters(
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelected = viewModel::onCategorySelected
                        )
                    }
                }

                itemsIndexed(
                    items = uiState.subscriptions,
                    key = { _, item -> item.id }
                ) { index, subscription ->
                    var itemVisible by remember(subscription.id) { mutableStateOf(false) }
                    LaunchedEffect(subscription.id) {
                        delay((index * 50L).coerceAtMost(250L))
                        itemVisible = true
                    }

                    AnimatedVisibility(
                        visible = itemVisible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
                    ) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    pendingDelete = subscription
                                    false
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            modifier = Modifier.animateItem(),
                            backgroundContent = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .size(56.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        ) {
                            SubscriptionCard(
                                subscription = subscription,
                                onClick = { onSubscriptionClick(subscription.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (pendingDelete != null) {
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete subscription?") },
            text = { Text("This action removes the subscription from your dashboard.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSubscription(pendingDelete!!.id)
                        pendingDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CategoryFilters(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryChip(
                category = null,
                selected = selectedCategory == null,
                onClick = onCategorySelected
            )
            Category.entries.forEach { category ->
                CategoryChip(
                    category = category,
                    selected = selectedCategory == category,
                    onClick = onCategorySelected
                )
            }
        }
    }
}
