package com.ardat.comsubsaverapp.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ardat.comsubsaverapp.data.model.Category

@Composable
fun CategoryChip(
    category: Category?,
    selected: Boolean,
    onClick: (Category?) -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = { onClick(category) },
        label = { Text(text = category?.label ?: "All") },
        colors = FilterChipDefaults.filterChipColors()
    )
}
