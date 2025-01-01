package com.example.el_clothstoree.Activity

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.R
import com.example.el_clothstoree.Utils.formatRupiah


@Composable
fun ListItems(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(500.dp)
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(items.size) { row ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecommendedItem(items, row)
            }
        }

    }
}
@Composable
fun ListItemsFullSize(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(items.size) { row ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecommendedItem(items, row)
            }
        }

    }
}

@Composable
fun RecommendedItem(items: List<ItemsModel>, pos: Int) {
    val context = LocalContext.current

    // Memanggil formatRupiah untuk memformat harga menjadi format Rupiah
    val formattedPrice = formatRupiah(items[pos].price)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .height(225.dp)
    ) {
        AsyncImage(
            model = items[pos].picUrl.firstOrNull() ?: R.drawable.placeholder,  // Fallback ke gambar placeholder jika URL kosong
            contentDescription = items[pos].title,
            modifier = Modifier
                .width(175.dp)
                .height(175.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))  // Menjaga bentuk sudut kotak tetap melengkung
                .border(2.dp, Color(0xFF3A7DFF), RoundedCornerShape(10.dp))  // Update to your preferred color for border
                .clickable {
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("object", items[pos])
                    }
                    startActivity(context, intent, null)
                },
            contentScale = ContentScale.Crop
        )
        Text(
            text = items[pos].title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
                Text(
                    text = items[pos].rating.toString(),
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }
            // Update to your preferred color for the price text
            Text(
                text = formattedPrice, // Harga sudah diformat dengan fungsi formatRupiah
                color = Color(0xFF3F51B5),  // Use your preferred color here
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}