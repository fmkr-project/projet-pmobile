package com.example.creamarch

import android.content.Context
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

object PlayerDex
{
	// Stores player progression on creatures.
	var seen: MutableMap<Int, Int> = mutableMapOf()
	var caught: MutableMap<Int, Int> = mutableMapOf()

	fun savePlayerDex(context: Context)
	{
		val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		val gson = Gson()
		val seenJson = gson.toJson(seen)
		val caughtJson = gson.toJson(caught)

		editor.putString("playerdex_seen", seenJson)
		editor.putString("playerdex_caught", caughtJson)

		editor.apply()
	}

	fun loadPlayerDex(context: Context)
	{
		val sharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
		val seenJson = sharedPreferences.getString("playerdex_seen", null)
		val caughtJson = sharedPreferences.getString("playerdex_caught", null)

		if (seenJson != null && caughtJson != null)
		{
			val dexTypes = object : TypeToken<MutableMap<Int, Int>>() {}.type
			seen = Gson().fromJson(seenJson, dexTypes)
			caught = Gson().fromJson(caughtJson, dexTypes)
		}
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

	fun getTotalSeen(): Int
	{
		if (seen.isEmpty()) return 0
		return seen.values.reduce { sum, el -> sum + el }
	}

	fun getTotalCaught(): Int
	{
		if (caught.isEmpty()) return 0
		return caught.values.reduce { sum, el -> sum + el }
	}

	fun getSpeciesCaught(): Int
	{
		return caught.size
	}
}