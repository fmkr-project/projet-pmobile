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

// Composable function for the player menu that displays the player's statistics
@Composable
fun PlayerMenu(
	modifier: Modifier = Modifier
)
{
	// Column to arrange the text elements vertically
	Column(
		modifier = modifier
			.fillMaxSize()  // Fill the available screen space
			.padding(32.dp)  // Add padding around the content
	)
	{
		// Title for the statistics section
		Text(
			text = "Statistics",  // Display the text for the title
			modifier = Modifier.align(Alignment.CenterHorizontally),  // Center the title horizontally
			fontWeight = FontWeight.Bold,  // Make the title bold
			fontSize = 32.sp  // Set the font size
		)
		Spacer(Modifier.size(16.dp))  // Spacer to add space between elements

		// Text for the "Total creatures encountered" label
		Text(
			text = "Total creatures encountered:",
			modifier = Modifier.align(Alignment.CenterHorizontally),  // Center the label horizontally
			fontStyle = FontStyle.Italic,  // Set the text style to italic
			fontSize = 24.sp  // Set the font size
		)
		Spacer(Modifier.size(8.dp))  // Spacer to add space between label and value

		// Text displaying the total number of creatures encountered
		Text(
			text = PlayerDex.getTotalSeen().toString(),  // Fetch the total creatures encountered from PlayerDex
			modifier = Modifier.align(Alignment.CenterHorizontally),  // Center the text horizontally
			fontSize = 48.sp  // Set the font size
		)
		Spacer(Modifier.size(16.dp))  // Spacer to add space between sections

		// Text for the "Total creatures caught" label
		Text(
			text = "Total creatures caught:",
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontStyle = FontStyle.Italic,  // Set the text style to italic
			fontSize = 24.sp  // Set the font size
		)
		Spacer(Modifier.size(8.dp))  // Spacer to add space between label and value

		// Text displaying the total number of creatures caught
		Text(
			text = PlayerDex.getTotalCaught().toString(),  // Fetch the total creatures caught from PlayerDex
			modifier = Modifier.align(Alignment.CenterHorizontally),  // Center the text horizontally
			fontSize = 48.sp  // Set the font size
		)
		Spacer(Modifier.size(16.dp))  // Spacer to add space between sections

		// Text for the "Number of species caught" label
		Text(
			text = "Number of species caught:",
			modifier = Modifier.align(Alignment.CenterHorizontally),
			fontStyle = FontStyle.Italic,  // Set the text style to italic
			fontSize = 24.sp  // Set the font size
		)
		Spacer(Modifier.size(8.dp))  // Spacer to add space between label and value

		// Text displaying the number of species caught
		Text(
			text = PlayerDex.getSpeciesCaught().toString(),  // Fetch the number of species caught from PlayerDex
			modifier = Modifier.align(Alignment.CenterHorizontally),  // Center the text horizontally
			fontSize = 48.sp  // Set the font size
		)
	}
}

// Preview function for the PlayerMenu
@Composable
@Preview
fun PlayerMenuPreview() { CreamarchTheme { PlayerMenu() } }
