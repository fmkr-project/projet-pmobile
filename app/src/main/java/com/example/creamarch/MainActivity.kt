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

class MainActivity : ComponentActivity() {
	private lateinit var distanceTracker: DistanceTracker
	private var menuStatus: MenuStatus = MenuStatus()


	@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initializePlayerTeam(this)
		initializeCreatures()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			if (ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACTIVITY_RECOGNITION
				) != PackageManager.PERMISSION_GRANTED
			) {
				requestActivityRecognitionPermission()
			} else {
				startStepCounterService()
			}
		} else {
			startStepCounterService()
		}

		setContent {
			CreamarchTheme {
				ExplorationMenu(distanceTracker = distanceTracker)
			}
		}
		// Initialisez DistanceTracker
		distanceTracker = DistanceTracker(this)

		// Vérifiez et demandez les permissions de localisation avant de commencer le suivi
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			// Demandez la permission de localisation en arrière-plan si nécessaire
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
				ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_BACKGROUND_LOCATION
				) != PackageManager.PERMISSION_GRANTED) {
				requestBackgroundLocationPermission()
			} else {
				startLocationService()
			}
		} else {
			requestFineLocationPermission()
		}

		setContent {
			CreamarchTheme {
				AppScreen(
					distanceTracker = distanceTracker,
					menuStatus = menuStatus,
				)
			}
		}
	}

	private fun requestActivityRecognitionPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				startStepCounterService()
			} else {
				showPermissionDeniedDialog("Permission pour reconnaître les activités refusée.")
			}
		}.launch(Manifest.permission.ACTIVITY_RECOGNITION)
	}

	private fun startStepCounterService() {
		Log.d("MainActivity", "Démarrage du service StepCounterService")
		val intent = Intent(this, StepCounterService::class.java)
		startService(intent)
	}


	// Demande de la permission ACCESS_FINE_LOCATION
	private fun requestFineLocationPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				// Demande la permission de localisation en arrière-plan pour Android Q et plus
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					requestBackgroundLocationPermission()
				} else {
					startLocationService()
				}
			} else {
				showPermissionDeniedDialog("La localisation est nécessaire pour suivre la distance parcourue.")
				
			}
		}.launch(Manifest.permission.ACCESS_FINE_LOCATION)
	}

	// Demande de la permission ACCESS_BACKGROUND_LOCATION pour Android Q et plus
	private fun requestBackgroundLocationPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				startLocationService()
			} else {
				showPermissionDeniedDialog("La localisation en arrière-plan est nécessaire pour suivre la distance même lorsque l'application est fermée.")
			}
		}.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
	}

	private fun startLocationService() {
		val intent = Intent(this, LocationService::class.java)
		startService(intent)
	}

	override fun onStart() {
		super.onStart()
		initializePlayerTeam(this)
		PlayerDex.loadPlayerDex(this)
	}
	override fun onStop() {
		super.onStop()
		savePlayerTeamState(this)
		PlayerDex.savePlayerDex(this)
	}

	override fun onDestroy() {
		super.onDestroy()
		savePlayerTeamState(this)
		PlayerDex.savePlayerDex(this)
		val intent = Intent(this, StepCounterService::class.java)
		stopService(intent)
	}

	private fun showPermissionDeniedDialog(message: String) {
		AlertDialog.Builder(this)
			.setTitle("Permission requise")
			.setMessage(message)
			.setPositiveButton("Paramètres") { _, _ ->
				// Ouvrir les paramètres de l'application pour permettre à l'utilisateur de modifier les permissions
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				val uri = android.net.Uri.fromParts("package", packageName, null)
				intent.data = uri
				startActivity(intent)
			}
			.setNegativeButton("Annuler", null)
			.show()
	}
}
