package com.example.creamarch

import DistanceTracker
import LocationService
import android.Manifest
import android.app.AlertDialog
import android.content.Context
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
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
	private lateinit var distanceTracker: DistanceTracker
	private var menuStatus: MenuStatus = MenuStatus()


	@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//clearCreatureListFromPreferences(this)
		initializePlayerTeam(this)
		val restoredCreatures = loadCreatureListFromPreferences(this)
		CreatureRepository.updateCreatureList(restoredCreatures)


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

	fun saveCreatureListToPreferences(context: Context, creatureList: List<Pair<Creature, Int>>) {
		val sharedPreferences = context.getSharedPreferences("CreaturePrefs", Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		// Convertir la liste des créatures en une chaîne JSON
		val json = Gson().toJson(creatureList)
		editor.putString("creatureList", json)
		editor.apply()
	}

	fun loadCreatureListFromPreferences(context: Context): List<Pair<Creature, Int>> {
		val sharedPreferences = context.getSharedPreferences("CreaturePrefs", Context.MODE_PRIVATE)
		val json = sharedPreferences.getString("creatureList", null)

		// Si la liste n'existe pas, on génère une nouvelle liste
		return if (json != null) {
			Log.d("test", "liste")
			Log.d("test", "JSON chargé : $json")
			val creatureListType = object : TypeToken<List<Pair<Creature, Int>>>() {}.type
			Gson().fromJson(json, creatureListType)
		} else {
			Log.d("test", "pas de liste")
			// Générer une nouvelle liste si aucune donnée n'est trouvée
			val newCreatureList = generateNewCreatureList()
			CreatureRepository.updateCreatureList(newCreatureList)
			newCreatureList
		}
	}

	fun clearCreatureListFromPreferences(context: Context) {
		val sharedPreferences = context.getSharedPreferences("CreaturePrefs", Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		// Effacer la liste des créatures
		editor.clear()
		editor.apply()

		Log.d("test", "Liste des créatures effacée")
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
		saveCreatureListToPreferences(this, CreatureRepository.creatureList.value)
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
