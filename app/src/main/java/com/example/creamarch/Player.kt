package com.example.creamarch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creamarch.ui.theme.CreamarchTheme

@Composable
fun PlayerMenu(
	modifier: Modifier = Modifier
)
{
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(32.dp)
	)
	{
		Text(
			text = "Statistiques",
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontWeight = FontWeight.Bold,
			fontSize = 32.sp
		)
		Spacer(Modifier.size(16.dp))
		Text(
			text = "Total créatures capturées :",
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontStyle = FontStyle.Italic,
			fontSize = 24.sp
		)
		Spacer(Modifier.size(8.dp))
		Text(
			text = PlayerDex.getTotalCaught().toString(),
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontSize = 48.sp
		)
		Spacer(Modifier.size(16.dp))
		Text(
			text = "Nombre d'espèces capturées :",
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontStyle = FontStyle.Italic,
			fontSize = 24.sp
		)
		Spacer(Modifier.size(8.dp))
		Text(
			text = PlayerDex.getSpeciesCaught().toString(),
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontSize = 48.sp
		)
	}
}

@Composable
@Preview
fun PlayerMenuPreview() { CreamarchTheme { PlayerMenu() } }