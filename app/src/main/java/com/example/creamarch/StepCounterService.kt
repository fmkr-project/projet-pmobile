package com.example.creamarch

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
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
import java.util.Calendar

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

    private var totalDistance: Double = 0.0
    private var dailyDistance: Double = 0.0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("StepCounterPrefs", MODE_PRIVATE)
        totalDistance = sharedPreferences.getFloat("totalDistance", 0f).toDouble()
        dailyDistance = sharedPreferences.getFloat("dailyDistance", 0f).toDouble()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        // Planifier une réinitialisation quotidienne
        scheduleDailyReset()

        serviceScope.launch {
            while (true) {
                Log.d("StepCounterService", "Distance aujourd'hui : $dailyDistance m, Total : $totalDistance m")
                delay(2000L) // Log toutes les 2 secondes
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel()


        // Sauvegarder les distances dans SharedPreferences
        sharedPreferences.edit().apply {
            putFloat("totalDistance", totalDistance.toFloat())
            putFloat("dailyDistance", dailyDistance.toFloat())
            apply()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            val stepDistance = stepLength
            dailyDistance += stepDistance
            totalDistance += stepDistance
            distanceWalked.value = dailyDistance
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun scheduleDailyReset() {
        val resetIntent = Intent(this, ResetReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, resetIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerTime = getNextMidnightMillis()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    private fun getNextMidnightMillis(): Long {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis
    }
}
