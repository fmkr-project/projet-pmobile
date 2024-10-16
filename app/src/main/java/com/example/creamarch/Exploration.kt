package com.example.creamarch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.creamarch.ui.theme.CreamarchTheme


@Composable
fun CreatureItem(
	creature: Creature,
	modifier: Modifier = Modifier
)
{
	Card(modifier = modifier)
	{
		Row(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp))
		{
			// TODO sprite goes here

			Column {
				Text(
					text = creature.baseData.name

				)
				Text(
					text = "Niv. " + creature.level.toString()

				)
			}
		}
	}
}

@Composable
fun ExplorationMenu(modifier: Modifier = Modifier)
{
	// Get the list of nearby creatures
	// todo temp
	var nearbyCreatures = (1..300).map { Dex.species[1]?.spawnNewCreature() }
	print(nearbyCreatures)

	// Compose
	Column(modifier = modifier)
	{
		Text(
			text = "Lorem ipsum dolor sit amet",
			modifier = modifier
		)
		// Scrolling list of nearby creatures
		LazyColumn {
			items(nearbyCreatures) {
				if (it != null) {
					CreatureItem(
						creature = it,
						modifier = Modifier.padding(10.dp))
				}
			}
		}
	}
}

@Composable
@Preview(showBackground = true)
fun ExplorationPreview()
{
	CreamarchTheme {
		ExplorationMenu()
	}
}