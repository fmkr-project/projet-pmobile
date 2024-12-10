import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.creamarch.R


// Service that tracks the user's location and updates the distance walked
class LocationService : Service() {

    private lateinit var locationManager: LocationManager
    private lateinit var distanceTracker: DistanceTracker


    // Called when the service is created
    override fun onCreate() {
        super.onCreate()
        distanceTracker = DistanceTracker(this)  // Initialize the distance tracker
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager  // Get the LocationManager system service
        startForegroundService()  // Start the service in the foreground
        startLocationUpdates()  // Start receiving location updates
    }

    @SuppressLint("ForegroundServiceType")
    // Starts the service as a foreground service to avoid it being killed by the system
    private fun startForegroundService() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location_channel",  // Channel ID
                "Location Service",  // Channel name
                NotificationManager.IMPORTANCE_DEFAULT  // Importance level
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Create the notification to display while the service is running in the foreground
        val notification: Notification = NotificationCompat.Builder(this, "location_channel")
            .setContentTitle("Distance Tracker")  // Title of the notification
            .setContentText("Tracking distance in background")  // Text content of the notification
            .setSmallIcon(R.drawable.ic_location)  // Icon to display in the notification
            .build()

        startForeground(1, notification)  // Start the service in the foreground with the notification
    }

    // Starts location updates and listens for changes in location
    private fun startLocationUpdates() {
        // LocationListener to handle location updates
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                distanceTracker.updateDistance(location)  // Update the distance when the location changes
            }

            // These methods are unused but required by the LocationListener interface
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        // Check if the app has permission to access the fine location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request location updates from the GPS provider
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,  // Use GPS provider
                2000L,  // Update interval (in milliseconds)
                1f,  // Minimum distance between updates (in meters)
                locationListener  // The listener that handles location updates
            )
        }
    }

    // This method is required by the Service class but is not used in this case
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
