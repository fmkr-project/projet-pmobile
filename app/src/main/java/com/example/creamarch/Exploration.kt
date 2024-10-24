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
	var prevFab = 1

	var fab = 1

	fun distance(): Int {
		val f3 = prevFab + fab
		prevFab = fab
		fab = f3
		val f4 = f3*(100 - Random.nextInt(-5,5))
		return f4
	}
	// Get the list of nearby creatures
	// todo temp
		Pair( first = Dex.species.values.random().spawnNewCreature(),
		second = distance())
	print(nearbyCreatures)

	val legendary = Pair(
		first = Creature(
			Dex.species[666] ?: CreatureSpecies("Raté",
				R.drawable.ic_launcher_background
			), // todo choose random unknown legendary creature
			50,
			Stats(100, 100, 100, 100)
		),
		second = 10000)
	val indexLegend = nearbyCreatures.indexOfFirst { it.second > legendary.second }

	nearbyCreatures.add(indexLegend, legendary)
	prevFab = 1
	fab = 1

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
						creature = it.first,
						distance = it.second, // TODO geolocate
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