package com.example.learning_android.ui.components.placePage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learning_android.data.remote.helpers.calculateAngle
import com.example.learning_android.data.remote.helpers.getMonthName
import com.example.learning_android.domain.model.place.PlaceDataManager

@Composable
fun SeasonalLightWheel(
  manager: PlaceDataManager?,
) {
  val selectedMonth by manager?.selectedMonth?.collectAsStateWithLifecycle()
    ?: remember {mutableStateOf(null)}

  val monthlyDataState by manager?.monthlyCoverage?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(emptyMap()) }

  val totalDays = monthlyDataState.values.sumOf { it }

  val textMeasurer = androidx.compose.ui.text.rememberTextMeasurer()
  val labelStyle = TextStyle(
    color = Color.Black.copy(alpha = 0.5f),
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
  )

  /*val fakeMonthlyCoverage = mapOf(
    1 to 31, 2 to 14, 3 to 1, 4 to 0, 5 to 25, 6 to 10,
    7 to 25, 8 to 13, 9 to 31, 10 to 31, 11 to 31, 12 to 31
  )*/

  val winter = Color(0xFF99C0C5)
  val spring = Color(0xFF9FAC81)
  val summer = Color(0xFFEACF7C)
  val autumn = Color(0xFFB9735E)

  val primary = MaterialTheme.colorScheme.primary
  val secondary = MaterialTheme.colorScheme.secondary

  val background = Brush.radialGradient(
    listOf(
      Color(0xFFD7D4C1),
      Color(0xFFD7D4C1).copy(alpha = 0.5F)
    ),
    center = Offset(1200f, 700f),
    radius = 2000f
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(background)
      .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  )
  {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically

    ) {
      Box(modifier = Modifier.weight(1f))

      Text(
        text = "SEASONAL WHEEL",
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.wrapContentWidth()
      )

      Text(
        modifier = Modifier.weight(1f),
        text = "${totalDays}/365",
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End
      )
    }



    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(8.dp),
      contentAlignment = Alignment.Center
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectTapGestures { offset ->
              val angle = calculateAngle(offset, size)
              val month = (angle / 30f).toInt() + 1
              val adjMonth = if (month > 12) 1 else month

              manager?.selectMonth(adjMonth)
            }
          }
      ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val totalRadius = size.minDimension / 2f

        val seasonRingWidth = 8.dp.toPx()
        val pieMaxRadius = totalRadius - seasonRingWidth - 6.dp.toPx()
        val pieInnerRadius = pieMaxRadius * 0.4f
        val pieWidth = pieMaxRadius - pieInnerRadius

        val monthLabels = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")

        for (i in 0 until 12) {
          val monthIndex = i + 1
          val startAngle = (i * 30f) - 88f
          val sweepAngle = 23f

          // 1. Season Ring
          val seasonColor = when (monthIndex) {
            12, 1, 2 -> winter
            3, 4, 5 -> spring
            6, 7, 8 -> summer
            else -> autumn
          }

          drawArc(
            color = seasonColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(
              center.x - totalRadius + (seasonRingWidth / 2),
              center.y - totalRadius + (seasonRingWidth / 2)
            ),
            size = Size(totalRadius * 2 - seasonRingWidth, totalRadius * 2 - seasonRingWidth),
            style = Stroke(width = seasonRingWidth, cap = StrokeCap.Round)
          )

          drawArc(
            color = Color.Gray.copy(alpha = 0.3f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(
              center.x - pieMaxRadius + (pieWidth / 2),
              center.y - pieMaxRadius + (pieWidth / 2)
            ),
            size = Size(pieMaxRadius * 2 - pieWidth, pieMaxRadius * 2 - pieWidth),
            style = Stroke(width = pieWidth)
          )

          val daysCollected = monthlyDataState[monthIndex] ?: 0
          val totalDays = when (monthIndex) {
            2 -> 28; 4, 6, 9, 11 -> 30; else -> 31
          }
          val fillPercent = (daysCollected.toFloat() / totalDays).coerceIn(0f, 1f)

          if (fillPercent > 0f) {
            val fillThickness = pieWidth * fillPercent
            drawArc(

              color = if (daysCollected >= totalDays) primary else secondary,
              startAngle = startAngle,
              sweepAngle = sweepAngle,
              useCenter = false,
              topLeft = Offset(
                center.x - pieMaxRadius + (fillThickness / 2),
                center.y - pieMaxRadius + (fillThickness / 2)
              ),
              size = Size(pieMaxRadius * 2 - fillThickness, pieMaxRadius * 2 - fillThickness),
              style = Stroke(width = fillThickness)
            )
          }

          val labelAngle = Math.toRadians((i * 30f + 15f - 90f).toDouble())
          val labelRadius = pieInnerRadius + (pieWidth / 2)

          val labelX = center.x + labelRadius * kotlin.math.cos(labelAngle).toFloat()
          val labelY = center.y + labelRadius * kotlin.math.sin(labelAngle).toFloat()

          val textLayoutResult = textMeasurer.measure(
            text = monthLabels[i],
            style = labelStyle
          )

          drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
              labelX - (textLayoutResult.size.width / 2),
              labelY - (textLayoutResult.size.height / 2)
            )
          )

          if (selectedMonth == monthIndex) {
            drawArc(
              color = Color.Black.copy(alpha = 0.1f),
              startAngle = startAngle,
              sweepAngle = sweepAngle,
              useCenter = true
            )
          }
        }
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = selectedMonth?.let { getMonthName(it) } ?: "ANNUAL",
          style = TextStyle(fontWeight = FontWeight.Black, fontSize = 20.sp)
        )
        Text(
          text = selectedMonth?.let { "${monthlyDataState[it] ?: 0} DAYS" } ?: "OVERVIEW",
          style = TextStyle(color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        )
      }
    }
  }
}