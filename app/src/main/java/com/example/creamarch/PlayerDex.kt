package com.example.creamarch

object PlayerDex
{
	// Stores player progression on creatures.
	var seen: MutableMap<Int, Boolean> = mutableMapOf()
	var caught: MutableMap<Int, Boolean> = mutableMapOf()

	fun saveStates()
	{

	}

	fun loadStates()
	{

	}

	fun markAsSeen(what: Int)
	{
		seen.put(what, true)
	}

	fun markAsCaught(what: Int)
	{
		markAsSeen(what)
		caught.put(what, true)
	}

	fun isSeen(what: Int): Boolean
	{
		return what in seen.keys
	}

	fun isCaught(what: Int): Boolean
	{
		return what in caught.keys
	}
}