package com.ardat.comsubsaverapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SpendSummaryCard(
    monthlyTotal: Double,
    yearlyTotal: Double,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Monthly Spend",
                    style = MaterialTheme.typography.labelLarge
                )
                AnimatedCurrencyValue(value = monthlyTotal)
            }

            Column {
                Text(
                    text = "Yearly Estimate",
                    style = MaterialTheme.typography.labelLarge
                )
                AnimatedCurrencyValue(value = yearlyTotal)
            }
        }
    }
}

@Composable
private fun AnimatedCurrencyValue(value: Double) {
    AnimatedContent(
        targetState = value,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "currency_animation"
    ) { target ->
        Text(
            text = NumberFormat.getCurrencyInstance(Locale.US).format(target),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
