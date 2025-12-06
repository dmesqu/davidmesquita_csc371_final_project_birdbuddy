package com.example.davidmesquita_csc371_final_project_birdbuddy.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Identify : Screen("identify")
    object MyCollection : Screen("my_collection")
    object AllBirds : Screen("all_birds")
    object BirdDetail : Screen("bird_detail/{birdId}") {
        fun createRoute(birdId: Long) = "bird_detail/$birdId"
    }
}
