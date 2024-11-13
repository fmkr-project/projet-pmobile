package com.example.creamarch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Stats(
    val maxHp: Int,
    val attack: Int,
    val defense: Int
) {
    var currentHp by mutableIntStateOf(maxHp)  // Assigner la valeur sans initialiser dans la propriété elle-même
}
