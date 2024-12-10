package com.example.creamarch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random


// Composable function to display a collection item (a creature)
@Composable
fun CollectionItem(
	id: Int,  // The creature's ID
	species: CreatureSpecies,  // Specific details of the creature (its sprite, stats, etc.)
	menuStatus: MenuStatus,  // Current status of the menu
	onClick: () -> Unit,  // Action to perform when an item is clicked
	modifier: Modifier = Modifier  // Layout modifier
)
{
	// Check if the creature has been seen or caught
	val isSeen = PlayerDex.isSeen(id)
	val isCaught = PlayerDex.isCaught(id)

	// Choose the image based on the creature's state (seen or not)
	val painter: Painter = if (isSeen) painterResource(species.menuSprite)
	else painterResource(R.drawable.what)

	// Card displaying the creature, including its image and ID
	Card(
		onClick =
		{
			// If the creature has been seen, open the collection popup
			if (isSeen)
			{
				onClick()
				menuStatus.openCollectionPopup()
				menuStatus.collectionPopupSpecies = species
			}
		},
		modifier = modifier
			.padding(5.dp)  // Padding around the card
	)
	{
		// Column containing the image and the creature's ID
		Column(
			modifier = modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally)
		{
			// Image representing the creature
			Image(
				painter = painter,
				contentDescription = null,
				modifier = modifier
					.size(60.dp)  // Size of the image
			)
			// Row containing the creature's ID and catch status
			Row(
				modifier = modifier,
				verticalAlignment = Alignment.CenterVertically
			)
			{
				// If caught, display the "caught" icon
				if (isCaught)
				{
					Image(
						painter = painterResource(R.drawable.mon_caught),
						contentDescription = null,
						modifier = modifier
							.size(16.dp)
					)
				}
				// Display the formatted ID of the creature
				Text(text = "#" + id.toString().padStart(3, '0'))
			}
		}
	}
}

// Composable function to display the collection menu
@Composable
fun CollectionMenu(
	modifier: Modifier = Modifier,  // Layout modifier
	menuStatus: MenuStatus,  // Current status of the menu
	playerDex: PlayerDex  // Player data, containing information about the collection
)
{
	// Variable to control the opening/closing of the popup
	var isCardOpen: Boolean by remember { mutableStateOf(menuStatus.collectionPopupIsOpen) }

	Box(modifier = modifier)
	{
		// Grid to display all collection items
		LazyVerticalGrid(
			columns = GridCells.Fixed(4),  // Displaying in 4 columns
			modifier = modifier
		)
		{
			// Iterate through the species in the Dex and create an item for each creature
			items(Dex.species.entries.toList())
			{ item ->
				CollectionItem(
					id = item.key,
					species = item.value,
					onClick = { isCardOpen = true },  // Open the popup on click
					menuStatus = menuStatus
				)
			}
		}

		// Display the popup when isCardOpen is true
		if (isCardOpen) {
			AlertDialog(
				onDismissRequest = {},  // Do nothing when the popup is dismissed
				title =
				{
					// Title of the popup with the creature's name
					Text(
						text = menuStatus.collectionPopupSpecies.name.toUpperCase(Locale.current),
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				},
				text =
				{
					// Content of the popup with image, stats, and description
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.fillMaxHeight(0.6f)
							.padding(20.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center
					)
					{
						// Image of the creature
						Image(
							painter = painterResource(menuStatus.collectionPopupSpecies.menuSprite),
							contentDescription = null,
							modifier = Modifier
								.size(96.dp)
						)
						Spacer(
							modifier = Modifier
								.size(15.dp)
						)
						// Creature stats
						Text(
							text = "Seen: " + PlayerDex.getSeen(
								Dex.getSpeciesId(
									menuStatus.collectionPopupSpecies
								)
							),
							fontStyle = FontStyle.Italic,
							fontSize = 20.sp
						)
						Text(
							text = "Caught: " + PlayerDex.getCaught(
								Dex.getSpeciesId(
									menuStatus.collectionPopupSpecies
								)
							),
							fontStyle = FontStyle.Italic,
							fontSize = 20.sp
						)
						Spacer(
							modifier = Modifier
								.size(12.dp)
						)
						// Display combat stats of the creature
						Text(
							text = "HP " + menuStatus.collectionPopupSpecies.baseStats.maxHp
									+ " ATK " + menuStatus.collectionPopupSpecies.baseStats.attack,
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold
						)
						Spacer(
							modifier = Modifier
								.size(28.dp)
						)
						// Creature description or default text if not caught
						Text(
							text = if (PlayerDex.isCaught(Dex.getSpeciesId(menuStatus.collectionPopupSpecies)))
								Dex.descriptions[Dex.getSpeciesId(menuStatus.collectionPopupSpecies)]!!
							else "??? ? ???? ? ?????? ????????? ?? ??????",
							fontSize = 18.sp,
							textAlign = TextAlign.Center
						)
						Spacer(
							modifier = Modifier
								.size(8.dp)
								.weight(1f)
						)
						// Button to close the popup
						OutlinedIconButton(
							onClick = {
								isCardOpen = false
								menuStatus.collectionPopupIsOpen = false
							},
							modifier = Modifier
								.size(52.dp)
						)
						{
							Icon(Icons.Default.Close, contentDescription = null)
						}
					}
				},
				confirmButton = {}  // No confirmation button
			)
		}
	}
}

// Preview of the CollectionMenu interface
@Preview
@Composable
fun CollectionPreview()
{
	CollectionMenu(
		menuStatus = MenuStatus(collectionPopupIsOpen = true),
		playerDex = PlayerDex
	)
}
