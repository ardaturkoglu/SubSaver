package com.ardat.comsubsaverapp.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Power
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(val label: String, val icon: ImageVector) {
    STREAMING("Streaming", Icons.Default.PlayCircle),
    MUSIC("Music", Icons.Default.MusicNote),
    CLOUD("Cloud", Icons.Default.Cloud),
    GAMING("Gaming", Icons.Default.Gamepad),
    FITNESS("Fitness", Icons.Default.FitnessCenter),
    NEWS("News", Icons.Default.Newspaper),
    UTILITIES("Utilities", Icons.Default.Power),
    OTHER("Other", Icons.Default.MoreHoriz);

    companion object {
        fun fromLabel(label: String): Category =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: OTHER
    }
}
