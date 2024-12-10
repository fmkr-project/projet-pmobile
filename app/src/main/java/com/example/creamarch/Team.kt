package com.example.creamarch

import android.content.Context
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.example.creamarch.ui.theme.CreamarchTheme
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlin.math.roundToInt

// List of creatures in the player's team
var playerTeam: MutableList<Creature> = mutableListOf()

// Function to initialize the player's team from saved data or defaults
fun initializePlayerTeam(context: Context) {
	playerTeam = loadPlayerTeam(context)  // Load the player's team
	playerTeam.map { it.stats.initializeCurrentHpState() }  // Initialize health states
}

// Call this function to save the player's team state before exiting the app
fun savePlayerTeamState(context: Context) {
	savePlayerTeam(context)  // Save the team data
}

// Function to save the player's team to SharedPreferences
fun savePlayerTeam(context: Context) {
	playerTeam.map { it.stats.copy(currentHp = it.stats.currentHpState.intValue) }  // Save current HP
	val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
	val editor = sharedPreferences.edit()

	val gson = Gson()
	val json = gson.toJson(playerTeam)  // Serialize the player team list to JSON
	editor.putString("player_team", json)
	editor.apply()  // Apply changes to SharedPreferences
}

// Function to load the player's team from SharedPreferences
fun loadPlayerTeam(context: Context): MutableList<Creature> {
	val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
	val json = sharedPreferences.getString("player_team", null)

	if (json != null) {
		val type = object : TypeToken<MutableList<Creature>>() {}.type
		val creatures = Gson().fromJson<MutableList<Creature>>(json, type)  // Deserialize the JSON to a list of creatures
		creatures.forEach { it.stats.initializeCurrentHpState() }  // Initialize health states
		return creatures
	} else {
		// If no saved data is found, return a default creature
		return (1..1).map { Dex.species[1]!!.spawnNewCreature(10) }.toMutableList()
	}
}

// Function to check if all creatures in the team are dead (HP <= 0)
fun deadTeam(): Boolean{
	val i = playerTeam.fold(true)
	{ acc, creature -> acc && (creature.stats.currentHpState.intValue <= 0) }  // Check if all creatures are dead
	return i
}

// Function to calculate the attack power based on the team's health and stats
fun clickPower(): Int{
	return (playerTeam.fold(0) { acc, creature ->
		if (creature.stats.currentHpState.intValue > 0) creature.stats.attack + acc
		else acc
	} * (85 .. 115).random() / 100f).toInt()  // Calculate attack with random variation
}

// Function to add a creature to the player's team
fun addCreatureToTeam(creature: Creature) {
	// If the team size exceeds 6, remove the last creature
	if (playerTeam.size >= 6) {
		playerTeam.removeAt(5)  // Remove the last element (index 5)
	}
	creature.stats.currentHpState.intValue = creature.stats.maxHp  // Reset the creature's HP

	// Add the creature to the front of the team list
	playerTeam.add(0, creature)  // Insert the creature at index 0 (beginning of the list)
}

// Function to add a creature to the player's team at a specific index
fun addCreatureToTeam(creature: Creature, index: Int){
	playerTeam.removeAt(index)  // Remove the creature at the specified index
	creature.stats.currentHpState.intValue = creature.stats.maxHp  // Reset the creature's HP
	playerTeam.add(index, creature)  // Add the creature back at the specified index
}

// Composable function to display a team member (creature) with its stats
@Composable
fun TeamMember(
	creature: Creature,
	pv: Int,
	maxPV: Int,
	modifier: Modifier
)
{
	Card(
		modifier = modifier,
		onClick = {}
	)
	{
		Row(modifier = Modifier
			.fillMaxWidth()  // Make the row fill the available width
			.padding(10.dp)  // Add padding inside the row
		)
		{
			Image(
				painter = painterResource(creature.baseData.menuSprite),  // Display the creature's sprite
				contentDescription = creature.baseData.name,  // Use the creature's name as the description
				modifier = modifier
					.fillMaxHeight()  // Make the image fill the available height
					.size(36.dp)  // Set the size of the image
			)

			Column(
				modifier = modifier.weight(1f)  // Make the column take up available space
			) {
				Text(
					text = creature.baseData.name,  // Display the creature's name
					fontWeight = FontWeight.Bold,  // Make the name bold
					fontSize = 28.sp  // Set the font size
				)
				Text(
					text = "lvl. " + creature.level.toString()  // Display the creature's level
				)
			}
			Text(
				text = "$pv / $maxPV",  // Display current HP / max HP
				modifier = modifier
					.wrapContentHeight()  // Make the text wrap its content vertically
			)
		}
	}
}

// Composable function to display the player's team
@Composable
fun TeamMenu(modifier: Modifier = Modifier)
{
	// Get the player's team.
	// TODO: Ensure there are no more than 6 creatures in the team
	for (creature in playerTeam) {
		LazyColumn(modifier = modifier) {
			items(playerTeam) {
				TeamMember(
					creature = it,
					pv = it.stats.currentHpState.intValue.coerceAtLeast(0),  // Ensure HP is at least 0
					maxPV = it.stats.maxHp,  // Pass the maximum HP
					modifier = Modifier.padding(10.dp)  // Add padding around each team member
				)
			}
		}
	}
}

// Preview of the TeamMenu composable for development purposes
@Composable
@Preview(showBackground = true)
fun TeamPreview()
{
	CreamarchTheme {
		TeamMenu()  // Display the TeamMenu composable within the theme
	}
}
