package com.example.creamarch

import DistanceTracker
import ParametresMenu
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Main composable function for the app screen
@Composable
fun AppScreen(
	navController: NavHostController = rememberNavController(),  // Navigation controller
	distanceTracker: DistanceTracker,  // Distance tracking
	menuStatus: MenuStatus  // Current menu status of the app
)
{
	// Scaffold helps structure the interface with a top bar, bottom bar, and main content
	Scaffold(
		topBar = {
			MainTopBar()  // Top bar of the app
		},
		bottomBar = {
			MainBottomBar(navController = navController)  // Bottom bar with navigation
		},
		modifier = Modifier
			.fillMaxSize()  // Fill the available space
	)
	{ innerPadding ->
		// NavHost handles navigation between different destinations
		NavHost(
			navController = navController,
			startDestination = BottomItem.Exploration.route  // Initial destination
		)
		{
			// Define the different navigation destinations
			composable(BottomItem.Team.route)
			{
				TeamMenu(  // Team menu
					modifier = Modifier.padding(innerPadding)
				)
			}

			composable(BottomItem.Collection.route)
			{
				CollectionMenu(  // Collection menu
					modifier = Modifier.padding(innerPadding),
					menuStatus = menuStatus,  // Menu status
					playerDex = PlayerDex  // Player data
				)
			}
			composable(BottomItem.Exploration.route)
			{
				ExplorationMenu(  // Exploration menu
					modifier = Modifier.padding(innerPadding),
					distanceTracker = distanceTracker  // Distance tracking
				)
			}
			composable(BottomItem.Player.route)
			{
				PlayerMenu(  // Player menu
					modifier = Modifier.padding(innerPadding)
				)
			}
			composable(BottomItem.Settings.route) {
				ParametresMenu(  // Settings menu
					modifier = Modifier.padding(innerPadding),
					onMusicToggle = { isEnabled ->  // Handle music state
						println("Music enabled: $isEnabled")
					},
					onVibrationToggle = { isEnabled ->  // Handle vibration state
						println("Vibration enabled: $isEnabled")
					}
				)
			}
		}
	}
}
