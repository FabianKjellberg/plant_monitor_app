package com.example.learning_android.ui.components.placePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun TempCard(
  manager: PlaceDataManager?,
  modifier: Modifier = Modifier
) {
  val tempGradient = Brush.linearGradient(
    colors = listOf(
      Color(0xFFF4C881),
      Color(0xFFF4AB7E),
      Color(0xFFDE9572)
    ),
    start = Offset(Float.POSITIVE_INFINITY, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
  )

  val avg by manager?.averageTemp?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(null) }
  val min by manager?.minTemp?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(null) }
  val max by manager?.maxTemp?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(null) }

  Box(
    modifier = modifier
      .aspectRatio(1F)
      .clip(RoundedCornerShape(24.dp))
      .background(tempGradient)
      .padding(12.dp)
  ) {
    Column(
      Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "TEMPERATURE",
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "avg",
          color = Color.Black.copy(0.5F),
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${avg?.let { "%.1f".format(it) } ?: "--"}°",
          color = Color.Black.copy(alpha = 0.7F),
          fontSize = 32.sp,
          fontWeight = FontWeight.ExtraBold

        )
      }
      Row(
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
          Text(
            text = "MIN",
            color = Color.Black.copy(0.5F),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${min?.let { "%.1f".format(it) } ?: "--"}°",
            color = Color.Black.copy(alpha = 0.5F),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold

          )
        }
        Spacer(Modifier.width(24.dp))
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
          Text(
            text = "MAX",
            color = Color.Black.copy(0.5F),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${max?.let { "%.1f".format(it) } ?: "--"}°",
            color = Color.Black.copy(alpha = 0.5F),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold

          )
        }
      }
    }
  }
}