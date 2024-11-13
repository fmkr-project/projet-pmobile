package com.example.creamarch

// Class to store base creature data.
data class CreatureSpecies(
	val name: String,
	val menuSprite: Int,
	val baseStats: BaseStats
)
{
	fun spawnNewCreature(level: Int): Creature
	// Return a new creature of this type with randomized stats.
	{
		// TODO also randomize level & stats
		return Creature(
			this,
			level,
			generateStats()
		)
	}

	fun generateStats(): TeamStats
	// Return a set of stats of this creature.
	{
		// TODO randomize
		return TeamStats(baseStats.maxHp, baseStats.attack)
	}
}