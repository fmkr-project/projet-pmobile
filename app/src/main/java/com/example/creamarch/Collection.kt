package com.example.creamarch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random


@Composable
fun CollectionItem(
	id: Int,
	species: CreatureSpecies,
	isCaught: Boolean,
	menuStatus: MenuStatus,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
)
{
	val painter: Painter = if (isCaught) painterResource(species.menuSprite)
	else painterResource(R.drawable.what)

	Card(
		onClick = { if (isCaught)
		{
			onClick()
			menuStatus.openCollectionPopup()
			menuStatus.collectionPopupSpecies = species
		}
				  },
		modifier = modifier
			.padding(5.dp))
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
			Text(text = "#" + id.toString().padStart(3, '0'))
		}
	}
}

@Composable
fun CollectionMenu(
	modifier: Modifier = Modifier,
	menuStatus: MenuStatus
)
{
	var isCardOpen: Boolean by remember { mutableStateOf(menuStatus.collectionPopupIsOpen) }

	key(isCardOpen)
	{
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
						isCaught = Random.nextBoolean(),
						onClick = { isCardOpen = true },
						menuStatus = menuStatus
					)
				}
			}

			// card
			if (isCardOpen) {
				Card(
					modifier = modifier
						.padding(80.dp)
						.fillMaxSize()
				)
				{
					Column(
						modifier = modifier
							.fillMaxSize(),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Bottom
					)
					{
						Image(
							painter = painterResource(menuStatus.collectionPopupSpecies.menuSprite),
							contentDescription = null,
							modifier = modifier
								.size(69.dp)
						)
						Text(
							text = "felkfejlkfeljkfejlkefkjl",//Dex.descriptions[Dex.getSpeciesId(menuStatus.collectionPopupSpecies)]!!,
							fontWeight = FontWeight.Bold,
							fontSize = 12.sp,
							modifier = modifier
						)
						OutlinedIconButton(
							onClick = { isCardOpen = false },
							modifier = modifier
						)
						{
							Icon(Icons.Default.Close, contentDescription = null)
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun CollectionPreview()
{
	CollectionMenu(menuStatus = MenuStatus(collectionPopupIsOpen = true))
}