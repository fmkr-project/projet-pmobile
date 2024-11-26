	package com.example.creamarch

	import DistanceTracker
	import android.util.Log
	import androidx.compose.foundation.Image
	import androidx.compose.foundation.background
	import androidx.compose.foundation.clickable
	import androidx.compose.foundation.layout.Arrangement
	import androidx.compose.foundation.layout.Box
	import androidx.compose.foundation.layout.Column
	import androidx.compose.foundation.layout.Row
	import androidx.compose.foundation.layout.Spacer
	import androidx.compose.foundation.layout.aspectRatio
	import androidx.compose.foundation.layout.fillMaxHeight
	import androidx.compose.foundation.layout.fillMaxSize
	import androidx.compose.foundation.layout.fillMaxWidth
	import androidx.compose.foundation.layout.height
	import androidx.compose.foundation.layout.padding
	import androidx.compose.foundation.layout.size
	import androidx.compose.foundation.layout.width
	import androidx.compose.foundation.layout.wrapContentHeight
	import androidx.compose.foundation.lazy.LazyColumn
	import androidx.compose.foundation.lazy.grid.GridCells
	import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
	import androidx.compose.foundation.lazy.grid.items
	import androidx.compose.foundation.lazy.items
	import androidx.compose.material3.AlertDialog
	import androidx.compose.material3.BasicAlertDialog
	import androidx.compose.material3.Button
	import androidx.compose.material3.Card
	import androidx.compose.material3.Text
	import androidx.compose.runtime.Composable
	import androidx.compose.runtime.LaunchedEffect
	import androidx.compose.runtime.collectAsState
	import androidx.compose.runtime.getValue
	import androidx.compose.runtime.mutableIntStateOf
	import androidx.compose.runtime.mutableStateOf
	import androidx.compose.runtime.remember
	import androidx.compose.runtime.setValue
	import androidx.compose.ui.Modifier
	import androidx.compose.ui.graphics.Color
	import androidx.compose.ui.res.painterResource
	import androidx.compose.ui.text.SpanStyle
	import androidx.compose.ui.text.buildAnnotatedString
	import androidx.compose.ui.text.font.FontStyle
	import androidx.compose.ui.text.font.FontWeight
	import androidx.compose.ui.text.style.TextAlign
	import androidx.compose.ui.text.style.TextDecoration
	import androidx.compose.ui.text.withStyle
	import androidx.compose.ui.unit.TextUnit
	import androidx.compose.ui.unit.dp
	import androidx.compose.ui.unit.sp
	import androidx.compose.ui.window.Dialog
	import com.example.creamarch.ui.theme.Pink80
	import com.example.creamarch.ui.theme.Purple80
	import com.example.creamarch.ui.theme.PurpleGrey80
	import kotlinx.coroutines.delay
	import kotlin.random.Random

	var prevFab = 0
	var fab = 1

	fun distance(): Int {
		val f3 = prevFab + fab
		prevFab = fab
		fab = f3
		val f4 = f3 * (100 - Random.nextInt(-5, 5))
		return f4/10
	}

	val legendary = Pair(
		first = Dex.species[666]!!.spawnNewCreature(50),
		second = 100)

	var tempCreature = (1..22).map {
		Pair(
			first = Dex.species.values.filter {  it != legendary.first.baseData}.random().spawnNewCreature(10), // TODO random levels
			second = distance()
		)
	}.toMutableList()

	val indexLegend = tempCreature.indexOfFirst { it.second >= legendary.second }

	var initialSubListSize = if (indexLegend != -1) indexLegend + 1 else tempCreature.size

	var legendaryAdded = false

	@Composable
	fun CreatureItem(
		creature: Creature,
		distance: Int,
		capture: Boolean,
		legendary: Boolean,
		onCapture: () -> Unit,
		modifier: Modifier = Modifier
	)
	{
		var bacMod = Modifier
			.fillMaxWidth()
			.padding(10.dp)
		if (capture) {
			bacMod = if (legendary) bacMod.background(color = Purple80)
			else bacMod.background(color = Pink80)
		}
		else
			if (legendary) bacMod = bacMod.background(color = PurpleGrey80)
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

@Composable
fun ExplorationMenu(distanceTracker: DistanceTracker,
					modifier: Modifier = Modifier,
)
{
	val debug = true

	val initialDistance = distanceTracker.loadTotalDistance().toInt()
	val dist by StepCounterService.distanceWalked.collectAsState(initial = 0.0)
	//val walkedDistance by distanceTracker.distance.collectAsState(initial = initialDistance)
	val walkedDistance = if (debug)
	{
		//val walkedDistance
		2000
	}
	else
	{
		dist.toInt()
	}

	//val walkedDistance = dist.toInt()

	//Log.d("initial distance", initialDistance.toString())

	// Get the list of nearby creatures
	// todo temp

	if (indexLegend != -1 && tempCreature[indexLegend] != legendary && !legendaryAdded) {
		tempCreature.add(indexLegend, legendary)
		legendaryAdded = true
	}


	val nearbyCreatures = remember {
		tempCreature
	}

	var nCreatures by remember {
		mutableStateOf(nearbyCreatures.take(initialSubListSize).toList())
	}

	var showDialog by remember { mutableStateOf(false) }
	var capturedCreature by remember { mutableStateOf<Pair<Creature, Int>?>(null) }

	fun captureCreature(creature: Pair<Creature, Int>) {
		capturedCreature = creature
		showDialog = true
	}

	var changeTeam by remember {
		mutableStateOf(false)
	}

	var youSurOfChange by remember {
		mutableStateOf(false)
	}

	var whichChange by remember {
		mutableIntStateOf(0)
	}

	// Code de la pop-up de combat
	if (showDialog && capturedCreature != null) {

		if (deadTeam()) showDialog = false
		AlertDialog(
			onDismissRequest = {  },
			title = { Text("Combat!",
				fontSize = 30.sp,
				textAlign = TextAlign.Center) },
			text = {
				Column(verticalArrangement = Arrangement.SpaceEvenly,
					modifier = Modifier.fillMaxHeight()) {
					HealthBar(
						currentHealth = capturedCreature!!.first.stats.currentHp,
						maxHealth = capturedCreature!!.first.stats.maxHp
					)
					Image(
						painter = painterResource(id = capturedCreature!!.first.baseData.menuSprite),
						contentDescription = "Créature à battre",
						modifier = Modifier
							.fillMaxWidth()
							.aspectRatio(1.5f)
							.clickable {
								capturedCreature!!.first.stats.currentHp -= clickPower()
								if (!deadTeam()) {
									var randTeam = Random.nextInt(playerTeam.size)
									while (playerTeam[randTeam].stats.currentHp <= 0)
										randTeam = Random.nextInt(playerTeam.size)
									playerTeam[randTeam].stats.currentHp -= capturedCreature!!.first.stats.attack
									if (deadTeam()) PlayerDex.see(Dex.getSpeciesId(capturedCreature!!.first.baseData))
								}
								else{
									if (capturedCreature!!.first.stats.currentHp <= 0) {
										PlayerDex.catch(Dex.getSpeciesId(capturedCreature!!.first.baseData))
										showDialog = false
										tempCreature.remove(capturedCreature!!)
										nCreatures = nearbyCreatures
											.take(initialSubListSize)
											.toList()
										if (playerTeam.size < 6) addCreatureToTeam(capturedCreature!!.first)
										else changeTeam = true
									}
								}
							}
					)

					//Spacer(modifier = Modifier.height(4.dp))

					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						modifier = Modifier
							.fillMaxWidth()
							.weight(1f)
						,
						verticalArrangement = Arrangement.SpaceEvenly,
						horizontalArrangement = Arrangement.SpaceEvenly
					)
					{
						items(playerTeam) {
							var imageMod = Modifier
								.fillMaxWidth()
								.aspectRatio(1f)
							if (it.stats.currentHp <= 0) imageMod = imageMod.background(color = Color.Red)
							if (it != null){
								Column(modifier = Modifier.padding(4.dp)) {
									Image(painter = painterResource(id = it.baseData.menuSprite),
										contentDescription = "My creatures",
										modifier = imageMod
									)
									HealthBar(
										currentHealth = it.stats.currentHp,
										maxHealth = it.stats.maxHp,
										modifier = Modifier.padding(4.dp)
									)
								}
							}
						}
					}

					//Spacer(modifier = Modifier.height(4.dp))
				}
			},
			confirmButton = {
				Button(onClick = {
					PlayerDex.see(Dex.getSpeciesId(capturedCreature!!.first.baseData))
					showDialog = false
				},
					modifier = Modifier.fillMaxWidth()
				) {
					Text("Fuite",
						fontSize = 30.sp,
						textAlign = TextAlign.Center)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.8f)
		)
	}
	//Code du choix de la creature a garder

	if (changeTeam){
		AlertDialog(
			modifier = Modifier
				.fillMaxSize(),
			onDismissRequest = {  },
			title = { Text(text = buildAnnotatedString {
				append("Quelle créature voulez-vous remplacer par ")
				withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
					append(capturedCreature!!.first.baseData.name)
					append(" niv ")
					append(capturedCreature!!.first.level.toString())
				}
				append("?")
			},
				fontSize = 18.sp)},
			text = {
				LazyColumn {
					items(playerTeam){
						TeamMember(
							creature = it,
							pv = it.stats.currentHp,
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
				Button(onClick = { changeTeam = false}) 
				{
					Text(text = "Aucune")
				}
			}


		)
	}

	// Popup pour s'assurer du changement de créature
	if (youSurOfChange){
		Dialog(onDismissRequest = {  })
			{
				Column ( modifier = Modifier
					.fillMaxHeight(0.4f)
					.fillMaxWidth()
					.background(Color.White),
					verticalArrangement = Arrangement.SpaceEvenly)

				{
					Text(text = "Etes-vous sûr de votre choix?",
						fontSize = 52.sp,
						lineHeight = 52.sp
					)
					Row(modifier = Modifier.fillMaxWidth()
						,horizontalArrangement = Arrangement.SpaceEvenly) {
						Button(onClick = {
							addCreatureToTeam(
								capturedCreature!!.first,
								whichChange
							)
							changeTeam = false
							youSurOfChange = false
						}
							//,modifier = Modifier.fillMaxSize(0.25f)
						)
						{
							Text(text = "Oui",
								fontSize = 30.sp)
						}
						
						Button(onClick = {
							youSurOfChange = false
							changeTeam = true
						}
							//,modifier = Modifier.fillMaxSize(0.25f)
						)
						{
							Text(text = "Non",
								fontSize = 30.sp)
						}
					}
				}
			}

	}


	// Code de la page de base
	var nextIndex by remember {
		mutableIntStateOf(0)
	}
	var tillNext by remember {
		mutableIntStateOf(nCreatures[nextIndex].second - walkedDistance)
	}
	nextIndex = nearbyCreatures.indexOfFirst { it.second >= walkedDistance }
	tillNext = if (nextIndex != -1) nearbyCreatures[nextIndex].second - walkedDistance else 0
	// Compose

	Column(modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(0.dp) )
	{
		Text(
			text = "Vous avez parcouru $walkedDistance m!",
			fontSize = 30.sp,
			modifier = modifier
		)
		Text(
			text = "Créature suivante dans $tillNext m!",
			fontSize = 30.sp,
			modifier = Modifier.padding(bottom = 5.dp)
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
						legendary = (it.first.baseData == legendary.first.baseData),
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