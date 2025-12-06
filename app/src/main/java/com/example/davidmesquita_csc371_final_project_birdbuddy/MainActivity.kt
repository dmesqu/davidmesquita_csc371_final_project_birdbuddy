package com.example.davidmesquita_csc371_final_project_birdbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.davidmesquita_csc371_final_project_birdbuddy.ui.navigation.Screen
import com.example.davidmesquita_csc371_final_project_birdbuddy.ui.screens.*
import com.example.davidmesquita_csc371_final_project_birdbuddy.ui.theme.BirdBuddyTheme
import com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel.AuthViewModel
import com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel.BirdViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val birdViewModel: BirdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BirdBuddyTheme {
                val navController = rememberNavController()
                val authState by authViewModel.uiState.collectAsState()
                LaunchedEffect(authState.currentUser) {
                    birdViewModel.setCurrentUser(authState.currentUser?.id)
                }
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Register.route) {
                        RegisterScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            onRegisterSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Home.route) {
                        HomeScreen(
                            navController = navController,
                            birdViewModel = birdViewModel,
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Identify.route) {
                        IdentifyBirdScreen(
                            navController = navController,
                            birdViewModel = birdViewModel
                        )
                    }
                    composable(Screen.MyCollection.route) {
                        MyCollectionScreen(
                            navController = navController,
                            birdViewModel = birdViewModel
                        )
                    }
                    composable(Screen.AllBirds.route) {
                        AllBirdsScreen(
                            navController = navController,
                            birdViewModel = birdViewModel
                        )
                    }
                    composable(
                        route = Screen.BirdDetail.route,
                        arguments = listOf(
                            navArgument("birdId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val birdId = backStackEntry.arguments?.getLong("birdId") ?: -1L
                        BirdDetailScreen(
                            birdId = birdId,
                            birdViewModel = birdViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
