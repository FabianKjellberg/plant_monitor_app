package com.example.learning_android.ui.components.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learning_android.R
import com.example.learning_android.domain.model.Device
import com.example.learning_android.ui.components.BatteryPill
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DeviceCard(device: Device, onClick: () -> Unit) {
  val batteryPercent = device.batteryPercentage?.roundToInt()
  val colorScheme = MaterialTheme.colorScheme

  val formattedDate = remember(device.batteryReadAt) {
    val instant = device.batteryReadAt ?: return@remember "Never"

    val zoneId = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zoneId).toLocalDate()
    val deviceDate = instant.atZone(zoneId).toLocalDate()

    when {
      deviceDate.isEqual(now) -> {
        val formatter = DateTimeFormatter.ofPattern("'Today,' HH:mm")
          .withZone(zoneId)
        formatter.format(instant)
      }
      else -> {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
          .withZone(zoneId)
        formatter.format(instant)
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            colorScheme.surfaceVariant,
            colorScheme.surfaceVariant.copy(alpha = 0.5f)
          )
        )
      )
      .clickable { onClick() }
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_pot_device),
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .size(160.dp)
        .padding(end = 8.dp)
        .graphicsLayer(
          alpha = 0.15f,
          scaleX = 1.2f,
          scaleY = 1.2f,
          rotationZ = -15f
        )
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .padding(20.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = device.name,
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface
          )
        )

        Text(
          text = "Last reading: $formattedDate",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = colorScheme.onSurfaceVariant
          )
        )
      }
    }

    Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
      BatteryPill(batteryPercent)
    }
  }
}