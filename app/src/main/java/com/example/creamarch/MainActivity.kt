package com.example.creamarch

import DistanceTracker
import LocationService
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.creamarch.ui.theme.CreamarchTheme

// Main class of the application managing permissions, services, and the user interface.
class MainActivity : ComponentActivity() {

	// Instance to track the distance walked and manage the app's menu
	private lateinit var distanceTracker: DistanceTracker
	private var menuStatus: MenuStatus = MenuStatus()

	// Method called when the activity is created
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Initialize the player's team and creatures
		initializePlayerTeam(this)
		initializeCreatures()

		// Request activity recognition permission for Android Q and above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			if (ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACTIVITY_RECOGNITION
				) != PackageManager.PERMISSION_GRANTED
			) {
				requestActivityRecognitionPermission()  // Request activity permission
			} else {
				startStepCounterService()  // Start the step counter service
			}
		} else {
			startStepCounterService()  // Directly start the step counter service for versions below Q
		}

		// Configure the user interface with a custom theme
		setContent {
			CreamarchTheme {
				ExplorationMenu(distanceTracker = distanceTracker)
			}
		}

		// Initialize the DistanceTracker to track the distance traveled
		distanceTracker = DistanceTracker(this)

		// Check and request location permissions before starting tracking
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			// Request background location permission for Android Q and above
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
				ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_BACKGROUND_LOCATION
				) != PackageManager.PERMISSION_GRANTED) {
				requestBackgroundLocationPermission()
			} else {
				startLocationService()  // Start the location service
			}
		} else {
			requestFineLocationPermission()  // Request fine location permission
		}

		// Configure the user interface again with the updated data
		setContent {
			CreamarchTheme {
				AppScreen(
					distanceTracker = distanceTracker,
					menuStatus = menuStatus,
				)
			}
		}
	}

	// Method to request activity recognition permission
	private fun requestActivityRecognitionPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				startStepCounterService()  // Start the service if permission is granted
			} else {
				showPermissionDeniedDialog("Permission to recognize activities denied.")
			}
		}.launch(Manifest.permission.ACTIVITY_RECOGNITION)
	}

	// Method to start the step counter service
	private fun startStepCounterService() {
		Log.d("MainActivity", "Starting StepCounterService")
		val intent = Intent(this, StepCounterService::class.java)
		startService(intent)
	}

	// Method to request ACCESS_FINE_LOCATION permission
	private fun requestFineLocationPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				// Request background location permission for Android Q and above
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					requestBackgroundLocationPermission()
				} else {
					startLocationService()
				}
			} else {
				showPermissionDeniedDialog("Location permission is required to track distance.")
			}
		}.launch(Manifest.permission.ACCESS_FINE_LOCATION)
	}

	// Method to request ACCESS_BACKGROUND_LOCATION permission for Android Q and above
	private fun requestBackgroundLocationPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				startLocationService()  // Start location service in the background if permission is granted
			} else {
				showPermissionDeniedDialog("Background location is required to track distance even when the app is closed.")
			}
		}.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
	}

	// Method to start the location service
	private fun startLocationService() {
		val intent = Intent(this, LocationService::class.java)
		startService(intent)
	}

	// Method called when the activity starts, loads player data
	override fun onStart() {
		super.onStart()
		initializePlayerTeam(this)
		PlayerDex.loadPlayerDex(this)
	}

	// Method called when the activity stops, saves player data
	override fun onStop() {
		super.onStop()
		savePlayerTeamState(this)
		PlayerDex.savePlayerDex(this)
	}

	// Method called when the activity is destroyed, stops the step counter service
	override fun onDestroy() {
		super.onDestroy()
		savePlayerTeamState(this)
		PlayerDex.savePlayerDex(this)
		val intent = Intent(this, StepCounterService::class.java)
		stopService(intent)
	}

	// Method to show a dialog when permission is denied
	private fun showPermissionDeniedDialog(message: String) {
		AlertDialog.Builder(this)
			.setTitle("Permission Required")
			.setMessage(message)
			.setPositiveButton("Settings") { _, _ ->
				// Open app settings to allow the user to modify permissions
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				val uri = android.net.Uri.fromParts("package", packageName, null)
				intent.data = uri
				startActivity(intent)
			}
			.setNegativeButton("Cancel", null)
			.show()
	}
}
