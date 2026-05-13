package com.example.learning_android.ui.components.placePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun LightCard() {

  val lightGradient = radialGradient(
    colors = listOf(
      Color(0xFFFFF176),
      Color(0xFFFFB300)
    ),
    center = Offset(1200f, 700f),
    radius = 2000f
  )

  Box(modifier = Modifier
    .fillMaxWidth()
    .clip(RoundedCornerShape(24.dp))
    .background(lightGradient)
    .padding(12.dp)
    .height(140.dp)
  ){
    Column(
      Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "LIGHT EXPOSURE",
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
      )
    }
  }
}