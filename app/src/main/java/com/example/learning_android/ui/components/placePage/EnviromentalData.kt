package com.example.learning_android.ui.components.placePage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.place.PlaceDataManager

@Composable
fun EnviromentalData (
  manager: PlaceDataManager?,
  loading: Boolean
){
  val hasValues = manager?.hasValues?.value ?: false

  Text(
    text = "ENVIRONMENTAL DATA",
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    if (manager == null || loading) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        CircularProgressIndicator(
          modifier = Modifier.size(64.dp),
          strokeWidth = 7.dp
        )
      }
    }

    else if(!hasValues) {
      Text("No or not enough data collected")
    }

    else {
      LightCard(
        manager = manager
      )
      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        TempCard(
          manager = manager,
          modifier = Modifier.weight(1F))
        HumidCard(
          manager = manager,
          modifier = Modifier.weight(1F)
        )
      }
      SeasonalLightWheel(
        manager = manager
      )
    }
  }
}