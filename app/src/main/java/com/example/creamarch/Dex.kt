package com.example.creamarch

// Class that stores every species' information and
// whether the player has caught at least one creature of the species.
object Dex
{
	// Associate an ID to a species.
	var species: Map<Int, CreatureSpecies> = mapOf(
		1 to CreatureSpecies("Bleutruk", R.drawable.bleutruk_icon),
		2 to CreatureSpecies("Cylindre", R.drawable.cylindre_icon),
		666 to CreatureSpecies("Teoridukomplo", R.drawable.teoridukomplo_icon)
	)

	// Associate an ID to some informative text about the species.
	var descriptions: Map<Int, String> = mapOf(
		1 to "Lorem ipsum dolor sit amet",
		2 to "ayo",
		666 to "hej!"
	)

	// Check if all maps have the same size, i.e. the information
	// about all the species is valid.
	// todo
}