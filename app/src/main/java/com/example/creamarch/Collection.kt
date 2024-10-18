package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun CollectionItem(
	id: Int,
	species: CreatureSpecies,
	isCaught: Boolean,
	modifier: Modifier = Modifier
)
{
	Card(
		onClick = { /*TODO*/ },
		modifier = modifier
			.padding(5.dp))
	{
		Column(
			modifier = modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally)
		{
			// TODO other image if player hasn't caught this species
			Image(
				painter = painterResource(species.menuSprite),
				contentDescription = null,
				modifier = modifier
					.size(60.dp)
			)
			Text(text = "#" + id.toString().padStart(3, '0'))
		}
	}
}

@Composable
fun CollectionMenu(modifier: Modifier = Modifier)
{
	LazyVerticalGrid(
		columns = GridCells.Fixed(4),
		modifier = modifier
	)
	{
		items(Dex.species.entries.toList())
		{
			item ->
			CollectionItem(
				id = item.key, species = item.value, isCaught = true
			)
		}
	}
}