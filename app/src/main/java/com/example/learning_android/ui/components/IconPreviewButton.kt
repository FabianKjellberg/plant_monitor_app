package com.example.learning_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learning_android.R
import com.example.learning_android.repositories.IconResource

@Composable
fun IconPreviewButton(selectedIconId: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val selectedIcon = IconResource.getIconById(selectedIconId)
  val hasSelection = selectedIconId != null

  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = modifier
        .size(56.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(
          if (hasSelection) Color.Transparent
          else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
        .clickable { onClick() }
        .drawBehind {
          if (!hasSelection) {
            drawRoundRect(
              color = Color.Gray,
              style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
              ),
              cornerRadius = CornerRadius(4.dp.toPx())
            )
          }
        },
      contentAlignment = Alignment.Center
    ) {
      if (hasSelection) {
        Icon(
          painter = painterResource(selectedIcon.iconId),
          contentDescription = null,
          modifier = Modifier.size(48.dp),
        )
      } else {
        Icon(
          painterResource(R.drawable.ic_dots_three),
          contentDescription = "Select Icon",
        )
      }
    }

    Text(
      text = if (hasSelection) "Change Icon" else "Select Icon",
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.padding(top = 8.dp),
      color = MaterialTheme.colorScheme.primary
    )
  }
}