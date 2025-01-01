package com.example.el_clothstoree.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.el_clothstoree.Utils.formatRupiah
import com.example.el_clothstoree.ui.theme.DarkBlue40
import com.example.el_clothstoree.ui.theme.LightBlue80
import com.example.el_clothstoree.ui.theme.SoftBlue80
import com.example.el_clothstoree.ui.theme.SoftBlueGrey40
import kotlin.random.Random

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve item price and delivery fee passed from CartActivity
        val itemTotal = intent.getDoubleExtra("ITEM_TOTAL", 0.0)  // Total price of items
        val delivery = intent.getDoubleExtra("DELIVERY", 0.0)      // Delivery fee

        // Define the tax rate (for example, 10%)
        val taxRate = 0.10 // 10% tax rate

        // Calculate the total amount without tax
        val totalAmountWithoutTax = itemTotal + delivery

        // Calculate the tax based on the total amount (item price + delivery fee)
        val tax = totalAmountWithoutTax * taxRate

        // Calculate the total amount including the tax
        val totalAmount = totalAmountWithoutTax + tax

        setContent {
            PaymentScreen(
                itemTotal = itemTotal,
                tax = tax,
                delivery = delivery,
                totalAmount = totalAmount
            )
        }
    }
}

@Composable
fun PaymentScreen(
    itemTotal: Double,
    tax: Double,
    delivery: Double,
    totalAmount: Double
) {
    val context = LocalContext.current // Konteks lokal
    var showSuccess by remember { mutableStateOf(false) }  // State to control visibility of the success tab
    var receiptNumber by remember { mutableStateOf(generateReceiptNumber()) }  // Random receipt number

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        Text(
            text = "Payment",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = DarkBlue40
        )

        // Item Details Section
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = SoftBlue80),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Displaying item total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Item Price:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftBlueGrey40
                    )
                    Text(
                        text = formatRupiah(itemTotal),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue40
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Displaying tax
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tax (10%):",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftBlueGrey40
                    )
                    Text(
                        text = formatRupiah(tax),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue40
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Displaying delivery fee
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Delivery Fee:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftBlueGrey40
                    )
                    Text(
                        text = formatRupiah(delivery),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue40
                    )
                }
            }
        }

        // Total Amount Section
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = SoftBlue80),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Amount:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftBlueGrey40
                )
                Text(
                    text = formatRupiah(totalAmount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue40
                )
            }
        }

        // Payment Method Section
        Text(
            text = "Payment Methods",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Opsi metode pembayaran
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PaymentMethodOption(
                icon = Icons.Default.Whatsapp,
                label = "Contact via WhatsApp",
                phoneNumber = "6281234567890" // Ganti dengan nomor WA Anda
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pay Now Button
        Button(
            onClick = {

                // Tampilkan tab sukses
                receiptNumber = generateReceiptNumber()
                showSuccess = true
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)), // DarkBlue40
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Pay Now", fontSize = 18.sp, color = Color.White)
        }
    }

    // Success tab showing after Pay Now is clicked
    AnimatedVisibility(
        visible = showSuccess,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(animationSpec = tween(500)),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(animationSpec = tween(500)),
        modifier = Modifier
            .fillMaxSize() // Make sure it takes full size
            .padding(16.dp) // Padding around the animated content
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(), // Make sure the Box takes up the full available space
            contentAlignment = Alignment.Center // Center the content inside the Box
        ) {
            SuccessTab(receiptNumber = receiptNumber, onFinishClick = {
                // Navigate to HistoryActivity when "Selesai" is clicked
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            })
        }
    }
}

@Composable
fun PaymentMethodOption(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, phoneNumber: String) {
    val context = LocalContext.current // Access the context here

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                openWhatsApp(context, phoneNumber)  // Use the context directly
            }
    ) {
        // The Card wrapped in a Row, and we use weight for space distribution
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue80),
            modifier = Modifier
                .weight(1f)  // The weight modifier here makes this card take the available space
                .padding(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(40.dp),
                    tint = DarkBlue40
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = label, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = SoftBlueGrey40)
            }
        }
    }
}

// Fungsi untuk membuka WhatsApp
fun openWhatsApp(context: Context, phoneNumber: String) {
    try {
        val uri = Uri.parse("https://wa.me/$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun SuccessTab(receiptNumber: String, onFinishClick: () -> Unit) {
    LocalContext.current // Access context here for navigation

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Padding around the whole success tab
    ) {
        // Background for the success tab
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circle with check vector icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Green, shape = RoundedCornerShape(30.dp))
                        .padding(12.dp)
                ) {
                    // Use the vector drawable here for the check icon
                    Icon(
                        imageVector = Icons.Filled.Check, // Use the correct icon from Material Icons
                        contentDescription = "Check Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pembayaran Telah Selesai",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Nomor Resi: $receiptNumber",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Finish button that triggers the onFinishClick action
                Button(
                    onClick = {
                        onFinishClick() // Trigger onFinishClick when clicked
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(text = "Selesai", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

fun generateReceiptNumber(): String {
    // Generate a random receipt number (for demo purposes)
    val random = Random.nextInt(100000, 999999)
    return "RESI-$random"
}