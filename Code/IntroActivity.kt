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

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true

        setContent {
            IntroScreen(
                onClick = { navigateToLoginActivity(true) }, // for Sign Up tab
                onSignInClick = { navigateToLoginActivity(false) } // for Sign In tab
            )
        }
    }

    // Function to navigate to LoginActivity with selected tab index
    private fun navigateToLoginActivity(isSignUp: Boolean) {
        val selectedTabIndex = if (isSignUp) 1 else 0 // 1 for Sign Up, 0 for Sign In
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("selectedTab", selectedTabIndex)
        }
        startActivity(intent)
    }
}
@Composable
@Preview
fun IntroScreen(onClick: (Boolean) -> Unit = {}, onSignInClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title text with gray color
        Text(
            text = stringResource(id = R.string.intro_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.black)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Subtitle text with dark gray color
        Text(
            text = stringResource(id = R.string.intro_sub_title),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp),
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Button(
            onClick = { onClick(true) }, // For "Let's get started", pass true for Sign Up
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Slightly smaller height for a better button size
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .shadow(4.dp, shape = RoundedCornerShape(12.dp), clip = false), // Adding shadow for elevation
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A7DFF)) // Blue button
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Center the content inside the button
            ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Create a ClickableText with the "Already have an account?" and "Sign in" parts
        val signText = stringResource(id = R.string.sign)
        val signInStart = signText.indexOf("Sign in") // Find where "Sign in" starts in the text
        val annotatedString = buildAnnotatedString {
            append(signText.substringBefore("Sign in")) // Non-clickable text
            // Style for "Sign In" part
            withStyle(style = SpanStyle(color = colorResource(R.color.Blue), fontSize = 18.sp, fontWeight = FontWeight.Bold)) {
                append("Sign in")
            }
        }

        // Displaying the text with clickable "Sign in" part
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                if (offset >= signInStart) { // Check if the "Sign in" portion was clicked
                    onSignInClick() // Trigger the onSignInClick function
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
