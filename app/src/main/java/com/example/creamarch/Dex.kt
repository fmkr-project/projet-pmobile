package com.example.creamarch

import android.util.Log

// Class that stores every species' information and
// whether the player has caught at least one creature of the species.
object Dex
{
	// Associate an ID to a species.
	var species: Map<Int, CreatureSpecies> = mapOf(
		1 to CreatureSpecies(
			"Bleutruk", Rarity.Uncommon, R.drawable.bleutruk_icon, BaseStats(50, 70)
		),
		2 to CreatureSpecies(
			"Cylindre", Rarity.Common, R.drawable.cylindre_icon, BaseStats(10, 55)
		),
		5 to CreatureSpecies(
			"Tronbon", Rarity.Common, R.drawable.tronbon_icon, BaseStats(60, 35)
		),
		6 to CreatureSpecies(
			"Feunaitr", Rarity.Uncommon, R.drawable.feunaitr_icon, BaseStats(80, 40)
		),
		7 to CreatureSpecies(
			"Oématlo", Rarity.Common, R.drawable.oematlo_icon, BaseStats(25, 35)
		),
		9 to CreatureSpecies(
			"Chaiziqea", Rarity.Uncommon, R.drawable.chaiziqea_icon, BaseStats(55, 40)
		),
		10 to CreatureSpecies(
			"Foteuyiqea", Rarity.Rare, R.drawable.foteuyiqea_icon, BaseStats(90, 60)
		),
		11 to CreatureSpecies(
			"Kanapay", Rarity.Epic, R.drawable.kanapay_icon, BaseStats(120, 85)
		),
		666 to CreatureSpecies(
			"Teoridukomplo", Rarity.Legendary, R.drawable.teoridukomplo_icon, BaseStats(1000, 100)
		)
	)

	// Associate an ID to some informative text about the species.
	var descriptions: Map<Int, String> = mapOf(
		1 to "Lorem ipsum dolor sit amet",
		2 to "ayo",
		5 to "TODO",
		6 to "d,eiofrjiojerioerjogir",
		7 to "TODO",
		9 to "TODO",
		10 to "TODO",
		11 to "TODO",
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