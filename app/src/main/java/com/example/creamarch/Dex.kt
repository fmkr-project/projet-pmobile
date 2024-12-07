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
		3 to CreatureSpecies(
			"Deuxcylindres", Rarity.Uncommon, R.drawable.deuxcylindres_icon, BaseStats(25, 60)
		),
		4 to CreatureSpecies(
			"Troicylindres", Rarity.Rare, R.drawable.troicylindres_icon, BaseStats(35, 80)
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
		8 to CreatureSpecies(
			"Bouledkanon", Rarity.Uncommon, R.drawable.bouledkanon_icon, BaseStats(90, 15)
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
		12 to CreatureSpecies(
			"Tabliquea", Rarity.Rare, R.drawable.tabliquea_icon, BaseStats(100, 50)
		),
		13 to CreatureSpecies(
			"Komodiquea", Rarity.Epic, R.drawable.komodiquea_icon, BaseStats(140, 70)
		),
		14 to CreatureSpecies(
			"Chtiblob", Rarity.Common, R.drawable.chtiblob_icon, BaseStats(20, 40)
		),
		15 to CreatureSpecies(
			"Blob", Rarity.Uncommon, R.drawable.blob_icon, BaseStats(40, 60)
		),
		16 to CreatureSpecies(
			"Deublob", Rarity.Rare, R.drawable.deublob_icon, BaseStats(60, 90)
		),
		17 to CreatureSpecies(
			"Barmétalik", Rarity.Common, R.drawable.barmetalik_icon, BaseStats(80, 5)
		),
		18 to CreatureSpecies(
			"Sus", Rarity.Common, R.drawable.sus_icon, BaseStats(25, 25)
		),
		19 to CreatureSpecies(
			"Amogus", Rarity.Uncommon, R.drawable.amogus_icon, BaseStats(50, 50)
		),
		20 to CreatureSpecies(
			"Bahton", Rarity.Common, R.drawable.bahton_icon, BaseStats(30, 40)
		),
		21 to CreatureSpecies(
			"Igrek", Rarity.Uncommon, R.drawable.igrek_icon, BaseStats(45, 65)
		),
		22 to CreatureSpecies(
			"Kaïou", Rarity.Uncommon, R.drawable.kaiou_icon, BaseStats(100, 10)
		),
		23 to CreatureSpecies(
			"Grokaïou", Rarity.Rare, R.drawable.grokaiou_icon, BaseStats(150, 15)
		),
		24 to CreatureSpecies(
			"Piikseyl", Rarity.Rare, R.drawable.piikseyl_icon, BaseStats(75, 75)
		),
		25 to CreatureSpecies(
			"Pylo", Rarity.Uncommon, R.drawable.pylo_icon, BaseStats(50, 75)
		),
		26 to CreatureSpecies(
			"Pikaglace", Rarity.Rare, R.drawable.pikaglace_icon, BaseStats(55, 95)
		),
		27 to CreatureSpecies(
			"Pahkhmhahn", Rarity.Epic, R.drawable.pahkhmhahn_icon, BaseStats(60, 120)
		),
		28 to CreatureSpecies(
			"Acyeth(b)", Rarity.Common, R.drawable.acyeth_icon, BaseStats(45, 45)
		),
		29 to CreatureSpecies(
			"Acyeth(n)", Rarity.Common, R.drawable.acyeth2_icon, BaseStats(45, 45)
		),
		30 to CreatureSpecies(
			"Baule(b)", Rarity.Rare, R.drawable.baule_icon, BaseStats(80, 80)
		),
		31 to CreatureSpecies(
			"Baule(n)", Rarity.Rare, R.drawable.baule2_icon, BaseStats(80, 80)
		),
		493 to CreatureSpecies(
			"Caulanta", Rarity.Legendary, R.drawable.caulanta_icon, BaseStats(200, 300)
		),
		514 to CreatureSpecies(
			"Deux", Rarity.Legendary, R.drawable.deux_icon, BaseStats(150, 400)
		),
		666 to CreatureSpecies(
			"Teoridukomplo", Rarity.Legendary, R.drawable.teoridukomplo_icon, BaseStats(350, 700)
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