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
        Text(
            text = "Paramètres",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Toggle for music
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activer la musique",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = musicEnabled,
                onCheckedChange = {
                    musicEnabled = it
                    onMusicToggle(it) // Notify the parent
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle for vibrations
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activer les vibrations",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = vibrationEnabled,
                onCheckedChange = {
                    vibrationEnabled = it
                    onVibrationToggle(it) // Notify the parent
                }
            )
        }
    }
}
