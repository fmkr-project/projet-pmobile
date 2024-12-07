package com.example.creamarch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random


@Composable
fun CollectionItem(
	id: Int,
	species: CreatureSpecies,
	menuStatus: MenuStatus,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
)
{
	val isSeen = PlayerDex.isSeen(id)
	val isCaught = PlayerDex.isCaught(id)

	val painter: Painter = if (isSeen) painterResource(species.menuSprite)
	else painterResource(R.drawable.what)

	Card(
		onClick =
		{
			if (isSeen)
			{
				onClick()
				menuStatus.openCollectionPopup()
				menuStatus.collectionPopupSpecies = species
			}
		},
		modifier = modifier
			.padding(5.dp)
	)
	{
		Column(
			modifier = modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally)
		{
			Image(
				painter = painter,
				contentDescription = null,
				modifier = modifier
					.size(60.dp)
			)
			Row(
				modifier = modifier,
				verticalAlignment = Alignment.CenterVertically
			)
			{
				if (isCaught)
				{
					Image(
						painter = painterResource(R.drawable.mon_caught),
						contentDescription = null,
						modifier = modifier
							.size(16.dp)
					)
				}
				Text(text = "#" + id.toString().padStart(3, '0'))
			}
		}
	}
}

@Composable
fun CollectionMenu(
	modifier: Modifier = Modifier,
	menuStatus: MenuStatus,
	playerDex: PlayerDex
)
{
	var isCardOpen: Boolean by remember { mutableStateOf(menuStatus.collectionPopupIsOpen) }
	
	Box(modifier = modifier)
	{
		LazyVerticalGrid(
			columns = GridCells.Fixed(4),
			modifier = modifier
		)
		{
			items(Dex.species.entries.toList())
			{ item ->
				CollectionItem(
					id = item.key,
					species = item.value,
					onClick = { isCardOpen = true },
					menuStatus = menuStatus
				)
			}
		}

		Log.d("d", isCardOpen.toString())
		// card
		if (isCardOpen) {
			AlertDialog(
				onDismissRequest = {},
				title =
				{
					Text(
						text = menuStatus.collectionPopupSpecies.name.toUpperCase(Locale.current),
						fontSize = 32.sp,
						fontWeight = FontWeight.Bold
					)
				},
				text =
				{
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.fillMaxHeight(0.5f)
							.padding(20.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center
					)
					{
						Image(
							painter = painterResource(menuStatus.collectionPopupSpecies.menuSprite),
							contentDescription = null,
							modifier = Modifier
								.size(96.dp)
						)
						Spacer(
							modifier = Modifier
								.size(15.dp)
						)
						Text(
							text = "Rencontré : " + PlayerDex.getSeen(
								Dex.getSpeciesId(
									menuStatus.collectionPopupSpecies
								)
							),
							fontStyle = FontStyle.Italic,
							fontSize = 20.sp
						)
						Text(
							text = "Capturé : " + PlayerDex.getCaught(
								Dex.getSpeciesId(
									menuStatus.collectionPopupSpecies
								)
							),
							fontStyle = FontStyle.Italic,
							fontSize = 20.sp
						)
						Spacer(
							modifier = Modifier
								.size(12.dp)
						)
						Text(
							text = "PV " + menuStatus.collectionPopupSpecies.baseStats.maxHp
									+ " ATK " + menuStatus.collectionPopupSpecies.baseStats.attack,
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold
						)
						Spacer(
							modifier = Modifier
								.size(28.dp)
						)
						Text(
							text = if (PlayerDex.isCaught(Dex.getSpeciesId(menuStatus.collectionPopupSpecies)))
								Dex.descriptions[Dex.getSpeciesId(menuStatus.collectionPopupSpecies)]!!
							else "??? ? ???? ? ????? ????????? ?? ??????",
							fontSize = 18.sp,
							textAlign = TextAlign.Center
						)
						Spacer(
							modifier = Modifier
								.size(8.dp)
								.weight(1f)
						)
						OutlinedIconButton(
							onClick = {
								isCardOpen = false
								menuStatus.collectionPopupIsOpen = false
									  },
							modifier = Modifier
								.size(52.dp)
						)
						{
							Icon(Icons.Default.Close, contentDescription = null)
						}
					}
				},
				confirmButton = {}
			)
		}
	}
}

@Preview
@Composable
fun CollectionPreview()
{
	CollectionMenu(
		menuStatus = MenuStatus(collectionPopupIsOpen = true),
		playerDex = PlayerDex
	)
}