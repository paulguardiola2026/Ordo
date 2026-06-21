package com.example.capstone2026.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            },
            icon = { Text("⌂") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute.startsWith("schedule"),
            onClick = {
                navController.navigate("schedule_monthly")
            },
            icon = { Text("📅") },
            label = { Text("Schedule") }
        )

        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = {
                navController.navigate("settings")
            },
            icon = { Text("⚙") },
            label = { Text("Settings") }
        )
    }
}