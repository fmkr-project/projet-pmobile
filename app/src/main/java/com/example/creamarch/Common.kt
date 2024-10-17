package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(modifier: Modifier = Modifier)
{
	CenterAlignedTopAppBar(
		title = { Text(
			text = "AppName goes here",
			color = Color.White
		) },
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = Color.Red,
		),
		modifier = modifier,

	)
}

val subMenus = listOf<String>("Lorem", "Ipsum", )

@Composable
fun MainBottomBar(modifier: Modifier = Modifier)
{
	NavigationBar(
		modifier = modifier,
		containerColor = Color.Red,
	) {
		Row(modifier = modifier) {
			NavigationBarItem(
				selected = false,
				onClick = { /*TODO*/ },
				icon = { Image(
					imageVector = Icons.AutoMirrored.Rounded.List,
					contentDescription = null
				) },
				label = { Text(text = "Équipe")}
			)

			NavigationBarItem(
				selected = false,
				onClick = { /*TODO*/ },
				icon = { Icon(
					imageVector = Icons.TwoTone.Star,
					contentDescription = null
				) },
				label = { Text(text = "Collection")}
			)

			FloatingActionButton(
				onClick = { /*TODO*/ },
				shape = CircleShape,
				modifier = Modifier
					.size(75.dp)
					.padding(5.dp),
				containerColor = Color.White,
				elevation = FloatingActionButtonDefaults.elevation(0.dp)
				) {
				Text(
					text = "C",
					fontSize = 40.sp,
					color = Color.Red,
				)
			}

			NavigationBarItem(
				selected = false,
				onClick = { /*TODO*/ },
				icon = { Icon(
					imageVector = Icons.Rounded.Face,
					contentDescription = null
				) },
				label = { Text(text = "Joueur")}
			)

			NavigationBarItem(selected = false,
				onClick = { /*TODO*/ },
				icon = { Icon(
					imageVector = Icons.Rounded.Settings,
					contentDescription = null
				) },
				label = { Text(text = "Paramètres")}
			)
		}
	}
}