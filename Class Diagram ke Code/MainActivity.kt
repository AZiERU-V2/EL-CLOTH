@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.example.el_cloth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.el_cloth.ui.theme.ElClothTheme

class MainActivity : ComponentActivity() {
    private val repository = ECommerceRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val sampleUser = Pengguna(
            idPengguna = "exampleUserId",
            email = "contoh@email.com",
            nama = "Nama Contoh",
            alamat = "Alamat Contoh"
        )

        repository.tambahPengguna(sampleUser) { success ->
            if (success) {
                Log.d("MainActivity", "User added successfully")
            } else {
                Log.e("MainActivity", "Failed to add user")
            }
        }

        setContent {
            ElClothTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val userId = "exampleUserId"
                    var pengguna by rememberSaveable { mutableStateOf<Pengguna?>(null) }
                    var isLoading by rememberSaveable { mutableStateOf(true) }

                    LaunchedEffect(true) {
                        repository.getPengguna(userId) { retrievedUser ->
                            pengguna = retrievedUser as? Pengguna
                            isLoading = false
                        }
                    }

                    Greeting(
                        name = when {
                            isLoading -> "Loading..."
                            pengguna != null -> pengguna?.nama ?: "User not found"
                            else -> "User not found"
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElClothTheme {
        Greeting("Android")
    }
}
