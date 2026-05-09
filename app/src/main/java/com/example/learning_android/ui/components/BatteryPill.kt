package com.example.learning_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BatteryPill(percentage: Int?, modifier: Modifier = Modifier) {
    val filledColor = when {
        percentage == null -> Color.Transparent
        percentage > 60 -> Color(0xFF4CAF50) // Healthy Green
        percentage > 20 -> Color(0xFFFFC107) // Warning Yellow
        else -> Color(0xFFF44336) // Critical Red
    }

    val fillFraction = when {
        percentage == null -> 1F
        else -> percentage.toFloat() / 100F
    }

    val percentText = when {
        percentage == null -> "--%"
        else -> "$percentage%"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = fillFraction)
                    .clip(RoundedCornerShape(2.dp))
                    .background(filledColor)
                    .align(Alignment.CenterStart)
            ) {

            }
            Text(
                text = percentText,
                style = MaterialTheme.typography.labelSmall.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                ),
                color = MaterialTheme.colorScheme.background,
            )
        }
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 8.dp)
                .clip(RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp))
                .background(MaterialTheme.colorScheme.onSurface)
        )
    }
}