package com.example.learning_android.ui.components.placePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learning_android.domain.model.place.PlaceDataManager

@Composable
fun LightCard(
  manager: PlaceDataManager?,
  modifier: Modifier = Modifier
) {

  val dli by manager?.averageDli?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(0F) }
  val peakPpfd by manager?.averagePeakPpfd?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(0f) }

  val lightGradient = radialGradient(
    colors = listOf(
      Color(0xFFFFE69C),
      Color(0xFFF2D06B),
      Color(0xFFEBB34D)
    ),
    center = Offset(1200f, 700f),
    radius = 2000f
  )

  val currentDli = dli

  val (heroText, description) = when {
    currentDli == null -> "--" to "--"
    currentDli >= 20f -> "Full Sun" to "Desert-like conditions"
    currentDli >= 12f -> "Bright Light" to "Perfect for succulents"
    currentDli >= 5f -> "Partial Shade" to "Good for Monsteras"
    else -> "Low Light" to "Needs grow lights to thrive"
  }

  val dliDisplay = currentDli?.let { "%.1f".format(it) } ?: "--"
  val ppfdDisplay = peakPpfd?.toInt()?.toString() ?: "--"

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(160.dp)
      .clip(RoundedCornerShape(24.dp))
      .background(lightGradient)
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "LIGHT EXPOSURE",
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )

      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = heroText,
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.Black.copy(alpha = 0.8f)
        )
        Text(
          text = description,
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          color = Color.Black.copy(alpha = 0.6f)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "AVG DAILY DOSE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.4f)
          )
          Text(
            text = "$dliDisplay mol",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.7f)
          )
        }

        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.Black.copy(0.1f)))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "AVG TOP PEAK",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.4f)
          )
          Text(
            text = "$ppfdDisplay μmol",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.7f)
          )
        }
      }
    }
  }
}