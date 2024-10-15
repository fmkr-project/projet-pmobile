package com.example.creamarch

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
				icon = { /*TODO*/ },
				label = { Text(text = "Lorem")}
			)
			FloatingActionButton(
				onClick = { /*TODO*/ },
				) {
				Text(text = "C")
			}
			NavigationBarItem(
				selected = false,
				onClick = { /*TODO*/ },
				icon = { /*TODO*/ },
				label = { Text(text = "Ipsum")}
			)
		}
	}
}