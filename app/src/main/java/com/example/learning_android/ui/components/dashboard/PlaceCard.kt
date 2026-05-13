package com.example.learning_android.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
      .clip(RoundedCornerShape(4.dp))
      .clickable(onClick = { onClick(place.id) })
      .background(MaterialTheme.colorScheme.background)
      .border(
        width = 1.5.dp,
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.secondary
      )
      .padding(10.dp)
      .padding(horizontal = 4.dp),
  )
  {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ){
      Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if(place.icon != null) {
          Icon(
            painter = painterResource(IconResource.getIconById(place.icon).iconId),
            contentDescription = "icon",
            Modifier.size(18.dp)
          )
        }
        Text(
          text = place.name,
          style = MaterialTheme.typography.titleMedium,
        )
      }
      Text(
        text = "(${place.devices.size})",
        style = MaterialTheme.typography.titleSmall,
      )
    }
  }
}