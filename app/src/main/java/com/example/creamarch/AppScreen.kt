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

@Composable
fun AppScreen(
	navController: NavHostController = rememberNavController(),
	distanceTracker : DistanceTracker,
	menuStatus: MenuStatus
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
			composable(BottomItem.Team.route)
			{
				TeamMenu(
					modifier = Modifier.padding(innerPadding)
				)
			}

			composable(BottomItem.Collection.route)
			{
				CollectionMenu(
					modifier = Modifier.padding(innerPadding),
					menuStatus = menuStatus,
					playerDex = PlayerDex
				)
			}
			composable(BottomItem.Exploration.route)
			{
				ExplorationMenu(
					modifier = Modifier.padding(innerPadding),
					distanceTracker = distanceTracker
				)
			}
			composable(BottomItem.Player.route)
			{
				PlayerMenu(
					modifier = Modifier.padding(innerPadding)
				)
			}
			composable(BottomItem.Settings.route) {
				ParametresMenu(
					modifier = Modifier.padding(innerPadding),
					onMusicToggle = { isEnabled ->
						// Gérer l'état de la musique ici
						println("Musique activée : $isEnabled")
						// Vous pouvez ajouter une sauvegarde dans SharedPreferences ou DataStore
					},
					onVibrationToggle = { isEnabled ->
						// Gérer l'état des vibrations ici
						println("Vibrations activées : $isEnabled")
						// Vous pouvez également sauvegarder cet état
					}
				)
			}
		}
	}
}


