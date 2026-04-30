package com.example.learning_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learning_android.ui.components.AppNavigation
import com.example.learning_android.ui.theme.Learning_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Learning_androidTheme {
                AppNavigation()
            }
        }
    }
}