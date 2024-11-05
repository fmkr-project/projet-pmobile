package com.example.creamarch

import DistanceTracker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creamarch.ui.theme.CreamarchTheme
import com.example.creamarch.ui.theme.Pink80
import kotlin.random.Random

@Composable
fun CreatureItem(
	creature: Creature,
	distance: Int,
	capture: Boolean,
	onCapture: () -> Unit,
	modifier: Modifier = Modifier
)
{
	var bacMod = Modifier
		.fillMaxWidth()
		.padding(10.dp)
	if (capture) bacMod = bacMod.background(color = Pink80)
	Card(
		modifier = modifier.clickable(enabled = capture, onClick = onCapture),
	)
	{
		Row(modifier = bacMod)
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
fun ExplorationMenu(distanceTracker: DistanceTracker, modifier: Modifier =Modifier)
{
	val walkedDistance by distanceTracker.distance.collectAsState(initial = 0f)
	//var walkedDistance = 2000

	// Get the list of nearby creatures
	// todo temp
	var prevFab = 1
	var fab = 1

	fun distance(): Int {
		val f3 = prevFab + fab
		prevFab = fab
		fab = f3
		val f4 = f3 * (100 - Random.nextInt(-5, 5))
		return f4
	}

	val nearbyCreatures = remember {
		mutableStateListOf<Pair<Creature, Int>>().apply {
			addAll((1..22).map {
				Pair(
					first = Dex.species.values.random().spawnNewCreature(),
					second = distance()
				)
			})
		}
	}

	val legendary = Pair(
		first = Creature(
			Dex.species[666] ?: CreatureSpecies("Raté",
				R.drawable.ic_launcher_background
			), // todo choose random unknown legendary creature
			50,
			Stats(100, 100, 100, 100)
		),
		second = 10000)
	val indexLegend = nearbyCreatures.indexOfFirst { it.second >= legendary.second }

	if (indexLegend != -1 && nearbyCreatures[indexLegend] != legendary) {
		nearbyCreatures.add(indexLegend, legendary)
	}

	val nCreatures = nearbyCreatures.subList(0, indexLegend+1)

	fun captureCreature(creature: Pair<Creature, Int>) {
		nearbyCreatures.remove(creature)
	}
	// Compose
	Column(modifier = modifier)
	{
		Text(
			text = "Vous avez parcouru $walkedDistance m!",
			fontSize = 30.sp,
			modifier = modifier
		)
		// Scrolling list of nearby creatures
		LazyColumn {
			items(nCreatures) {
				if (it != null) {
					var isCapturable = false
					if (it.second < walkedDistance)  isCapturable = true
					CreatureItem(
						creature = it.first,
						distance = it.second, // TODO geolocate
						capture = isCapturable,
						onCapture = { captureCreature(it) },
						modifier = Modifier.padding(10.dp)
					)
				}
			}
		}
	}
}

/*@Composable
@Preview(showBackground = true)
fun ExplorationPreview()
{
	val fakeDistanceTracker = FakeDistanceTracker()
	CreamarchTheme {
		ExplorationMenu(distanceTracker = )
	}
}*/