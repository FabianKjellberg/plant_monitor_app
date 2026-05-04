package com.example.learning_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learning_android.R
import com.example.learning_android.domain.model.PotDevice
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DeviceCard(device: PotDevice, onClick: () -> Unit) {
    val batteryPercent = device.batteryPercentage?.roundToInt()

    val formattedDate = remember(device.batteryReadAt) {
        val instant = device.batteryReadAt ?: return@remember "Never"

        val zoneId = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zoneId).toLocalDate()
        val deviceDate = instant.atZone(zoneId).toLocalDate()

        when {
            deviceDate.isEqual(now) -> {
                val formatter = DateTimeFormatter.ofPattern("'Today,' HH:mm")
                    .withZone(zoneId)
                formatter.format(instant)
            }
            else -> {
                val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
                    .withZone(zoneId)
                formatter.format(instant)
            }
        }
    }

    ElevatedCard(
        shape = RoundedCornerShape(36.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.4F).padding(end = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier.size(120.dp).background(brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Green.copy(alpha = 0.4F),
                                    Color.Transparent,
                                )
                            )
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_pot_device),
                            contentDescription = "PotDevice",
                            modifier = Modifier.padding(25.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxHeight().weight(0.6F),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = device.deviceName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Last reading: $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(20.dp)
            ) {
                BatteryPill(batteryPercent)
            }
        }
    }
}