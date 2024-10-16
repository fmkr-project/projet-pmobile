package com.example.creamarch

// Class that stores every species' information and
// whether the player has caught at least one creature of the species.
object Dex
{
	// Associate an ID to a species.
	var species: Map<Int, CreatureSpecies> = mapOf(
		1 to CreatureSpecies("Bleutruk")
	)

	// Associate an ID to some informative text about the species.
	var descriptions: Map<Int, String> = mapOf(
		1 to "Lorem ipsum dolor sit amet"
	)

	// Check if all maps have the same size, i.e. the information
	// about all the species is valid.
	// todo
}