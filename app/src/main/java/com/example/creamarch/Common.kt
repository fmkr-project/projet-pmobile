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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(modifier: Modifier = Modifier)
{
	CenterAlignedTopAppBar(
		title = { Text(
			text = "Créamarch",
			color = Color.White
		) },
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = Color.Red,
		),
		modifier = modifier,

	)
}

// Navigation
sealed class BottomItem(val route: String)
{
	data object Team: BottomItem("team")
	data object Collection: BottomItem("collection")
	data object Exploration: BottomItem("exploration")
	data object Player: BottomItem("player")
	data object Settings: BottomItem("settings")
}

@Composable
fun MainBottomBar(
	modifier: Modifier = Modifier,
	navController: NavHostController
)
{
	NavigationBar(
		modifier = modifier,
		containerColor = Color.Red,
	) {
		val navBackStackEntry by navController.currentBackStackEntryAsState()
		val currentRoute = navBackStackEntry?.destination?.route

		Row(modifier = modifier) {
			NavigationBarItem(
				selected = currentRoute == BottomItem.Team.route,
				onClick = {
					navController.navigate(BottomItem.Team.route)
					{

					}
				},
				icon = { Image(
					imageVector = Icons.AutoMirrored.Rounded.List,
					contentDescription = null
				) },
				label = { Text(text = "Équipe")}
			)

			NavigationBarItem(
				selected = currentRoute == BottomItem.Collection.route,
				onClick = {
					navController.navigate(BottomItem.Collection.route)
					{

					}
				},
				icon = { Icon(
					imageVector = Icons.TwoTone.Star,
					contentDescription = null
				) },
				label = { Text(text = "Collection")}
			)

			FloatingActionButton(
				onClick = {
					navController.navigate(BottomItem.Exploration.route)
					{

					}
				},
				shape = CircleShape,
				modifier = Modifier
					.size(75.dp)
					.padding(5.dp),
				containerColor = Color.White,
				elevation = FloatingActionButtonDefaults.elevation(0.dp)
				) {
				/*
				Text(
					text = "C",
					fontSize = 40.sp,
					color = Color.Red,
				)*/
				Image(
					painter = painterResource(R.drawable.bleutruk_icon),
					contentDescription = null,
					modifier = Modifier
						.size(48.dp)
				)
			}

			NavigationBarItem(
				selected = currentRoute == BottomItem.Player.route,
				onClick = { navController.navigate(BottomItem.Player.route) },
				icon = { Icon(
					imageVector = Icons.Rounded.Face,
					contentDescription = null
				) },
				label = { Text(text = "Joueur")}
			)

			NavigationBarItem(selected =  currentRoute == BottomItem.Settings.route,
				onClick = { navController.navigate(BottomItem.Settings.route)
						  },
				icon = { Icon(
					imageVector = Icons.Rounded.Settings,
					contentDescription = null
				) },
				label = { Text(text = "Paramètres")}
			)
		}
	}
}