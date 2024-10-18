package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creamarch.ui.theme.CreamarchTheme
import kotlin.random.Random


@Composable
fun CreatureItem(
	creature: Creature,
	distance: Int,
	modifier: Modifier = Modifier
)
{
	Card(
		modifier = modifier,
		onClick = {}
	)
	{
		Row(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp))
		{
			Image(
				painter = painterResource(creature.baseData.menuSprite),
				contentDescription = creature.baseData.name,
				modifier = modifier
					.fillMaxHeight()
					.size(36.dp)
			)

			Column(
				modifier = modifier
					.weight(1f)
			) {
				Text(
					text = creature.baseData.name,
					fontWeight = FontWeight.Bold,
					fontSize = 28.sp

				)
				Text(
					text = "niv. " + creature.level.toString()

				)
			}
			
			Text(
				text = "$distance m",
				modifier = modifier
					.wrapContentHeight()
			)
		}
	}
}

@Composable
fun ExplorationMenu(modifier: Modifier = Modifier)
{
	// Get the list of nearby creatures
	// todo temp
	var nearbyCreatures = (1..22).map { Dex.species.values.random().spawnNewCreature() }
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
						distance = 500, // TODO geolocate
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