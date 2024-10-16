package com.example.creamarch

// Class to store base creature data.
data class CreatureSpecies(
	val name: String
)
{
	fun spawnNewCreature(): Creature
	// Return a new creature of this type with randomized stats.
	{
		// TODO also randomize level
		return Creature(this, 10)
	}
}