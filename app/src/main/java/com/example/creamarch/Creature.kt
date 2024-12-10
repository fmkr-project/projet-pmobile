package com.example.creamarch

import com.google.gson.annotations.SerializedName

// Class to store player's creatures data.

// Class representing a creature with its base data, level, stats, and IVs (Individual Values)
class Creature(
	@SerializedName("baseData") val baseData: CreatureSpecies,  // Base data of the creature (name, type, etc.)
	@SerializedName("level") val level: Int,  // Level of the creature
	@SerializedName("stats") val stats: TeamStats,  // Current stats of the creature (HP, attack, etc.)
	@SerializedName("iv") val iv: Iv  // Individual values (IVs) for the creature's stats
)
{
	// TODO: Generate stats depending on level and IVs.
}
