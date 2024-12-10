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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.creamarch.ui.theme.Pink80
import com.example.creamarch.ui.theme.Purple80
import com.example.creamarch.ui.theme.PurpleGrey80
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random
// Variables for creature generation
var prevFab = 0
var fab = 1
var initialSubListSize = 0

// Function to calculate a random distance using the Fibonacci sequence
fun distance(): Int {
	val f3 = prevFab + fab
	prevFab = fab
	fab = f3
	val f4 = f3 * (100 - Random.nextInt(-5, 5))
	return f4 / 10
}

// Function to generate a new list of creatures with randomized rarity and distance
fun generateNewCreatureList(): MutableList<Pair<Creature, Int>> {
	val newCreatureList: MutableList<Pair<Creature, Int>> = mutableListOf()
	for (i in (1..22)) {
		// Roll a rarity.
		// 58% Common, 28% Uncommon, 10% Rare, 4% Epic
		val random = Random.nextFloat()
		val rarity: Rarity = if (random < 0.58f) Rarity.Common
		else if (random < 0.86f) Rarity.Uncommon
		else if (random < 0.96f) Rarity.Rare
		else Rarity.Epic
		newCreatureList.add(
			Pair(
				first = Dex.species.values.filter { it.rarity == rarity }.random().spawnNewCreature(6 + newCreatureList.size),
				second = distance()  // Generate a random distance for the creature
			)
		)
	}

	// Ensure legendary creature is added at the correct position
	val indexLegend = newCreatureList.indexOfFirst { it.second >= legendary.second }

	initialSubListSize = if (indexLegend != -1) indexLegend + 1 else newCreatureList.size

	// Add legendary creature if necessary
	if (indexLegend != -1 && newCreatureList[indexLegend] != legendary) {
		newCreatureList.add(indexLegend, legendary)
	}
	return newCreatureList
}

// Legendary creature definition
val legendary = Pair(
	first = Dex.species.values.filter { it.rarity == Rarity.Legendary }.random().spawnNewCreature(20),
	second = 100  // Legendary creature always has distance value 100
)

// Creature repository to manage creature list
object CreatureRepository {
	private val _creatureList = MutableStateFlow<List<Pair<Creature, Int>>>(emptyList())
	val creatureList: StateFlow<List<Pair<Creature, Int>>> get() = _creatureList

	// Update the creature list
	fun updateCreatureList(newList: List<Pair<Creature, Int>>) {
		_creatureList.value = newList
	}
}

// Function to display a single creature item in the list
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

	// Apply background color based on capture status and whether the creature is legendary
	if (capture) {
		bacMod = if (legendary) bacMod.background(color = Purple80)
		else bacMod.background(color = Pink80)
	} else if (legendary) {
		bacMod = bacMod.background(color = PurpleGrey80)
	}

	// Card displaying the creature's details (image, name, level, distance)
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
					text = "Level " + creature.level.toString()
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

// Function to display the health bar of a creature
@Composable
fun HealthBar(currentHealth: Int, maxHealth: Int, modifier: Modifier = Modifier) {
	val healthPercentage = (currentHealth / maxHealth.toFloat()).coerceIn(0f, 1f)
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(10.dp)
			.background(Color.Gray)  // Background color of the health bar
	) {
		Box(
			modifier = Modifier
				.fillMaxHeight()
				.fillMaxWidth(healthPercentage)  // Width based on health percentage
				.background(Color.Green)  // Color of the health bar
		)
	}
}

// Initialize creatures when the app starts
fun initializeCreatures() {
	val initialList = generateNewCreatureList()
	CreatureRepository.updateCreatureList(initialList)
}

// Composable function for the Exploration menu
@Composable
fun ExplorationMenu(
	distanceTracker: DistanceTracker,
	modifier: Modifier = Modifier,
) {
	val debug = true
	val dist by StepCounterService.distanceWalked.collectAsState(initial = 0.0)
	val walkedDistance = if (debug) 2000 else dist.toInt()

	var lastHealedDistance by remember { mutableIntStateOf(0) }

	// Heal creatures every 100 meters
	if (walkedDistance >= lastHealedDistance + 100) {
		val healingAmount = 10

		playerTeam.forEach { creature ->
			val newHp = (creature.stats.currentHp + healingAmount).coerceAtMost(creature.stats.maxHp)
			creature.stats.currentHpState.intValue = newHp
		}
		lastHealedDistance = (walkedDistance / 100) * 100
	}

	var nearbyCreatures by remember { mutableStateOf(CreatureRepository.creatureList.value) }
	var nCreatures by remember {
		mutableStateOf(nearbyCreatures.take(initialSubListSize).toList())
	}
	var showDialog by remember { mutableStateOf(false) }
	var capturedCreature by remember { mutableStateOf<Pair<Creature, Int>?>(null) }
	var changeTeam by remember { mutableStateOf(false) }
	var youSurOfChange by remember { mutableStateOf(false) }
	var whichChange by remember { mutableIntStateOf(0) }

	// Function to capture a creature
	fun captureCreature(creature: Pair<Creature, Int>) {
		capturedCreature = creature
		showDialog = true  // Open the battle popup
	}

	// Calculate the index and distance to the next creature
	val nextIndex = nearbyCreatures.indexOfFirst { it.second > walkedDistance }
	val tillNext = if (nextIndex != -1) nearbyCreatures[nextIndex].second - walkedDistance else 0

	Column(modifier = modifier.padding(16.dp)) {
		// Display the walked distance
		Text(
			text = "You've walked $walkedDistance meters!",
			fontSize = 28.sp,
			modifier = Modifier.padding(bottom = 16.dp)
		)

		// Display distance to next creature
		if (tillNext > 0) {
			Text(
				text = "Next creature in $tillNext meters!",
				fontSize = 20.sp,
				color = Color.Gray,
				modifier = Modifier.padding(bottom = 16.dp)
			)
		} else {
			Text(
				text = "No more creatures nearby!",
				fontSize = 20.sp,
				color = Color.Red,
				modifier = Modifier.padding(bottom = 16.dp)
			)
		}

		// List of creatures
		LazyColumn {
			items(nCreatures) { creature ->
				creature.let {
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

		// Combat popup code
		if (showDialog && capturedCreature != null && !deadTeam()) {
			// Combat logic with the captured creature
			AlertDialog(
				onDismissRequest = { /* Block outside closing */ },
				title = { Text("Combat!", fontSize = 30.sp, textAlign = TextAlign.Center) },
				text = {
					Column(
						verticalArrangement = Arrangement.SpaceEvenly,
						modifier = Modifier.fillMaxHeight()
					) {
						HealthBar(
							currentHealth = capturedCreature!!.first.stats.currentHpState.intValue,
							maxHealth = capturedCreature!!.first.stats.maxHp
						)
						Image(
							painter = painterResource(id = capturedCreature!!.first.baseData.menuSprite),
							contentDescription = "Creature to defeat",
							modifier = Modifier
								.fillMaxWidth()
								.aspectRatio(1.5f)
								.clickable {
									capturedCreature!!.first.stats.reduceHp(clickPower())
									if (!deadTeam()) {
										var randTeam = Random.nextInt(playerTeam.size)
										while (playerTeam[randTeam].stats.currentHpState.intValue <= 0)
											randTeam = Random.nextInt(playerTeam.size)
										playerTeam[randTeam].stats.reduceHp(
											(capturedCreature!!.first.stats.attack * (85 .. 115).random() / 100f).toInt()
										)
										if (deadTeam()) PlayerDex.see(Dex.getSpeciesId(capturedCreature!!.first.baseData))
									}
									if (capturedCreature!!.first.stats.currentHpState.intValue <= 0) {
										PlayerDex.catch(Dex.getSpeciesId(capturedCreature!!.first.baseData))
										showDialog = false
										nearbyCreatures = nearbyCreatures.filter { it != capturedCreature }.toMutableList()
										nCreatures = nearbyCreatures.take(initialSubListSize)
										if (playerTeam.size < 6) addCreatureToTeam(capturedCreature!!.first)
										else changeTeam = true
									}
									Log.d("StepCounterService", "${capturedCreature!!.first.stats.currentHpState.intValue}")
								}
						)
						// Display the player's team health bar
						LazyVerticalGrid(
							columns = GridCells.Fixed(3),
							modifier = Modifier
								.fillMaxWidth()
								.weight(1f),
							verticalArrangement = Arrangement.SpaceEvenly,
							horizontalArrangement = Arrangement.SpaceEvenly
						) {
							items(playerTeam) {
								var imageMod = Modifier
									.fillMaxWidth()
									.aspectRatio(1f)
								if (it.stats.currentHpState.intValue <= 0) imageMod = imageMod.background(color = Color.Red)
								if (it != null) {
									Column(modifier = Modifier.padding(4.dp)) {
										Image(
											painter = painterResource(id = it.baseData.menuSprite),
											contentDescription = "My creatures",
											modifier = imageMod
										)
										HealthBar(
											currentHealth = it.stats.currentHpState.intValue,
											maxHealth = it.stats.maxHp,
											modifier = Modifier.padding(4.dp)
										)
									}
								}
							}
						}
					}
				},
				confirmButton = {
					Button(onClick = {
						PlayerDex.see(Dex.getSpeciesId(capturedCreature!!.first.baseData))
						showDialog = false
					}) {
						Text("Flee", fontSize = 30.sp, textAlign = TextAlign.Center)
					}
				}
			)
		}

		// Code to select which creature to replace in the team
		if (changeTeam) {
			AlertDialog(
				modifier = Modifier.fillMaxSize(),
				onDismissRequest = { /* Block outside closing */ },
				title = {
					Text(
						text = buildAnnotatedString {
							append("Which creature do you want to replace with ")
							withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
								append(capturedCreature!!.first.baseData.name)
								append(" level ")
								append(capturedCreature!!.first.level.toString())
							}
							append("?")
						},
						fontSize = 18.sp
					)
				},
				text = {
					LazyColumn {
						items(playerTeam) {
							TeamMember(
								creature = it,
								pv = it.stats.currentHpState.intValue,
								maxPV = it.stats.maxHp,
								modifier = Modifier
									.padding(8.dp)
									.clickable {
										youSurOfChange = true
										whichChange = playerTeam.indexOf(it)
									}
							)
						}
					}
				},
				confirmButton = {
					Button(onClick = { changeTeam = false }) {
						Text(text = "None")
					}
				}
			)
		}

		// Popup to confirm creature swap
		if (youSurOfChange) {
			Dialog(onDismissRequest = { /* Block outside closing */ }) {
				Column(
					modifier = Modifier
						.fillMaxHeight(0.4f)
						.fillMaxWidth()
						.background(Color.White),
					verticalArrangement = Arrangement.SpaceEvenly
				) {
					Text(
						text = "Are you sure about your choice?",
						fontSize = 52.sp,
						lineHeight = 52.sp
					)
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceEvenly
					) {
						Button(onClick = {
							addCreatureToTeam(capturedCreature!!.first, whichChange)
							changeTeam = false
							youSurOfChange = false
						}) {
							Text(text = "Yes", fontSize = 30.sp)
						}

						Button(onClick = {
							youSurOfChange = false
							changeTeam = true
						}) {
							Text(text = "No", fontSize = 30.sp)
						}
					}
				}
			}
		}
	}
}
