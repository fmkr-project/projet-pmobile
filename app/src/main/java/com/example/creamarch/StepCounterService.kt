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

// Service to track steps, calculate distance, and reset daily distance at midnight
class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepCount: Int = 0
    private val stepLength = 0.78 // Average step length in meters
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var lastStepCount = 0

    companion object {
        val distanceWalked = MutableStateFlow(0.0) // Total distance in meters
    }

    private var totalDistance: Double = 0.0
    private var dailyDistance: Double = 0.0
    private lateinit var sharedPreferences: SharedPreferences

    // Called when the service is created
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

        // Schedule a daily reset for the distance counters
        scheduleDailyReset()

        // Coroutine to log the distance every 2 seconds
        serviceScope.launch {
            while (true) {
                //Log.d("StepCounterService", "Today's Distance: $dailyDistance m, Total: $totalDistance m")
                delay(2000L) // Log every 2 seconds
            }
        }
    }

    // Called when the service is destroyed
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)  // Unregister the sensor listener
        serviceScope.cancel()  // Cancel the coroutine

        // Save the total and daily distances to SharedPreferences
        sharedPreferences.edit().apply {
            putFloat("totalDistance", totalDistance.toFloat())
            putFloat("dailyDistance", dailyDistance.toFloat())
            apply()
        }
    }

    // Method to handle binding the service (not used here)
    override fun onBind(intent: Intent?): IBinder? = null

    // Method to handle sensor data when steps are detected
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            val stepDistance = stepLength  // Calculate the distance per step
            dailyDistance += stepDistance  // Add to the daily distance
            totalDistance += stepDistance  // Add to the total distance
            distanceWalked.value = dailyDistance  // Update the total walked distance
        }
    }

    // Method to handle sensor accuracy changes (not used here)
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Method to schedule a daily reset of the distances at midnight
    private fun scheduleDailyReset() {
        val resetIntent = Intent(this, ResetReceiver::class.java)  // Intent to trigger the reset
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, resetIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerTime = getNextMidnightMillis()  // Get the time for the next midnight
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,  // Trigger at midnight
            AlarmManager.INTERVAL_DAY,  // Repeat daily
            pendingIntent  // PendingIntent to trigger the reset
        )
    }

    // Helper method to get the time in milliseconds for the next midnight
    private fun getNextMidnightMillis(): Long {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, 0)  // Set time to midnight
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)  // Move to the next day
        }
        return calendar.timeInMillis  // Return the time in milliseconds
    }
}
