package com.example.el_clothstoree.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.el_clothstoree.Helper.ManagementFavorite
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.R
import com.example.el_clothstoree.Utils.formatRupiah

class FavoriteActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FavoriteScreen(
                ManagementFavorite(this),
                onBackClick = {
                    finish()
                }
            )
        }
    }
}

@Composable
private fun FavoriteScreen(
    managementFavorite: ManagementFavorite = ManagementFavorite(LocalContext.current),
    onBackClick: () -> Unit
) {
    val favoriteItems = remember { mutableStateOf(managementFavorite.getFavoriteList()) }
    var activeMenuItem by remember { mutableStateOf("Favorite") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp) // Leave space for the BottomMenu
        ) {
            ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Favorite",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Back arrow icon
                    contentDescription = "Back",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onBackClick() } // Trigger back click action
                )
            }
            // Handle empty state or list of favorite items
            if (favoriteItems.value.isEmpty()) {
                Text(
                    text = "No Favorites Yet",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                FavoriteList(
                    favoriteItems = favoriteItems.value,
                    managementFavorite = managementFavorite
                ) {
                    favoriteItems.value = managementFavorite.getFavoriteList()
                }
            }
        }

        // Bottom Menu
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            activeMenuItem = activeMenuItem,
            onHomeClick = {
                activeMenuItem = "Home"
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            onCartClick = {
                activeMenuItem = "Cart"
                val intent = Intent(context, CartActivity::class.java)
                context.startActivity(intent)
            },
            onFavoriteClick = {
                activeMenuItem = "Favorite"
                // Stay on the current screen (Favorite)
            },
            onProfileClick = {
                activeMenuItem = "Profile"
                // Navigate to Profile screen
                val intent = Intent(context, ProfileActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun FavoriteList(
    favoriteItems: ArrayList<ItemsModel>,
    managementFavorite: ManagementFavorite,
    onItemChange: () -> Unit
) {
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
        items(favoriteItems) { item ->
            FavoriteItem(
                item = item,
                managementFavorite = managementFavorite,
                onItemChange = onItemChange
            )
        }
    }
}

@Composable
fun FavoriteItem(
    item: ItemsModel,
    managementFavorite: ManagementFavorite,
    onItemChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Expand to full width
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.picUrl[0]),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .background(
                    colorResource(R.color.lightGrey),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f) // Take up the remaining space for title and price
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = formatRupiah(item.price),
                color = colorResource(R.color.Blue),
                fontSize = 14.sp
            )
        }

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Remove from Favorites",
            tint = Color.Red,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    managementFavorite.removeFavorite(item)
                    onItemChange() // Refresh the list after removal
                }
                .align(Alignment.CenterVertically)
        )
    }
}