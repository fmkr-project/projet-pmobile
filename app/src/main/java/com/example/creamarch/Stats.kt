package com.example.creamarch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.annotations.SerializedName


// Data class representing the stats of a creature's team (health and attack)
data class TeamStats(
    @SerializedName("maxHp") val maxHp: Int,  // Maximum health of the creature
    @SerializedName("currentHp") val currentHp: Int,  // Current health of the creature
    @SerializedName("attack") val attack: Int  // Attack power of the creature
){
    // State to track the current health in a mutable state (with Compose)
    @Transient
    var currentHpState = mutableIntStateOf(currentHp.coerceIn(0, maxHp))

    // Initialize the current health state based on the initial health
    fun initializeCurrentHpState(){
        currentHpState = mutableIntStateOf(currentHp.coerceIn(0, maxHp))
    }

    // Private function to change the health, ensuring it's within bounds
    private fun changeHp(newHp: Int){
        currentHpState.intValue = newHp.coerceIn(0, maxHp)
    }

    // Function to reduce health by a given amount
    fun reduceHp(reduce: Int){
        changeHp(currentHpState.intValue - reduce)
    }

    // Function to increase health by a given amount
    fun addHp(add: Int){
        changeHp(currentHpState.intValue + add)
    }
}

// Data class representing the base stats of a creature (without IVs)
data class BaseStats(
    @SerializedName("baseHp") val maxHp: Int,  // Base maximum health of the creature
    @SerializedName("baseAttack") val attack: Int  // Base attack power of the creature
)

// Data class representing the individual values (IVs) of a creature's stats
data class Iv(
    @SerializedName("hpIv") val hpIv: Int,  // Individual value for health
    @SerializedName("attackIv") val attackIv: Int  // Individual value for attack
)
