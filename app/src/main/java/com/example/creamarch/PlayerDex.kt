package com.example.creamarch

object PlayerDex
{
	// Stores player progression on creatures.
	var seen: MutableMap<Int, Int> = mutableMapOf()
	var caught: MutableMap<Int, Int> = mutableMapOf()

	fun saveStates()
	{

	}

	fun loadStates()
	{

	}

	fun see(what: Int)
	{
		if (what !in seen.keys)
		{
			seen[what] = 1
		}
		else
		{
			seen[what] = seen[what]!! + 1
		}
	}

	fun catch(what: Int)
	{
		see(what)
		if (what !in caught.keys)
		{
			caught[what] = 1
		}
		else
		{
			caught[what] = caught[what]!! + 1
		}

	}

	fun isSeen(what: Int): Boolean
	{
		return what in seen.keys
	}

	fun getSeen(what: Int): Int
	{
		return if (!isSeen(what)) 0
		else seen[what]!!
	}

	fun isCaught(what: Int): Boolean
	{
		return what in caught.keys
	}

	fun getCaught(what: Int): Int
	{
		return if (!isCaught(what)) 0
		else caught[what]!!
	}
}