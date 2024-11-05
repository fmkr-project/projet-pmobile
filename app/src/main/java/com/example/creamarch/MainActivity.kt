package com.example.creamarch

import DistanceTracker
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import com.example.creamarch.ui.theme.CreamarchTheme
import android.Manifest




class MainActivity : ComponentActivity() {
	private lateinit var distanceTracker: DistanceTracker

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Initialisez DistanceTracker
		distanceTracker = DistanceTracker(this)

		// Vérifiez et demandez la permission de localisation avant de commencer le suivi
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			distanceTracker.startTracking()
		} else {
			requestLocationPermission()
		}

		setContent {
			CreamarchTheme {
				AppScreen(distanceTracker = distanceTracker)
			}
		}
	}

	private fun requestLocationPermission() {
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				distanceTracker.startTracking()
			} else {
				// Gestion du cas où la permission est refusée
			}
		}.launch(Manifest.permission.ACCESS_FINE_LOCATION)
	}

	override fun onDestroy() {
		super.onDestroy()
		distanceTracker.stopTracking()
	}
}
