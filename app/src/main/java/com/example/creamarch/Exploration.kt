package com.example.creamarch

import DistanceTracker
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creamarch.ui.theme.Pink80
import com.example.creamarch.ui.theme.Purple80
import com.example.creamarch.ui.theme.PurpleGrey80
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

// Variables pour la génération des créatures
var prevFab = 0
var fab = 1

fun distance(): Int {
	val f3 = prevFab + fab
	prevFab = fab
	fab = f3
	val f4 = f3 * (100 - Random.nextInt(-5, 5))
	return f4 / 10
}

// Génération dynamique de la liste des créatures
fun generateNewCreatureList(): MutableList<Pair<Creature, Int>> {
	val newCreatureList = (1..22).map {
		Pair(
			first = Dex.species.values.filter { it.rarity != Rarity.Legendary }.random().spawnNewCreature(5),
			second = distance()
		)
	}.toMutableList()

	val indexLegend = newCreatureList.indexOfFirst { it.second >= legendary.second }

	if (indexLegend != -1 && newCreatureList[indexLegend] != legendary) {
		newCreatureList.add(indexLegend, legendary)
	}
	return newCreatureList
}

// Créature légendaire
val legendary = Pair(
	first = Dex.species.values.filter { it.rarity == Rarity.Legendary }.random().spawnNewCreature(20),
	second = 100
)

// Repository pour gérer les créatures
object CreatureRepository {
	private val _creatureList = MutableStateFlow<List<Pair<Creature, Int>>>(emptyList())
	val creatureList: StateFlow<List<Pair<Creature, Int>>> get() = _creatureList

	fun updateCreatureList(newList: List<Pair<Creature, Int>>) {
		_creatureList.value = newList
	}
}



@Composable
fun CreatureItem(
	creature: Creature,
	distance: Int,
	capture: Boolean,
	legendary: Boolean,
	onCapture: () -> Unit,
	modifier: Modifier = Modifier
) {
	var bacMod = Modifier
		.fillMaxWidth()
		.padding(10.dp)
	if (capture) {
		bacMod = if (legendary) bacMod.background(color = Purple80)
		else bacMod.background(color = Pink80)
	} else if (legendary) {
		bacMod = bacMod.background(color = PurpleGrey80)
	}

	Card(
		modifier = modifier.clickable(enabled = capture, onClick = onCapture),
	) {
		Row(modifier = bacMod) {
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
fun HealthBar(currentHealth: Int, maxHealth: Int, modifier: Modifier = Modifier) {
	val healthPercentage = (currentHealth / maxHealth.toFloat()).coerceIn(0f, 1f)
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(10.dp)
			.background(Color.Gray) // Couleur de fond de la barre de vie
	) {
		Box(
			modifier = Modifier
				.fillMaxHeight()
				.fillMaxWidth(healthPercentage) // Largeur en fonction de la santé
				.background(Color.Green) // Couleur de la barre de vie
		)
	}
}

// Ajout dans votre service ou activité principale pour initialiser la liste des créatures
fun initializeCreatures() {
	val initialList = generateNewCreatureList()
	CreatureRepository.updateCreatureList(initialList)
}

@Composable
fun ExplorationMenu(
	distanceTracker: DistanceTracker,
	modifier: Modifier = Modifier,
) {
	val debug = true
	val dist by StepCounterService.distanceWalked.collectAsState(initial = 0.0)
	val walkedDistance = if (debug) 2000 else dist.toInt()

	// Observer la liste des créatures
	val creatureList by CreatureRepository.creatureList.collectAsState()

	// Liste visible des créatures
	val nCreatures = remember(creatureList) {
		creatureList.filter { it.second <= walkedDistance }
	}

	// Calculer l'index et la distance jusqu'à la prochaine créature
	val nextIndex = creatureList.indexOfFirst { it.second > walkedDistance }
	val tillNext = if (nextIndex != -1) creatureList[nextIndex].second - walkedDistance else 0

	var showDialog by remember { mutableStateOf(false) }
	var capturedCreature by remember { mutableStateOf<Pair<Creature, Int>?>(null) }

	fun captureCreature(creature: Pair<Creature, Int>) {
		capturedCreature = creature
		showDialog = true
	}

	Column(modifier = modifier.padding(16.dp)) {
		// Afficher la distance parcourue
		Text(
			text = "Vous avez parcouru $walkedDistance m!",
			fontSize = 30.sp,
			modifier = Modifier.padding(bottom = 16.dp)
		)

		// Afficher la distance jusqu'à la prochaine créature
		if (tillNext > 0) {
			Text(
				text = "Créature suivante dans $tillNext m!",
				fontSize = 20.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 16.dp)
			)
		} else {
			Text(
				text = "Aucune autre créature proche!",
				fontSize = 20.sp,
				color = Color.Red,
				modifier = Modifier.padding(bottom = 16.dp)
			)
		}

		// Liste des créatures
		LazyColumn {
			items(nCreatures) { creature ->
				creature?.let {
					val isCapturable = it.second <= walkedDistance
					CreatureItem(
						creature = it.first,
						distance = it.second,
						capture = isCapturable,
						legendary = (it.first.baseData == legendary.first.baseData),
						onCapture = { captureCreature(it) },
						modifier = Modifier.padding(10.dp)
					)
				}
			}
		}

		// Afficher la boîte de dialogue si une créature est capturée
		if (showDialog && capturedCreature != null) {
			AlertDialog(
				onDismissRequest = { showDialog = false },
				title = {
					Text(
						text = "Félicitations!",
						fontSize = 24.sp,
						textAlign = TextAlign.Center
					)
				},
				text = {
					Text(
						text = "Vous avez capturé ${capturedCreature!!.first.baseData.name}!",
						fontSize = 18.sp,
						textAlign = TextAlign.Center
					)
				},
				confirmButton = {
					Button(onClick = { showDialog = false }) {
						Text(text = "OK")
					}
				}
			)
		}
	}
}


