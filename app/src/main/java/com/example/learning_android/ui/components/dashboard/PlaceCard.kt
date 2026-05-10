package com.example.learning_android.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.repositories.IconResource

@Composable
fun PlaceCard(
  place: DetailedHomePlace,
  onClick: (placeId: String) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(6.dp))
      .clickable(onClick = { onClick(place.id) })
      .background(
        brush = Brush.verticalGradient(
          listOf(
            MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8F)
          )
        )
      )
      .padding(6.dp)
  )
  {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ){
      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if(place.icon != null) {
          Icon(
            painter = painterResource(IconResource.getIconById(place.icon).iconId),
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.surfaceVariant
          )
        }
        Text(
          text = place.name,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.surfaceVariant
        )
      }
      Text(
        text = "(${place.devices.size})",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.surface
      )
    }
  }
}