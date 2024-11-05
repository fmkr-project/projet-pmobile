import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DistanceTracker(public val context: Context) {
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var lastLocation: Location? = null

    private val _distance = MutableStateFlow(loadTotalDistance().toInt())
    val distance: StateFlow<Int> = _distance
    private var totalDistance = _distance.value.toFloat()

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lastLocation?.let { lastLoc ->
                val distancecalc = lastLoc.distanceTo(location)

                totalDistance += distancecalc
                _distance.value = Math.round(totalDistance)
                saveTotalDistance(totalDistance)
            }
            lastLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun startTracking() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            lastLocation = null
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,
                1f,
                locationListener
            )
        } else {

        }
    }
    private fun saveTotalDistance(distance: Float) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("distance_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("total_distance", distance)
            apply()
        }
    }

    public fun loadTotalDistance(): Float {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("distance_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getFloat("total_distance", 0f)
    }

    fun stopTracking() {
        saveTotalDistance(totalDistance)
        Log.d("save data", totalDistance.toString())
        locationManager.removeUpdates(locationListener)
    }
}
