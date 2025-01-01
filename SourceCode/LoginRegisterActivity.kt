package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.el_clothstoree.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Get the selectedTab value passed from IntroActivity
        val selectedTabIndex = intent.getIntExtra("selectedTab", 0) // Default to 0 (Sign In)

        setContent {
            LoginScreen(selectedTabIndex)
        }
    }
}

@Composable
fun LoginScreen(selectedTabIndex: Int) {
    val selectedTab = remember { mutableStateOf(selectedTabIndex) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .systemBarsPadding(), // Ensures content is not hidden behind system bars
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 48.dp)
                .size(150.dp)
                .align(Alignment.CenterHorizontally), // Centers the image horizontally
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tab row for Sign In / Sign Up
        TabRow(
            selectedTabIndex = selectedTab.value,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                androidx.compose.material3.TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.value]),
                    color = Color(0xFF3F51B5),
                    height = 2.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab.value == 0,
                onClick = { selectedTab.value = 0 },
                text = {
                    Text(
                        "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab.value == 0) Color(0xFF3F51B5) else Color.Black
                    )
                }
            )
            Tab(
                selected = selectedTab.value == 1,
                onClick = { selectedTab.value = 1 },
                text = {
                    Text(
                        "Buat Akun",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab.value == 1) Color(0xFF3F51B5) else Color.Black
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Show Sign In or Sign Up content based on selected tab
        if (selectedTab.value == 0) {
            TabLogin()
        } else {
            TabDaftar()
        }
    }
}

@Composable
fun TabLogin() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Function to validate email
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Text
        Text(
            text = "Selamat Datang Kembali",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.lowercase() // Ensure the email is in lowercase
            },
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            isError = email.isNotEmpty() && !isValidEmail(email),
            singleLine = true
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(8.dp),
                    tint = if (passwordVisible) Color(0xFF3A7DFF) else Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            isError = password.isNotEmpty() && password.length < 6
        )

        // Display error message if any
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Login button
        Button(
            onClick = {
                if (isValidEmail(email) && password.length >= 6) {
                    isLoading = true
                    LoginUser(email, password) { success, error ->
                        isLoading = false
                        if (success) {
                            // Navigate to main activity on successful login
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            errorMessage = error ?: "Login Gagal"
                        }
                    }
                } else {
                    errorMessage = "Masukkan email dan password yang valid"
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A7DFF))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot password text
        TextButton(
            onClick = {
                // Handle Forgot Password action here
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Lupa Password?",
                fontSize = 16.sp,
                color = Color(0xFF3A7DFF),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun LoginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    // Clean the email (trim and lowercase)
    val cleanedEmail = email.trim().lowercase()

    // Attempt to sign in with email and password
    auth.signInWithEmailAndPassword(cleanedEmail, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful
                onComplete(true, null)
            } else {
                // Handle login error
                val error = when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Email atau password salah"
                    is FirebaseAuthInvalidUserException -> "Akun dengan email ini tidak ditemukan"
                    is Exception -> {
                        // Check if the exception is network-related
                        if (task.exception?.message?.contains("network") == true) {
                            "Network error. Please check your connection."
                        } else {
                            "Login gagal, coba lagi"
                        }
                    }
                    else -> "Login gagal, coba lagi"
                }
                onComplete(false, error)
            }
        }
}

@Composable
fun TabDaftar() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Function to validate email
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    // Function to validate password
    fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}$" // Minimal 6 karakter, huruf besar, huruf kecil, dan angka
        return password.matches(regex.toRegex())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Ensure the layout is scrollable
            .imePadding() // Pastikan ini ada
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Text
        Text(
            text = "Buat Akun Baru",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.lowercase() // Ensure the email is in lowercase
            },
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            isError = email.isNotEmpty() && !isValidEmail(email),
            singleLine = true
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(8.dp),
                    tint = if (passwordVisible) Color(0xFF3A7DFF) else Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            isError = password.isNotEmpty() && !isValidPassword(password)
        )

        // Confirm Password input field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon") },
            trailingIcon = {
                Icon(
                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password",
                    modifier = Modifier
                        .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                        .padding(8.dp),
                    tint = if (confirmPasswordVisible) Color(0xFF3A7DFF) else Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            isError = confirmPassword != password
        )

        // Display error message if any
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Sign Up button
        Button(
            onClick = {
                // Validate email, password, and confirmPassword
                if (isValidEmail(email) && isValidPassword(password) && password == confirmPassword) {
                    isLoading = true
                    DaftarUser(email, password, context) { success, error ->
                        isLoading = false
                        if (success) {
                            // Navigate to main activity on successful sign-up
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            errorMessage = error ?: "Pendaftaran Gagal"
                        }
                    }
                } else {
                    errorMessage = "Periksa kembali data yang Anda masukkan."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A7DFF))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Buat Akun",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun DaftarUser(
    email: String,
    password: String,
    context: android.content.Context,
    onComplete: (Boolean, String?) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    // Buat akun dengan email dan password
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Jika pendaftaran berhasil, simpan data pengguna ke Firestore
            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
            val userMap = hashMapOf(
                "userId" to userId,
                "email" to email,
                "createdAt" to com.google.firebase.Timestamp.now()
            )

            firestore.collection("users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    // Data berhasil disimpan ke Firestore
                    onComplete(true, null)
                }
                .addOnFailureListener { exception ->
                    // Gagal menyimpan data ke Firestore
                    onComplete(false, "Gagal menyimpan data: ${exception.message}")
                }
        } else {
            // Tangani error saat pendaftaran
            val errorMessage = when (task.exception) {
                is FirebaseAuthInvalidCredentialsException -> onComplete(false, "Email tidak valid")
                is FirebaseAuthUserCollisionException -> onComplete(false, "Akun dengan email ini sudah ada")
                is FirebaseNetworkException -> onComplete(false, "Jaringan tidak stabil, coba lagi")
                is FirebaseAuthWeakPasswordException -> onComplete(false, "Password terlalu lemah")
                else -> onComplete(false, task.exception?.message ?: "Pendaftaran gagal")
            }
        }
    }
}