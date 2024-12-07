package com.example.creamarch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.annotations.SerializedName

data class TeamStats(
    @SerializedName("maxHp") val maxHp: Int,
    @SerializedName("currentHp") val currentHp: Int,
    @SerializedName("attack") val attack: Int
){
    @Transient
    var currentHpState = mutableIntStateOf(currentHp.coerceIn(0,maxHp))

    fun initializeCurrentHpState(){
        currentHpState = mutableIntStateOf(currentHp.coerceIn(0,maxHp))
    }
    private fun changeHp(newHp: Int){
        currentHpState.intValue = newHp.coerceIn(0,maxHp)
    }
    fun reduceHp(reduce: Int){
        changeHp(currentHpState.intValue - reduce)
    }
    fun addHp(add: Int){
        changeHp(currentHpState.intValue + add)
    }
}

data class BaseStats(
    @SerializedName("baseHp") val maxHp: Int,
    @SerializedName("baseAttack") val attack: Int
)

data class Iv(
    @SerializedName("hpIv") val hpIv: Int,
    @SerializedName("attackIv") val attackIv: Int
)