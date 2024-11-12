package com.example.creamarch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.creamarch.ui.theme.CreamarchTheme

var playerTeam = (1..6).map { Dex.species.values.random().spawnNewCreature() }

@Composable
fun TeamMember(
	creature: Creature,
	pv: Int,
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
				text = "$pv / $pv",
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


	LazyColumn (modifier = modifier) {
		items(playerTeam) {
			TeamMember(
				creature = it,
				pv = 100,
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