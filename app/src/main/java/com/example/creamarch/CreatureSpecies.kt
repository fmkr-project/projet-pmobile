package com.example.creamarch

import com.google.gson.annotations.SerializedName
import kotlin.math.floor
import kotlin.random.Random

enum class Rarity
{
	Common,
	Uncommon,
	Rare,
	Epic,
	Legendary
}

// Class to store base creature data.
data class CreatureSpecies(
	@SerializedName("name") val name: String,
	@SerializedName("rarity") val rarity: Rarity,
	@SerializedName("sprite") val menuSprite: Int,
	@SerializedName("baseStats") val baseStats: BaseStats
)
{
	fun spawnNewCreature(level: Int): Creature
	// Return a new creature of this type with randomized stats.
	{
		val iv: Iv = Iv((0..31).random(), (0..31).random())
		// TODO also randomize level & stats
		return Creature(
			this,
			level,
			generateStats(level, iv),
			iv
		)
	}

	private fun generateStats(level: Int, iv: Iv): TeamStats
	// Return a set of stats of this creature.
	{
		// Use Pokemon gen III+ formula.
		return TeamStats(
			floor((2 * baseStats.maxHp.toDouble() + iv.hpIv) * level / 100).toInt() + level + 10,
			floor((2 * baseStats.maxHp.toDouble() + iv.hpIv) * level / 100).toInt() + level + 10,
			floor((2 * baseStats.attack.toDouble() + iv.attackIv) * level / 100).toInt()
		)
	}
}