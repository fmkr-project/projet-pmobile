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

var playerTeam: MutableList<Creature> = mutableListOf()

fun initializePlayerTeam(context: Context) {
	playerTeam = loadPlayerTeam(context)
}

// Appeler cette fonction pour sauvegarder l'état avant de quitter l'application
fun savePlayerTeamState(context: Context) {
	savePlayerTeam(context)
}

fun savePlayerTeam(context: Context) {
	val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
	val editor = sharedPreferences.edit()

	val gson = Gson()
	val json = gson.toJson(playerTeam) // Sérialiser la liste en JSON
	editor.putString("player_team", json)
	editor.apply()
}

fun loadPlayerTeam(context: Context): MutableList<Creature> {
	val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
	val json = sharedPreferences.getString("player_team", null)

	if (json != null)
	{
		val type = object : TypeToken<MutableList<Creature>>() {}.type
		return Gson().fromJson(json, type) // Désérialiser la liste
	}
	else
	{
		PlayerDex.catch(1)
		return (1..1).map { Dex.species[1]!!.spawnNewCreature(10) }.toMutableList()
	}
}

fun deadTeam(): Boolean{
	val i = playerTeam.fold(true)
	{ acc, creature -> acc && (creature.stats.currentHp <= 0) }
	return i
}

fun clickPower(): Int{
	return (playerTeam.fold(0) { acc, creature ->
		if (creature.stats.currentHp > 0) creature.stats.attack + acc
		else acc
	} * (85 .. 115).random() / 100f).toInt()
}

fun addCreatureToTeam(creature: Creature) {
	// Si la taille de playerTeam est 6, on supprime la derniere créature
	if (playerTeam.size >= 6) {
		playerTeam.removeAt(5)  // Retire le dernier élément (indice 5)
	}
	creature.stats.currentHp = creature.stats.maxHp

	// Ajoute la créature en première position
	playerTeam.add(0, creature)  // Insere la créature à l'index 0 (en debut de liste)
}

fun addCreatureToTeam(creature: Creature, index: Int){
	playerTeam.removeAt(index)
	creature.stats.currentHp = creature.stats.maxHp
	playerTeam.add(index, creature)
}

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
			.fillMaxWidth()
			.padding(10.dp)
		)
		{
			Image(
			painter = painterResource(creature.baseData.menuSprite),
			contentDescription = creature.baseData.name,
			modifier = modifier
				.fillMaxHeight()
				.size(36.dp)
			)

			Column(
				modifier = modifier.weight(1f)
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
				text = "$pv / $maxPV",
				modifier = modifier
					.wrapContentHeight()
			)
		}
	}
}

@Composable
fun TeamMenu(modifier: Modifier = Modifier)
{
	// Get the player's team.
	// TODO temp
	// TODO ensure there are no more than 6 creatures in the team
	for (creature in playerTeam)
		if (creature.stats.currentHp < 0)
			creature.stats.currentHp = 0

	LazyColumn (modifier = modifier) {
		items(playerTeam) {
			TeamMember(
				creature = it,
				pv = it.stats.currentHp,
				maxPV = it.stats.maxHp,
				modifier = Modifier.padding(10.dp))
		}
	}
}

@Composable
@Preview(showBackground = true)
fun TeamPreview()
{
	CreamarchTheme {
		TeamMenu()
	}
}