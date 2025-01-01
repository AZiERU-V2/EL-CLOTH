package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.Utils.formatRupiah
import com.example.el_clothstoree.ViewModel.MainViewModel

class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchScreen(onBackClick = {
                onBackPressed()
            })
        }
    }
}

@Composable
fun SearchScreen(onBackClick: () -> Unit) {
    val viewModel: MainViewModel = viewModel()
    val searchQuery = remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState(emptyList())
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            isLoading = true
            viewModel.searchProducts(searchQuery.value)
            isLoading = false
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding() // Adds padding for the status and navigation bars
        .padding(16.dp)) { // Additional padding inside the column
        // Show loading indicator if searching
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Move TopSection below navbar
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    TopSection(onBackClick = onBackClick, searchQuery = searchQuery)
                }
                if (searchResults.isNullOrEmpty() && searchQuery.value.isNotEmpty()) {
                    item {
                        Text(
                            text = "No results found",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(searchResults) { item ->
                        SearchResultItem(item)  // Passing item directly
                    }
                }
            }
        }
    }
}

@Composable
fun TopSection(onBackClick: () -> Unit, searchQuery: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)  // Additional padding to the left and right
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Search bar with the back button inside it
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Vertically center the contents
                    modifier = Modifier
                        .background(
                            Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .fillMaxWidth() // Ensure the Row takes up the full width of the parent
                        .height(50.dp)  // Adjusted height to match the row layout
                        .padding(horizontal = 16.dp),  // Padding inside the Row
                ) {
                    // Back Button inside the search bar
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // Back arrow icon
                        contentDescription = "Back",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onBackClick() } // Trigger back click action
                            .padding(end = 8.dp) // Padding between icon and text
                    )

                    // Search TextField to type the query
                    TextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        placeholder = {
                            Text(
                                text = "Search for products...",
                                color = Color.Gray,
                                fontSize = 14.sp // Adjusted font size for placeholder
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp, // Increased font size for text input
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                searchQuery.value.takeIf { it.isNotEmpty() }?.let {
                                    // Trigger search logic
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth() // Ensures the TextField takes full width
                            .height(50.dp) // Adjusted height to match the row layout
                            .background(Color.Transparent) // Ensures no extra background is added to TextField
                            .padding(start = 8.dp) // Space between the back icon and the text input
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(item: ItemsModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
            .clip(shape = MaterialTheme.shapes.medium)
            .shadow(elevation = 4.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Centers the image and text vertically
        ) {
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(item.picUrl.firstOrNull()), // Load image with Coil
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.small) // Rounded corners for image
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details Column
            Column(
                modifier = Modifier
                    .weight(1f) // Ensures text takes up remaining space
                    .padding(vertical = 8.dp) // Added some vertical padding for spacing between text elements
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp) // Reduced bottom padding for closer spacing
                )

                Text(
                    text = formatRupiah(item.price),
                    color = Color(0xFF3F51B5),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp) // Bottom padding for spacing between price and button
                )

                // View Details Button
                Button(
                    onClick = {
                        // Create an Intent to open the DetailActivity
                        val intent = Intent(context, DetailActivity::class.java).apply {
                            putExtra("object", item) // Send the object to the intent
                        }

                        // Start the DetailActivity with the intent
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(), // Make the button span the full width
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5)
                    ),
                    shape = MaterialTheme.shapes.medium // Rounded corners for button
                ) {
                    // Display button text with text color according to theme
                    Text(
                        text = "View Details",
                        color = MaterialTheme.colorScheme.onPrimary  // Text color from MaterialTheme
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen(onBackClick = {})
}