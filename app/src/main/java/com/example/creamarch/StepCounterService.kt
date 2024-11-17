package com.example.creamarch

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepCount: Int = 0
    private val stepLength = 0.78 // Longueur moyenne d'un pas en mètres
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var lastStepCount = 0

    companion object {
        val distanceWalked = MutableStateFlow(0.0) // Distance totale en mètres
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("StepCounterService", "Service onCreate appelé")
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        Log.d("StepCounterService", "Distance totale parcourue : m")

        // Lancer une coroutine pour loguer la distance toutes les 2 secondes
        serviceScope.launch {
            while (true) {
                Log.d("StepCounterService", "Distance totale parcourue : ${distanceWalked.value} m")
                delay(2000L) // Attendre 2 secondes avant de répéter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel() // Arrêter toutes les coroutines lancées
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++
            val distance = stepCount * stepLength
            distanceWalked.value = distance
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
