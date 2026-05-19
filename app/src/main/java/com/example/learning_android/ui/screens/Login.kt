package com.example.learning_android.ui.screens

import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.viewmodels.LoginViewModel

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel){
  LaunchedEffect(Unit) {
    viewModel.navigationEvent.collect { page: AppPage ->
      navController.navigate(page.route) {
        popUpTo(AppPage.LOGIN.route) { inclusive = true }
      }
    }
  }

  val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle()

  val loginButtonEnabled =
    viewModel.password.isNotEmpty() &&
    viewModel.email.trim().isNotEmpty()  &&
    !viewModel.loading

  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp)
    ,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {

    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_device_nav_icon),
        contentDescription = null,
        Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.primary
      )
      Spacer(Modifier.width(16.dp))
      Text(
        text = "Habiplant",
        fontSize = 38.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary
      )
    }
    Spacer(Modifier.height(64.dp))
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      label = {Text("Email")},
      value = viewModel.email,
      onValueChange = { viewModel.email = it},
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      label = {Text("Password")},
      value = viewModel.password,
      onValueChange = {viewModel.password = it},
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier.animateContentSize()
    ){
      if(errorMessage.value != null) {
        Text(
          modifier = Modifier.padding(16.dp),
          text = errorMessage.value ?: "",
          color = MaterialTheme.colorScheme.error
        )
      }
    }
    Spacer(Modifier.height(8.dp))
    Button(
      onClick = { viewModel.onLoginClick() },
      enabled = loginButtonEnabled
    ){
      Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        text = "Login",
        fontSize = 18.sp
      )
    }
    Spacer(Modifier.height(8.dp))
    TextButton(
      onClick = {}
    ) {
      Text(
        text = "create account",
        fontSize = 16.sp,
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.secondary,
      )
    }

  }
}