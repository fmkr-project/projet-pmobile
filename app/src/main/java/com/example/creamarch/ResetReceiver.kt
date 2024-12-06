package com.example.creamarch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


// Récepteur pour réinitialiser les données
class ResetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val sharedPreferences = context?.getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)

        // Réinitialiser la distance journalière
        sharedPreferences?.edit()?.apply {
            putFloat("dailyDistance", 0f)
            apply()
        }

        // Régénérer la liste des créatures
        val newCreatureList = generateNewCreatureList()
        CreatureRepository.updateCreatureList(newCreatureList)

        Log.d("ResetReceiver", "Distance journalière réinitialisée et nouvelle liste de créatures générée.")
    }
}