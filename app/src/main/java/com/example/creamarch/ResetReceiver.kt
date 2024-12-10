package com.example.creamarch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

// Receiver to reset data
class ResetReceiver : BroadcastReceiver() {
    // Called when the broadcast is received
    override fun onReceive(context: Context?, intent: Intent?) {
        // Get SharedPreferences to access stored data
        val sharedPreferences = context?.getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)

        // Reset the daily distance
        sharedPreferences?.edit()?.apply {
            putFloat("dailyDistance", 0f)  // Set the daily distance to 0
            apply()  // Apply the changes
        }

        // Regenerate the creature list
        val newCreatureList = generateNewCreatureList()  // Generate a new list of creatures
        CreatureRepository.updateCreatureList(newCreatureList)  // Update the creature list in the repository

        // Log the reset action for debugging
        Log.d("ResetReceiver", "Daily distance reset and new creature list generated.")
    }
}
