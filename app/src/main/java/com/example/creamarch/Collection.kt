package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
fun CollectionItem(
	id: Int,
	species: CreatureSpecies,
	isCaught: Boolean,
	modifier: Modifier = Modifier
)
{
	var painter: Painter = if (isCaught) painterResource(species.menuSprite)
	else painterResource(R.drawable.what)

	Card(
		onClick = { if (isCaught) collectionItemOpenPopup(species) },
		modifier = modifier
			.padding(5.dp))
	{
		Column(
			modifier = modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally)
		{
			Image(
				painter = painter,
				contentDescription = null,
				modifier = modifier
					.size(60.dp)
			)
			Text(text = "#" + id.toString().padStart(3, '0'))
		}
	}
}

fun collectionItemOpenPopup(creatureSpecies: CreatureSpecies)
{
	MenuStatuses.CollectionPopupIsOpen = true
	MenuStatuses.CollectionPopupSpecies = creatureSpecies
}

@Composable
fun CollectionMenu(modifier: Modifier = Modifier)
{
	Box(modifier = modifier)
	{
		LazyVerticalGrid(
			columns = GridCells.Fixed(4),
			modifier = modifier
		)
		{
			items(Dex.species.entries.toList())
			{ item ->
				CollectionItem(
					id = item.key, species = item.value, isCaught = Random.nextBoolean()
				)
			}
		}

		// card
		if (MenuStatuses.CollectionPopupIsOpen)
		{
			Card(modifier = modifier
				.padding(80.dp))
			{
				Column(
					modifier = modifier,
					horizontalAlignment = Alignment.CenterHorizontally)
				{
					Image(
						painter = painterResource(MenuStatuses.CollectionPopupSpecies.menuSprite),
						contentDescription = null,
						modifier = modifier
							.size(60.dp)
					)
					Text(
						text = "Lorem ipsum dolor sit amet",
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}