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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.creamarch.ui.theme.CreamarchTheme

class MainActivity : ComponentActivity() {
	private lateinit var distanceTracker: DistanceTracker
	private var menuStatus: MenuStatus = MenuStatus()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

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
					menuStatus = menuStatus
				)
			}
		}
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

	override fun onDestroy() {
		super.onDestroy()
		distanceTracker.stopTracking()
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
