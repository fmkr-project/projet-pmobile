package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TeamMember(
	creature: Creature,
	modifier: Modifier
)
{
	Card(
		modifier = modifier
			.padding(10.dp),
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
				modifier = modifier
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
		}
	}
}

@Composable
fun TeamMenu(modifier: Modifier = Modifier)
{
	// Get the player's team.
	// TODO temp
	// TODO ensure there are no more than 6 creatures in the team
	var playerTeam = (1..6).map { Dex.species.values.random().spawnNewCreature() }

	LazyColumn(
		modifier = modifier
	) {
		items(playerTeam) {
			TeamMember(
				creature = it,
				modifier = modifier
			)
		}
	}
}