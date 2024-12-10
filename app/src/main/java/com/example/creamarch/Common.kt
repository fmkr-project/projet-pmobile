package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

// Function to display the top bar (TopAppBar) of the application
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(modifier: Modifier = Modifier)
{
	// TopAppBar with a centered title and a red background
	CenterAlignedTopAppBar(
		title = { Text(
			text = "Créamarch",  // Title of the application
			color = Color.White  // Text color
		) },
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = Color.Red,  // Background color of the bar
		),
		modifier = modifier,
	)
}

// Definition of different routes for the bottom navigation bar
sealed class BottomItem(val route: String)
{
	data object Team: BottomItem("team")  // Route for the team menu
	data object Collection: BottomItem("collection")  // Route for the collection menu
	data object Exploration: BottomItem("exploration")  // Route for exploration
	data object Player: BottomItem("player")  // Route for the player's profile
	data object Settings: BottomItem("settings")  // Route for settings
}

// Function to display the bottom navigation bar (BottomBar)
@Composable
fun MainBottomBar(
	modifier: Modifier = Modifier,  // Layout modifier
	navController: NavHostController  // Navigation controller to handle navigation actions
)
{
	// Define the navigation bar
	NavigationBar(
		modifier = modifier,
		containerColor = Color.Red,  // Background color of the navigation bar
	) {
		// Get the current navigation route
		val navBackStackEntry by navController.currentBackStackEntryAsState()
		val currentRoute = navBackStackEntry?.destination?.route

		// Display navigation items
		Row(modifier = modifier) {
			// Item for the team menu
			NavigationBarItem(
				selected = currentRoute == BottomItem.Team.route,
				onClick = {
					navController.navigate(BottomItem.Team.route)  // Navigate to the team menu
				},
				icon = { Image(
					imageVector = Icons.AutoMirrored.Rounded.List,  // Icon for the list
					contentDescription = null
				) },
				label = { Text(text = "Team")}  // Label for the item
			)

			// Item for the collection menu
			NavigationBarItem(
				selected = currentRoute == BottomItem.Collection.route,
				onClick = {
					navController.navigate(BottomItem.Collection.route)  // Navigate to the collection
				},
				icon = { Icon(
					imageVector = Icons.TwoTone.Star,  // Icon for the collection (star)
					contentDescription = null
				) },
				label = { Text(text = "Collection")}  // Label for the item
			)

			// FloatingActionButton in the center for exploration
			FloatingActionButton(
				onClick = {
					navController.navigate(BottomItem.Exploration.route)  // Navigate to exploration
				},
				shape = CircleShape,  // Circular shape for the button
				modifier = Modifier
					.size(75.dp)  // Size of the button
					.padding(5.dp),  // Padding around the button
				containerColor = Color.White,  // Background color of the button
				elevation = FloatingActionButtonDefaults.elevation(0.dp)  // Button elevation
			) {
				// Icon in the center of the button (could be a logo or specific image)
				Image(
					painter = painterResource(R.drawable.bleutruk_icon),  // Image resource for the icon
					contentDescription = null,
					modifier = Modifier
						.size(48.dp)  // Size of the icon
				)
			}

			// Item for the player's profile
			NavigationBarItem(
				selected = currentRoute == BottomItem.Player.route,
				onClick = { navController.navigate(BottomItem.Player.route) },  // Navigate to the player's profile
				icon = { Icon(
					imageVector = Icons.Rounded.Face,  // Icon for the profile
					contentDescription = null
				) },
				label = { Text(text = "Player")}  // Label for the item
			)

			// Item for the settings
			NavigationBarItem(
				selected =  currentRoute == BottomItem.Settings.route,
				onClick = { navController.navigate(BottomItem.Settings.route) },  // Navigate to settings
				icon = { Icon(
					imageVector = Icons.Rounded.Settings,  // Icon for settings
					contentDescription = null
				) },
				label = { Text(text = "Settings")}  // Label for the item
			)
		}
	}
}
