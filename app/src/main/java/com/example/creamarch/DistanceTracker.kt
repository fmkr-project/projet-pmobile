import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DistanceTracker(public val context: Context) {
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var lastLocation: Location? = null
    private var totalDistance = 0f

    private val _distance = MutableStateFlow(0f)
    val distance: StateFlow<Float> = _distance

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lastLocation?.let { lastLoc ->
                val distance = lastLoc.distanceTo(location)
                totalDistance += distance
                _distance.value = totalDistance
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
            totalDistance = 0f
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,
                1f,
                locationListener
            )
        } else {

        }
    }

    fun stopTracking() {
        locationManager.removeUpdates(locationListener)
    }
}
