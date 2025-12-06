package com.example.davidmesquita_csc371_final_project_birdbuddy.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.davidmesquita_csc371_final_project_birdbuddy.R
import com.example.davidmesquita_csc371_final_project_birdbuddy.ui.navigation.Screen
import com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel.AuthViewModel

@Composable
private fun GradientButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(gradient, shape = MaterialTheme.shapes.large)
                .then(
                    Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

//login
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    if (uiState.currentUser != null) {
        LaunchedEffect(uiState.currentUser) {
            onLoginSuccess()
        }
    }
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background
        )
    )
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }
    val fadeAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(600)
    )
    val logoScale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.92f,
        animationSpec = tween(600)
    )
    val sheetOffset by animateFloatAsState(
        targetValue = if (animateIn) 0f else 60f,
        animationSpec = tween(600)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 84.dp, start = 24.dp, end = 24.dp)
                .graphicsLayer {
                    alpha = fadeAlpha
                    scaleX = logoScale
                    scaleY = logoScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Bird Buddy icon",
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            shadowElevation = 24f
                            shape = RectangleShape
                            clip = true
                        }
                )
            }

            Text(
                text = "Bird Buddy",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Sign in to see all the birds you've discovered.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
                textAlign = TextAlign.Center
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = fadeAlpha
                    translationY = sheetOffset
                },
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Welcome back", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Enter your username and password to continue.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Hide" else "Show")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                uiState.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(8.dp))
                GradientButton(
                    text = if (uiState.isLoading) "Signing in..." else "Sign In",
                    enabled = !uiState.isLoading,
                    onClick = { authViewModel.login(username, password) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(
                    onClick = { navController.navigate(Screen.Register.route) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("New to Bird Buddy? Create an account")
                }
                Text(
                    "Bird Buddy helps kids explore nature in a safe, fun way.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

//reg
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }
    if (uiState.currentUser != null) {
        LaunchedEffect(uiState.currentUser) { onRegisterSuccess() }
    }
    val bgGradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background
        )
    )
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }
    val fadeAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(600)
    )
    val logoScale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.92f,
        animationSpec = tween(600)
    )
    val sheetOffset by animateFloatAsState(
        targetValue = if (animateIn) 0f else 60f,
        animationSpec = tween(600)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 84.dp, start = 24.dp, end = 24.dp)
                .graphicsLayer {
                    alpha = fadeAlpha
                    scaleX = logoScale
                    scaleY = logoScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Bird Buddy icon",
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            shadowElevation = 24f
                            shape = RectangleShape
                            clip = true
                        }
                )
            }
            Text(
                "Create your Bird Buddy account",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                "We’ll save the birds you discover so you can look back on them anytime.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
                textAlign = TextAlign.Center
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = fadeAlpha
                    translationY = sheetOffset
                },
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Create account", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        localError = null
                    },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        localError = null
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Hide" else "Show")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        localError = null
                    },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(if (confirmPasswordVisible) "Hide" else "Show")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                val errorToShow = localError ?: uiState.errorMessage
                if (errorToShow != null) {
                    Text(
                        text = errorToShow,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                GradientButton(
                    text = if (uiState.isLoading) "Creating..." else "Create Account",
                    enabled = !uiState.isLoading,
                    onClick = {
                        when {
                            username.isBlank() ->
                                localError = "Please enter a username."
                            password.isBlank() ->
                                localError = "Please enter a password."
                            password != confirmPassword ->
                                localError = "Passwords do not match."
                            else -> authViewModel.register(username, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Already have an account? Sign in")
                }
                Text(
                    "Parents can help kids sign up, then let them explore and log birds on their own.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
