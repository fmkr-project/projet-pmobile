package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun CollectionItem(
	species: CreatureSpecies,
	isCaught: Boolean,
	modifier: Modifier = Modifier
)
{
	Card(onClick = { /*TODO*/ })
	{
		Row(modifier = modifier)
		{
			// TODO other image if player hasn't caught this species
			Image(
				painter = painterResource(species.menuSprite),
				contentDescription = null
			)
			Text(text = "hej")
		}
	}
}

@Composable
fun CollectionMenu(modifier: Modifier = Modifier)
{
	LazyVerticalGrid(
		columns = GridCells.Fixed(4),
		modifier = modifier)
	{
		items(Dex.species.values.toList())
		{
			item ->
			CollectionItem(
				species = item, isCaught = true
			)
		}
	}
}