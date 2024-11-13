package com.example.creamarch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class TeamStats(
    val maxHp: Int,
    val attack: Int
) {
    var currentHp by mutableIntStateOf(maxHp)  // Assigner la valeur sans initialiser dans la propriété elle-même
}

data class BaseStats(
    val maxHp: Int,
    val attack: Int
)