package com.ardat.comsubsaverapp.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.FilledButton
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ardat.comsubsaverapp.MainActivity
import com.ardat.comsubsaverapp.SubSaverApp
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.flow.first

class SubSaverWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as SubSaverApp
        val subscriptions = app.subscriptionRepository.getAllSubscriptions().first()
        val monthlyTotal = subscriptions.sumOf { it.monthlyAmount }
        val count = subscriptions.size

        provideContent {
            Column(modifier = GlanceModifier.padding(12.dp)) {
                Text("SubSaver", style = TextStyle(fontWeight = FontWeight.Medium))
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text("Monthly: ${NumberFormat.getCurrencyInstance(Locale.US).format(monthlyTotal)}")
                Text("Active subscriptions: $count")
                Spacer(modifier = GlanceModifier.height(10.dp))
                FilledButton(
                    text = "Open Dashboard",
                    onClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }
}

class SubSaverWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SubSaverWidget()
}

object SubSaverWidgetUpdater {
    suspend fun refresh(context: Context) {
        // Data is read directly inside provideGlance; just trigger a widget redraw.
        SubSaverWidget().updateAll(context)
    }
}
