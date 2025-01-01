package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.R
import com.example.el_clothstoree.Utils.formatRupiah
import com.example.el_clothstoree.ViewModel.MainViewModel

class ProdukActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProdukScreen(onBackClick = { finish() })
        }
    }
}

@Composable
fun ProdukScreen(viewModel: MainViewModel = viewModel(), onBackClick: () -> Unit) {
    val allItems = remember { mutableStateListOf<ItemsModel>() }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    // Mengambil data produk
    LaunchedEffect(Unit) {
        viewModel.loadAllItems()
        viewModel.allItems.observe(lifecycleOwner.value) { items ->
            allItems.clear()
            allItems.addAll(items)
        }
    }

    // Tampilan dengan Scaffold
    Scaffold(
        topBar = {
            CustomTopAppBar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            columns = GridCells.Fixed(2), // Set number of columns (2 in this case)
            contentPadding = PaddingValues(16.dp) // Padding around grid items
        ) {
            items(allItems) { item ->
                ProdukItem(item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Produk Kami",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack, // Back arrow icon
                contentDescription = "Back",
                tint = Color.Gray,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { onBackClick() }
                    .padding(start = 16.dp)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.Blue)
        )
    )
}

@Composable
fun ProdukItem(product: ItemsModel) {
    val context = LocalContext.current

    // Memanggil formatRupiah untuk memformat harga menjadi format Rupiah
    val formattedPrice = formatRupiah(product.price)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .height(250.dp) // Increased height for better spacing
    ) {
        // Memastikan gambar produk tampil dengan fallback ke gambar placeholder jika URL kosong
        AsyncImage(
            model = product.picUrl.firstOrNull() ?: R.drawable.placeholder,  // Fallback ke gambar placeholder jika URL kosong
            contentDescription = product.title,
            modifier = Modifier
                .size(175.dp)  // Ensure image is square
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))  // Rounded corners for the image
                .border(
                    2.dp,
                    colorResource(R.color.LightBlue),
                    RoundedCornerShape(10.dp)
                )  // Blue border
                .clickable {
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("object", product)
                    }
                    startActivity(context, intent, null)
                },
            contentScale = ContentScale.Crop
        )

        // Menampilkan nama produk
        Text(
            text = product.title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Menampilkan rating dan harga
        Row(
            modifier = Modifier
                .padding(top = 8.dp) // Increase padding between the title and price/rating row
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Ensure space between rating and price
        ) {
            // Menampilkan rating produk
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    modifier = Modifier.size(16.dp) // Adjust size for better alignment
                )
                Spacer(modifier = Modifier.width(4.dp)) // Add small space between the star and rating
                Text(
                    text = product.rating.toString(),
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }

            // Menampilkan harga dengan format Rupiah
            Text(
                text = formattedPrice, // Harga sudah diformat dengan fungsi formatRupiah
                color = colorResource(R.color.purple),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}