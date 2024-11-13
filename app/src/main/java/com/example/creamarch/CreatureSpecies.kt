package com.example.creamarch

// Class to store base creature data.
data class CreatureSpecies(
	val name: String,
	val menuSprite: Int
)
{
	fun spawnNewCreature(): Creature
	// Return a new creature of this type with randomized stats.
	{
		// TODO also randomize level & stats
		return Creature(
			this,
			10,
			Stats(100, 100, 100))
	}
}