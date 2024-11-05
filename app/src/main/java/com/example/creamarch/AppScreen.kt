package com.example.creamarch

import DistanceTracker
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

@Composable
fun AppScreen(
	navController: NavHostController = rememberNavController(),
	distanceTracker : DistanceTracker
)
{
	Scaffold(
		topBar = {
			MainTopBar()
		},
		bottomBar = {
			MainBottomBar(navController = navController)
		},
		modifier = Modifier
			.fillMaxSize()
	)
	{ innerPadding ->
		NavHost(
			navController = navController,
			startDestination = BottomItem.Exploration.route
		)
		{
			composable(BottomItem.Team.route) { TeamMenu(modifier = Modifier.padding(innerPadding)) }
			composable(BottomItem.Collection.route) { CollectionMenu(modifier = Modifier.padding(innerPadding)) }
			composable(BottomItem.Exploration.route) { ExplorationMenu(modifier = Modifier.padding(innerPadding), distanceTracker= distanceTracker) }
		}
	}
}

