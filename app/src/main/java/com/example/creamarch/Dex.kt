package com.example.creamarch

import android.util.Log

// Class that stores every species' information and
// whether the player has caught at least one creature of the species.
object Dex
{
	// Associate an ID to a species.
	var species: Map<Int, CreatureSpecies> = mapOf(
		1 to CreatureSpecies(
			"Bleutruk", R.drawable.bleutruk_icon, BaseStats(100, 5)
		),
		2 to CreatureSpecies(
			"Cylindre", R.drawable.cylindre_icon, BaseStats(35, 8)
		),
		5 to CreatureSpecies(
			"Tronbon", R.drawable.tronbon_icon, BaseStats(175, 3)
		),
		6 to CreatureSpecies(
			"Feunaitr", R.drawable.feunaitr_icon, BaseStats(330, 10)
		),
		666 to CreatureSpecies(
			"Teoridukomplo", R.drawable.teoridukomplo_icon, BaseStats(1000, 50)
		)
	)

	// Associate an ID to some informative text about the species.
	var descriptions: Map<Int, String> = mapOf(
		1 to "Lorem ipsum dolor sit amet",
		2 to "ayo",
		5 to "TODO",
		6 to "d,eiofrjiojerioerjogir",
		666 to "hej!"
	)

	// Check if all maps have the same size, i.e. the information
	// about all the species is valid.
	// todo

	fun getSpeciesByName(speciesName: String): CreatureSpecies
	{
		for (pair in species)
		{
			if (pair.value.name === speciesName) return pair.value
		}
		Log.e("ono", "ono")
		throw IllegalArgumentException("Species name unknown!")
	}

	fun getSpeciesId(species: CreatureSpecies): Int
	{
		for (pair in this.species)
		{
			if (pair.value == species) return pair.key
		}
		Log.e("ono", "ono")
		throw IllegalArgumentException("This species does not exist in the dex!")
	}
}