package com.example.learning_android.ui.components.placePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learning_android.domain.model.place.PlaceDataManager
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HumidCard(
  modifier: Modifier = Modifier,
  manager: PlaceDataManager?,
) {

  val avg by manager?.averageHumidity?.collectAsStateWithLifecycle() ?:
    remember { mutableStateOf(null) }

  val humidGradient = Brush.radialGradient(
    colors = listOf(
      Color(0xFF64B5F6),
      Color(0xFF1976D2)
    ),
    center = Offset(0.5f, 0.5f),
    radius = 900f
  )

  val comfortLabel = if( avg == null) {
    "--"
  } else if (avg!! <= 30F) {
    "Dry"
  }
  else if(avg!! <= 60F) {
    "Normal"
  }
  else if(avg!! <= 80F){
    "Tropical"
  }
  else {
    "Rainforest"
  }

  Box(
    modifier = modifier
      .aspectRatio(1F)
      .clip(RoundedCornerShape(24.dp))
      .background(humidGradient)
      .padding(12.dp)
  ) {
    Column(
      Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "HUMIDITY",
        color = Color.Black.copy(0.5F),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = comfortLabel,
          color = Color.Black.copy(alpha = 0.7F),
          fontSize = 27.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "RELATIVE %",
          fontSize = 12.sp,
          color = Color.Black.copy(alpha = 0.5f),
          fontWeight = FontWeight.Bold,
        )
        Text(
          text = "avg",
          color = Color.Black.copy(0.5F),
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${avg?.let { "%.1f".format(it) } ?: "--"}%",
          color = Color.Black.copy(alpha = 0.7F),
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold

        )
      }
    }
  }
}