package com.example.learning_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.ui.components.AppNavigation
import com.example.learning_android.ui.theme.Learning_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ApiClient.init(applicationContext)

        setContent {
            Learning_androidTheme {
                AppNavigation()
            }
        }
    }
}