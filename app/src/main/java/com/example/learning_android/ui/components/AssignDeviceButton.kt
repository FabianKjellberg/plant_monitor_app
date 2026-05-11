package com.example.learning_android.ui.components

import android.R.attr.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.domain.model.DeviceType
import com.example.learning_android.repositories.DeviceRepository
import com.example.learning_android.repositories.IconResource

@Composable
fun AssignDeviceButton (
  place: DetailedHomePlace,
  deviceId: String,
  deviceType: DeviceType,
  onClick: () -> Unit,
  busy: Boolean
) {
  val disabled = place.devices.any { device -> deviceId == device.id} || busy

  val dotStatusColor = if (disabled) {
   Color.LightGray
  }
  else if (place.devices.isEmpty()){
    Color.Green
  }
  else if (place.devices.any {device -> device.type == deviceType}){
    Color.Red
  }
  else {
    Color.Yellow
  }
  val disabledAlpha = if (disabled) {
    0.38F
  }
  else {
    1F
  }

  Box(
    Modifier
      .padding(start = 12.dp)
      .clip(RoundedCornerShape(4.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = disabledAlpha))
      .padding(8.dp)
      .padding(start = 4.dp)
      .clickable(
        onClick = { onClick() },
        enabled = !disabled
      )
  ){
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      if(place.icon != null) {
        Icon(
          painterResource(IconResource.getIconById(place.icon).iconId),
          contentDescription = "place-icon",
          modifier = Modifier.size(20.dp),
          tint = LocalContentColor.current.copy(alpha = disabledAlpha)
        )
      }
      Spacer(Modifier.width(4.dp))
      Text(
        modifier = Modifier.weight(1F),
        text = place.name,
        style = MaterialTheme.typography.labelLarge,
        color = LocalContentColor.current.copy(alpha = disabledAlpha)
      )
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(color = dotStatusColor)
      )
      Spacer(Modifier.width(4.dp))
    }
  }
}