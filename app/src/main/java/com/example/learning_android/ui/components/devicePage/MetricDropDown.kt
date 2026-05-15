package com.example.learning_android.ui.components.devicePage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.R
import com.example.learning_android.domain.model.ReadingType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricDropDown(
  readingType: ReadingType,
  updateReadingType: (readingType: ReadingType) -> Unit,
  modifier: Modifier = Modifier)
{
  var expanded by remember { mutableStateOf(false) }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
    modifier = modifier.fillMaxWidth(),

    ) {
    OutlinedTextField(
      value = readingType.displayName,
      onValueChange = {},
      readOnly = true,
      label = { Text("Metric") },
      trailingIcon = {
        val rotation by animateFloatAsState(
          targetValue = if (expanded) 180f else 0f,
          label = "ChevronRotation"
        )
        Icon(
          painter = painterResource(R.drawable.ic_chevron_down),
          contentDescription = null,
          modifier = Modifier
            .size(24.dp)
            .rotate(rotation)
        )
      },
      colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
      modifier = Modifier
        .menuAnchor()
        .fillMaxWidth(),
      textStyle = TextStyle(fontSize = 14.sp)
    )

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
      ReadingType.entries.forEach { type ->
        DropdownMenuItem(
          text = { Text(type.displayName) },
          onClick = {
            updateReadingType(type)
            expanded = false
          },
        )
      }
    }
  }
}

