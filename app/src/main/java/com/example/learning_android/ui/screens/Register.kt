package com.example.learning_android.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.viewmodels.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ActiveField {
  PASSWORD,
  OTHER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
  viewModel: RegisterViewModel,
  navController: NavController
) {

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val passwordBoxBackground = MaterialTheme.colorScheme.surface.copy(alpha = 0.5F)

  val err = MaterialTheme.colorScheme.error
  val success = MaterialTheme.colorScheme.primary

  var activeField by remember { mutableStateOf(ActiveField.OTHER) }
  val isPasswordFocus = activeField == ActiveField.PASSWORD

  val passMatchColor = if (viewModel.passMatch) success else err
  val passLengthColor = if (viewModel.passLength) success else err
  val passUpperColor = if (viewModel.passUpper) success else err
  val passLowerColor = if (viewModel.passLower) success else err
  val passSpecialColor = if (viewModel.passSpecial) success else err
  val passDigitColor = if (viewModel.passDigit) success else err

  val focusManager = LocalFocusManager.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
          IconButton(
            onClick = {
              navController.popBackStack()
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_arrow_left),
              contentDescription = null
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background
        )
      )
    },
    snackbarHost = {
      Box(modifier = Modifier
        .fillMaxSize()
      ){
        SnackbarHost(
          hostState = snackbarHostState,
          modifier = Modifier.align(Alignment.TopCenter).padding(top = 84.dp)
        ) { data ->
          Snackbar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background,
            snackbarData = data
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "Create new account",
        fontSize = 24.sp,
        style = MaterialTheme.typography.titleLarge
      )
      Spacer(Modifier.height(8.dp))
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            if (focusState.isFocused) activeField = ActiveField.OTHER
          },
        label = { Text("Email") },
        value = viewModel.emailValue,
        onValueChange = { value -> viewModel.emailValue = value},
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Email,
          imeAction = ImeAction.Next
        ),
        singleLine =  true,
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }
        )
      )

      if(isPasswordFocus || !viewModel.securePassword) {
        Spacer(Modifier.height(8.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = passwordBoxBackground)
            .padding(12.dp)
            .animateContentSize(),
        ) {
          Column {
            Text(
              text = "• Must contain at least 8 characters",
              style = MaterialTheme.typography.bodyMedium,
              color = passLengthColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "• Must contain at least 1 uppercase letter",
              style = MaterialTheme.typography.bodyMedium,
              color = passUpperColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "• Must contain at least 1 lowercase letter",
              style = MaterialTheme.typography.bodyMedium,
              color = passLowerColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "• Must contain at least 1 digit",
              style = MaterialTheme.typography.bodyMedium,
              color = passDigitColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "• Must contain at least 1 special character",
              style = MaterialTheme.typography.bodyMedium,
              color = passSpecialColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "• Passwords must match",
              style = MaterialTheme.typography.bodyMedium,
              color = passMatchColor
            )
          }
        }
      }
      Spacer(Modifier.height(8.dp))
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            if (focusState.isFocused) activeField = ActiveField.PASSWORD
          },
        label = { Text("Password") },
        value = viewModel.passValue,
        onValueChange = { value ->
          viewModel.passValue = value
          viewModel.onPasswordChange()
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Password,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }
        )
      )
      Spacer(Modifier.height(8.dp))
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            if (focusState.isFocused) activeField = ActiveField.PASSWORD
          },
        label = { Text("Repeat password") },
        value = viewModel.repeatPassValue,
        onValueChange = { value ->
          viewModel.repeatPassValue = value
          viewModel.onPasswordChange()
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Password,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }
        ),
        singleLine = true
      )

      Spacer(Modifier.height(8.dp))
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            if (focusState.isFocused) activeField = ActiveField.OTHER
          },
        label = { Text("Home name") },
        value = viewModel.homeNameValue,
        onValueChange = { value -> viewModel.homeNameValue = value },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done
        )
      )
      Spacer(Modifier.height(16.dp))
      Button(
        onClick = { viewModel.onRegisterClick(
          onSuccess = {
            scope.launch {
              snackbarHostState.showSnackbar(
                message = "Successfully created account, redirecting...",
                duration = SnackbarDuration.Short
              )
              navController.popBackStack()
            }
          }
        ) },
        enabled = viewModel.isRegisterEnabled
      ){
        Text(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          text = "Register New Account",
          fontSize = 18.sp
        )
      }
    }
  }
}