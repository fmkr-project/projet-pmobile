package com.example.creamarch

import com.google.gson.annotations.SerializedName

// Class to store player's creatures data.

class Creature(
	@SerializedName("baseData") val baseData: CreatureSpecies,
	@SerializedName("level") val level: Int,
	@SerializedName("stats") val stats: TeamStats,
	@SerializedName("iv") val iv: Iv
)
{
	// TODO Generate stats depending on level.
}