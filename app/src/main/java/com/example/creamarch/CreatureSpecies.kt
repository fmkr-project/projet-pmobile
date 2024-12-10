package com.example.creamarch

import com.google.gson.annotations.SerializedName
import kotlin.math.floor
import kotlin.random.Random

// Enumeration representing the different possible rarities for a creature
enum class Rarity
{
	Common,     // Commonly found
	Uncommon,   // Slightly more frequent
	Rare,       // Rare
	Epic,       // Very rare
	Legendary   // Legendary, extremely rare
}

// Class representing the base data for a creature (name, rarity, sprite, base stats)
data class CreatureSpecies(
	@SerializedName("name") val name: String,  // The name of the creature
	@SerializedName("rarity") val rarity: Rarity,  // The rarity of the creature
	@SerializedName("sprite") val menuSprite: Int,  // Resource for the creature's sprite
	@SerializedName("baseStats") val baseStats: BaseStats  // The creature's base stats
)
{
	// Method to generate a new creature with a given level and randomized stats
	fun spawnNewCreature(level: Int): Creature
	{
		// Randomly generate the individual values (IVs) for the creature
		val iv: Iv = Iv((0..31).random(), (0..31).random())
		// TODO: Add the ability to randomize the level and adjust stats accordingly
		return Creature(
			this,
			level,
			generateStats(level, iv),  // Generate stats based on level and IVs
			iv  // Individual values
		)
	}

	// Method to generate the stats of a creature based on its level and IVs
	private fun generateStats(level: Int, iv: Iv): TeamStats
	{
		// Use the generation III+ Pokémon formula to calculate stats
		// Calculate health (HP)
		val hp = floor((2 * baseStats.maxHp.toDouble() + iv.hpIv) * level / 100).toInt() + level + 10
		// Calculate attack
		val attack = floor((2 * baseStats.attack.toDouble() + iv.attackIv) * level / 100).toInt()

		// Return the generated stats as a TeamStats object
		return TeamStats(hp, hp, attack)
	}
}
