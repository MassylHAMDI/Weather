import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeaterTheme

@Composable
fun AddCityDialog(
    onDismiss: () -> Unit,
    onAddCity: (String) -> Unit
) {
    var cityName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add City") },
        text = {
            TextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("City Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (cityName.isNotEmpty()) {
                        onAddCity(cityName)
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
@Preview
@Composable
fun AddCityDialogPreview() {
    WeaterTheme {
        AddCityDialog(
            onDismiss = {},
            onAddCity = {}
        )
    }
}