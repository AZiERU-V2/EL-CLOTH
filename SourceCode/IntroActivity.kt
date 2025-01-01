package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.el_clothstoree.R

// Activity untuk layar intro
class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Atur tampilan agar mencakup seluruh layar (fullscreen)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true // Ubah status bar menjadi terang

        // Set layar menggunakan Compose
        setContent {
            IntroScreen(
                onClick = { navigateToLoginActivity(true) }, // Ketika tombol "Daftar" ditekan
                onSignInClick = { navigateToLoginActivity(false) } // Ketika tombol "Masuk" ditekan
            )
        }
    }

    // Fungsi untuk navigasi ke LoginActivity
    private fun navigateToLoginActivity(isSignUp: Boolean) {
        val selectedTabIndex = if (isSignUp) 1 else 0 // 1 untuk Daftar, 0 untuk Masuk
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("selectedTab", selectedTabIndex) // Kirim data tab yang dipilih ke LoginActivity
        }
        startActivity(intent)
    }
}

// Komposisi layar intro
@Composable
@Preview
fun IntroScreen(onClick: (Boolean) -> Unit = {}, onSignInClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Isi seluruh layar
            .background(Color.White) // Latar belakang putih
            .systemBarsPadding() // Tambahkan padding untuk area system bar (status bar dan navigation bar)
            .verticalScroll(rememberScrollState()) // Agar konten dapat digulir
            .padding(16.dp), // Padding luar sebesar 16 dp
        horizontalAlignment = Alignment.CenterHorizontally // Konten diratakan di tengah secara horizontal
    ) {
        // Logo aplikasi
        Image(
            painter = painterResource(id = R.drawable.intro_logo), // Sumber gambar logo
            contentDescription = null, // Tidak ada deskripsi konten
            modifier = Modifier
                .padding(top = 48.dp) // Tambahkan jarak atas sebesar 48 dp
                .fillMaxWidth()
                .fillMaxHeight(), // Isi tinggi layar
            contentScale = ContentScale.Crop // Skala gambar untuk mengisi area
        )

        Spacer(modifier = Modifier.height(32.dp)) // Spasi vertikal sebesar 32 dp

        // Judul layar intro
        Text(
            text = stringResource(id = R.string.intro_title), // Teks dari string resources
            fontSize = 26.sp, // Ukuran font
            fontWeight = FontWeight.Bold, // Font tebal
            textAlign = TextAlign.Center, // Teks diratakan di tengah
            color = colorResource(R.color.black) // Warna hitam
        )

        Spacer(modifier = Modifier.height(32.dp)) // Spasi vertikal sebesar 32 dp

        // Subjudul layar intro
        Text(
            text = stringResource(id = R.string.intro_sub_title), // Subjudul dari string resources
            fontSize = 12.sp, // Ukuran font
            modifier = Modifier.padding(top = 16.dp), // Tambahkan jarak atas sebesar 16 dp
            color = Color.DarkGray, // Warna abu-abu gelap
            textAlign = TextAlign.Center, // Teks diratakan di tengah
            lineHeight = 24.sp // Jarak antar baris
        )

        // Tombol "Login"
        Button(
            onClick = { onSignInClick() }, // Aksi saat tombol ditekan
            modifier = Modifier
                .fillMaxWidth() // Tombol memenuhi lebar layar
                .height(100.dp) // Tinggi tombol 100 dp
                .padding(horizontal = 32.dp, vertical = 16.dp) // Tambahkan padding horizontal dan vertikal
                .shadow(
                    4.dp, // Bayangan tombol
                    shape = RoundedCornerShape(12.dp), // Bentuk sudut tombol melengkung
                    clip = false
                ),
            shape = RoundedCornerShape(12.dp), // Bentuk tombol
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A7DFF)) // Warna tombol biru
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), // Isi ukuran tombol
                contentAlignment = Alignment.Center // Pusatkan konten dalam tombol
            ) {
                Text(
                    text = "Login", // Teks tombol
                    color = Color.White, // Warna teks putih
                    fontSize = 18.sp, // Ukuran teks
                    fontWeight = FontWeight.Bold // Font tebal
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spasi vertikal sebesar 16 dp

        // Teks klik untuk "Buat Akun"
        val signText = stringResource(id = R.string.sign) // Ambil teks dari string resources
        val signUpStart = signText.indexOf("Buat Akun") // Cari posisi teks "Buat Akun"
        val annotatedString = buildAnnotatedString {
            append(signText.substringBefore("Buat Akun")) // Tambahkan teks sebelum "Buat Akun"
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF3A7DFF), // Warna biru untuk teks "Buat Akun"
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Buat Akun") // Teks "Buat Akun" dengan gaya khusus
            }
        }

        // Teks yang dapat diklik untuk "Buat Akun"
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                if (offset >= signUpStart) { // Periksa apakah teks "Buat Akun" diklik
                    onClick(true) // Jalankan fungsi onClick untuk "Buat Akun"
                }
            },
            modifier = Modifier.padding(top = 16.dp) // Tambahkan jarak atas sebesar 16 dp
        )
    }
}