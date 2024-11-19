package com.example.creamarch

import android.util.Log

// Class that stores every species' information and
// whether the player has caught at least one creature of the species.
object Dex
{
	// Associate an ID to a species.
	var species: Map<Int, CreatureSpecies> = mapOf(
		1 to CreatureSpecies(
			"Bleutruk", Rarity.Uncommon, R.drawable.bleutruk_icon, BaseStats(50, 7)
		),
		2 to CreatureSpecies(
			"Cylindre", Rarity.Common, R.drawable.cylindre_icon, BaseStats(30, 8)
		),
		5 to CreatureSpecies(
			"Tronbon", Rarity.Common, R.drawable.tronbon_icon, BaseStats(60, 3)
		),
		6 to CreatureSpecies(
			"Feunaitr", Rarity.Uncommon, R.drawable.feunaitr_icon, BaseStats(80, 5)
		),
		666 to CreatureSpecies(
			"Teoridukomplo", Rarity.Legendary, R.drawable.teoridukomplo_icon, BaseStats(1000, 25)
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