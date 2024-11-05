package com.example.creamarch



import DistanceTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDistanceTracker {
    private val _distance = MutableStateFlow(1500f) // Distance simulée
    val distance: StateFlow<Float> = _distance

}
