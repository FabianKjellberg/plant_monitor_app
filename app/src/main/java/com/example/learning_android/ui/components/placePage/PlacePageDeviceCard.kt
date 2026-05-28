package com.example.learning_android.ui.components.placePage

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learning_android.data.remote.helpers.batteryPercentFromMv
import com.example.learning_android.domain.model.DetailedHomeDevice
import com.example.learning_android.ui.components.BatteryPill

@Composable
fun PlacePageDeviceCard(
  device: DetailedHomeDevice,
  onClick: () -> Unit
) {
  val batteryPercent = batteryPercentFromMv(device.batteryMv)
  val colorScheme = MaterialTheme.colorScheme

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      //verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
      ) {

        Text(
          text = device.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold,
          color = colorScheme.onSurface
        )

        Text(
          text = "Plant sensor",
          style = MaterialTheme.typography.bodySmall,
          color = colorScheme.onSurfaceVariant
        )
      }

      BatteryPill(batteryPercent)
    }
  }
}