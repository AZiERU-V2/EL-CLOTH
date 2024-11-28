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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.R
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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back Button and Search Bar
        TopSection(onBackClick = onBackClick, searchQuery = searchQuery)

        // Show loading indicator if searching
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Show search results
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (searchResults.isNullOrEmpty() && searchQuery.value.isNotEmpty()) {
                    item {
                        Text(
                            text = "No results found",
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
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
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search Products",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    searchQuery.value.takeIf { it.isNotEmpty() }?.let {
                        // Trigger the search in the ViewModel
                    }
                }
            )
        )
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
        Row(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(item.picUrl.firstOrNull()), // Load image with Coil
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.small) // Rounded corners for image
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details with vertical scroll for long text
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = formatRupiah(item.price),
                    color = Color.Blue,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // View Details Button
                Button(
                    onClick = {
                        val intent = Intent(context, DetailActivity::class.java).apply {
                            putExtra("object", item) // Mengirim objek ke Intent
                        }
                        context.startActivity(intent) // Membuka DetailActivity dengan Intent
                        // Start the DetailActivity with the intent
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("View Details", color = MaterialTheme.colorScheme.onPrimary)
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