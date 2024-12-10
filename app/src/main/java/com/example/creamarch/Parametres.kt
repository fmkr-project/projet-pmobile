import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ParametresMenu(
    modifier: Modifier = Modifier,
    isMusicEnabled: Boolean = true,
    isVibrationEnabled: Boolean = true,
    onMusicToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit
) {
    // States for toggles
    var musicEnabled by remember { mutableStateOf(isMusicEnabled) }
    var vibrationEnabled by remember { mutableStateOf(isVibrationEnabled) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Title of the settings menu
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Toggle for music
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable Music",  // Label for the music toggle
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = musicEnabled,  // Whether the music toggle is on or off
                onCheckedChange = {
                    musicEnabled = it
                    onMusicToggle(it) // Notify the parent about the change
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))  // Add space between toggles

        // Toggle for vibrations
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable Vibration",  // Label for the vibration toggle
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = vibrationEnabled,  // Whether the vibration toggle is on or off
                onCheckedChange = {
                    vibrationEnabled = it
                    onVibrationToggle(it) // Notify the parent about the change
                }
            )
        }
    }
}

