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
		1 to "Petit mais puissant, c'est le premier fidèle compagnon de nombreux jeunes Éleveurs.",
		2 to "Il se déplace dans un vaste réseau de galeries souterraines creusées par les autres CYLINDRE.",
		3 to "Deux CYLINDRE qui se sont liés d'amitié. Leurs secousses synchronisées sont redoutables.",
		4 to "Trois CYLINDRE peuvent produire des secousses sismiques pouvant faire s'écrouler de petites maisons.",
		5 to "Cette Créature vaniteuse s'introduit souvent dans les conversations des humains pour leur proposer son aide.",
		6 to "Cette Créature est très refermée sur elle-même. Beaucoup d'Éleveurs lui préfèrent son cousin, PAINGWIN.",
		7 to "Cette Créature fantomatique peut être aperçue près des épaves d'anciens navires.",
		8 to "Il se déplace en roulant. Pendant l'hiver, des nuées de BOULEDKANON forment de longues traînées dans la neige.",
		9 to "Lorsqu'un humain s'assoit dessus, il prend plaisir à le projeter violemment au sol.",
		10 to "La peau de cette Créature s'écaille à mesure qu'il vieillit.",
		11 to "Cette Créature mange très peu. Il passe souvent ses journées à regarder la télévision des humains.",
		12 to "Même âgé, il dégage de riches senteurs rappelant les pays nordiques.",
		13 to "Son jeu préféré est de recracher par la fenêtre les nombreux objets que les humains rangent dans ses tiroirs.",
		14 to "Tout juste né, il a déjà l'intelligence de plusieurs prix Nobel réunis.",
		15 to "Cette Créature très intelligente est crainte par même de puissants prédateurs.",
		16 to "Cette Créature est constituée de deux cerveaux qui peuvent effectuer de redoutables attaques bi-élémentales.",
		17 to "Il aime surprendre les humains en faisant un bruit assourdissant.",
		18 to "On dirait que cette Créature est sus.",
		19 to "Cette Créature est décidément très sus.",
		20 to "Cette Créature des bois attaque les intrus en projetant son corps à toute vitesse.",
		21 to "Très serviable, la légende raconte qu'un IGREK a aidé un enfant assoiffé dans les bois à se réhydrater.",
		22 to "Un peu stupide, son premier réflexe lorsqu'il rencontre quelque chose pour la première fois est de lui vomir dessus.",
		23 to "Il s'agit de l'alpha des troupeaux de KAÏOU. Cette Créature reste cependant très gentille envers les autres Créatures.",
		24 to "Cette Créature venant d'une autre région est très craintive et fuit les troupeaux de PAHKHMHAHN.",
		25 to "Lorsque deux PYLO se rencontrent, ils ont tendance à s'empiler. On a déjà vu des montagnes de cent voire deux cents PYLO.",
		26 to "Très têtu, il a tendance à s'accrocher à tout ce qu'il voit.",
		27 to "Cette Créature venant d'une autre région a tendance à poursuivre les PIIKSEYL solitaires.",
		28 to "Lorsqu'un ACYETH se brise, ses morceaux se recollent pour former un nouvel ACYETH.",
		29 to "Cette Créature a peur des hauteurs. Il ne peut pas monter une marche de plus de deux centimètres.",
		30 to "Il ne sort jamais les jours de pluie ou de neige, de peur de rester coincé dehors.",
		31 to "Dans une autre contrée, il est dit que les BAULE apprécient être utilisés comme de la vaisselle.",
		493 to "Pendant la journée, il est immobile. Il aime chanter de temps à autre un chant de guerre.",
		514 to "It's DEUX!!!! From the hit show TEAPOT!!!!!",
		666 to "Cette Créature est devenue célèbre grâce à un fameux humoriste : COMPLOTISTE ! HEIN ! TEORIDUKOMPLO !"
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