package com.example.learning_android.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.SelectionState

@Composable
fun SelectionCard(
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  state: SelectionState,
  enabled: Boolean,
  content: @Composable () -> Unit = {}
) {
  val themeColors = androidx.compose.material3.MaterialTheme.colorScheme

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            themeColors.surfaceVariant,
            themeColors.surfaceVariant.copy(alpha = 0.6F)
          )
        )
      )
      .clickable(
        enabled = enabled && state != SelectionState.SELECTED,
        onClick = onClick
      )
      .padding(20.dp)
      .animateContentSize()
      .alpha(if (enabled) 1f else 0.45f)
  ) {
    Column(
      verticalArrangement = Arrangement.Bottom,
    ) {
      Text(
        text = title,
        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          color = themeColors.onSurface
        )
      )

      if(state == SelectionState.UNSELECTED){
        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = subtitle,
          style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
            color = themeColors.onSurfaceVariant
          )
        )
      }
      else if(state == SelectionState.SELECTED) {
        Spacer(modifier = Modifier.height(4.dp))
        content()
      }
    }
  }
}