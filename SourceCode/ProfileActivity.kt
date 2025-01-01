package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreenWithLogout()
        }
    }
}

@Composable
fun ProfileScreenWithLogout() {
    val context = LocalContext.current
    ProfileScreen(
        onLogoutClick = {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    )
}

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit) {
    var activeMenuItem by remember { mutableStateOf("Profile") }
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var userGender by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userDOB by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingAddress by remember { mutableStateOf(false) }
    var isEditingPhone by remember { mutableStateOf(false) }
    var isEditingGender by remember { mutableStateOf(false) }
    var isEditingDOB by remember { mutableStateOf(false) }

    // Separate state for fields that will be edited
    var newName by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newGender by remember { mutableStateOf("") }
    var newDOB by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName = document.getString("nama") ?: ""
                        userAddress = document.getString("alamat") ?: ""
                        userPhone = document.getString("phone") ?: ""
                        userGender = document.getString("gender") ?: ""
                        userDOB = document.getString("dob") ?: "Atur Tanggal Lahir"
                        userEmail = document.getString("email") ?: ""

                        // Inisialisasi nilai untuk mode edit
                        newName = userName
                        newAddress = userAddress
                        newPhone = userPhone
                        newGender = userGender
                        newDOB = userDOB
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        }
    }

    // Reset new value when entering edit mode
    fun onEditNameClick() {
        if (isEditingName) newName = userName
        isEditingName = !isEditingName
    }

    fun onEditAddressClick() {
        if (isEditingAddress) newAddress = userAddress
        isEditingAddress = !isEditingAddress
    }

    fun onEditPhoneClick() {
        if (isEditingPhone) newPhone = userPhone
        isEditingPhone = !isEditingPhone
    }

    fun onEditGenderClick() {
        if (isEditingGender) newGender = userGender
        isEditingGender = !isEditingGender
    }

    fun onEditDOBClick() {
        if (isEditingDOB) newDOB = userDOB
        isEditingDOB = !isEditingDOB
    }

    Surface(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Section with Blue Background
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF3A7DFF))
                        .height(120.dp)
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Icon
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Field Rows with editable fields
                ProfileFieldRow(
                    label = "Nama",
                    value = newName,
                    isEditing = isEditingName,
                    onEditClick = { onEditNameClick() },
                    onValueChange = { newName = it },
                    placeholder = "Masukkan Nama Anda",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "nama"
                )

                ProfileFieldRow(
                    label = "Alamat",
                    value = newAddress,
                    isEditing = isEditingAddress,
                    onEditClick = { onEditAddressClick() },
                    onValueChange = { newAddress = it },
                    placeholder = "Masukkan Alamat Anda",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "alamat"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Other form fields
                ProfileFieldRow(
                    label = "Jenis Kelamin",
                    value = newGender,
                    isEditing = isEditingGender,
                    onEditClick = { onEditGenderClick() },
                    onValueChange = { newGender = it },
                    placeholder = "Pilih Jenis Kelamin",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "gender"
                )
                ProfileFieldRow(
                    label = "Tanggal Lahir",
                    value = newDOB,
                    isEditing = isEditingDOB,
                    onEditClick = { onEditDOBClick() },
                    onValueChange = { newDOB = it },
                    placeholder = "Masukkan Tanggal Lahir",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "dob"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone and Email Section
                ProfileFieldRow(
                    label = "No. Handphone",
                    value = newPhone,
                    isEditing = isEditingPhone,
                    onEditClick = { onEditPhoneClick() },
                    onValueChange = { newPhone = it },
                    placeholder = "Masukkan Nomor Handphone",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "phone"
                )
                ProfileFieldRow(
                    label = "Email",
                    value = userEmail,
                    isEditing = false,
                    onEditClick = {},
                    onValueChange = {},
                    placeholder = "",
                    userId = userId ?: "",
                    firestore = firestore,
                    fieldName = "email"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = { onLogoutClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A7DFF))
                ) {
                    Text(text = "Log out", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ProfileFieldRow(
    label: String,
    value: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onValueChange: (String) -> Unit,
    placeholder: String,
    userId: String,
    firestore: FirebaseFirestore,
    fieldName: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(text = placeholder) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = if (value.isNotEmpty()) value else "Belum diisi",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            IconButton(onClick = {
                if (isEditing) {
                    // Simpan nilai ke Firestore saat mode edit selesai
                    updateFirestoreField(firestore, userId, fieldName, value)
                }
                onEditClick() // Ubah antara mode edit dan view
            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.ChevronRight else Icons.Default.ChevronRight,
                    contentDescription = "Edit"
                )
            }
        }
    }
}

fun updateFirestoreField(firestore: FirebaseFirestore, userId: String, fieldName: String, value: String) {
    if (userId.isNotEmpty()) {
        firestore.collection("users").document(userId)
            .update(fieldName, value)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }
}