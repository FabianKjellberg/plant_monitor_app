package com.example.learning_android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.rotationMatrix
import com.example.learning_android.domain.model.ChartEntity
import com.example.learning_android.domain.model.ReadingType
import com.patrykandpatrick.vico.compose.axis.axisGuidelineComponent
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SensorLineChart(title: String, dataPoints: List<ChartEntity>, from: Instant, to: Instant, readingType: ReadingType) {
    if (dataPoints.isEmpty()) {
        Text("No data")
        return
    }

    val entries = dataPoints.map { dataPoint -> entryOf(dataPoint.time.epochSecond.toFloat(), dataPoint.value)}
    val model = entryModelOf(entries)
    val lineColor = when (readingType) {
        ReadingType.LUX -> 0xFFFFC107.toInt()
        ReadingType.HUMIDITY -> 0xFF2196F3.toInt()
        ReadingType.TEMPERATURE -> 0xFFF44336.toInt()
        ReadingType.BATTERY_PERCENTAGE -> 0xFF4CAF50.toInt()
        ReadingType.PRESSURE -> 0xFF9C27B0.toInt()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title)

        Chart(
            model = model,
            chart = lineChart(
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minX = from.epochSecond.toFloat(),
                    maxX = to.epochSecond.toFloat()
                ),
                lines = listOf(
                    lineSpec(
                        lineColor = Color(lineColor),
                        lineThickness = 2.dp,
                        // This is the modern replacement for cubicStrength
                        pointConnector = DefaultPointConnector(
                            cubicStrength = 0.2f // 0.2f is the standard "smooth" look
                        ),
                        lineBackgroundShader = verticalGradient(
                            colors = arrayOf(
                                Color(lineColor).copy(alpha = 0.4f),
                                Color(lineColor).copy(alpha = 0.1f)
                            )
                        )
                    )
                ),
            ),
            chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
            startAxis = rememberStartAxis(
                label = axisLabelComponent(
                    textSize = 8.sp
                ),
                guideline = lineComponent(
                    color = Color.LightGray.copy(alpha = 0.15F)
                )
            ),
            bottomAxis = rememberBottomAxis(
                guideline = null,
                tick = null,
                label = axisLabelComponent(
                    textSize = 8.sp
                ),
                labelRotationDegrees = 45F,
                valueFormatter = { value, _ ->
                    val instant = Instant.ofEpochSecond(value.toLong())
                    DateTimeFormatter.ofPattern("HH:00").withZone(ZoneId.systemDefault()).format(instant)
                },
                itemPlacer = AxisItemPlacer.Horizontal.default(
                    spacing = 80,
                    shiftExtremeTicks = true,
                    addExtremeLabelPadding = true
                )
            )
        )
    }

}