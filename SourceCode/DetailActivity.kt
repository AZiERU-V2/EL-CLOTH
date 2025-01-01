package com.example.el_clothstoree.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.el_clothstoree.Helper.ManagementFavorite
import com.example.el_clothstoree.Helper.ManagmentCart
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.R
import com.example.el_clothstoree.Utils.formatRupiah
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


class DetailActivity : BaseActivity() {
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getParcelableExtra<ItemsModel>("object") ?: throw IllegalArgumentException("Item must not be null")
        managmentCart = ManagmentCart(this)

        setContent {
            DetailScreen(
                item = item,
                onBackClick = { finish() },
                onAddToCartClick = {
                    item.numberInCart = 1
                    managmentCart.insertItem(item)
                },
                onCartClick = {
                    startActivity(Intent(this, CartActivity::class.java))
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailScreen(
    item: ItemsModel,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onCartClick: () -> Unit
) {
    var selectedImageUrl by remember { mutableStateOf(item.picUrl.first()) }
    var selectedModelIndex by remember { mutableStateOf(-1) }
    val isFavorite = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState()
    val context = LocalContext.current
    val itemPrice = item.price // Harga item

    // Create an instance of ManagementFavorite with the context
    val managementFavorite = remember { ManagementFavorite(context) }

    // Tax and delivery calculations (example)
    val delivery = 5000.0 // Example delivery fee

    // LaunchedEffect for updating the selectedImageUrl when the pager state changes
    LaunchedEffect(pagerState.currentPage) {
        selectedImageUrl = item.picUrl[pagerState.currentPage]
    }

    // LaunchedEffect for updating the pager when the selectedImageUrl changes
    LaunchedEffect(selectedImageUrl) {
        val index = item.picUrl.indexOf(selectedImageUrl)
        if (index >= 0) {
            pagerState.scrollToPage(index) // Update pagerState to match the selected image
        }
    }

    LaunchedEffect(item) {
        isFavorite.value = managementFavorite.isFavorite(item)  // Check if the item is in favorites
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back and Cart Icons
        ConstraintLayout(
            modifier = Modifier
                .padding(top = 36.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            val (backIcon, cartIcon) = createRefs()

            // Back Icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Back arrow icon
                contentDescription = "Back",
                tint = Color.Gray,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { onBackClick() }
                    .padding(end = 8.dp)
                    .constrainAs(backIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            // Cart Icon
            IconButton(
                onClick = { onCartClick() },
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .constrainAs(cartIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Open Cart",
                    tint = Color.Black
                )
            }
        }

        HorizontalPager(
            count = item.picUrl.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(model = item.picUrl[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, Color(0xFF3A7DFF), RoundedCornerShape(8.dp))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(item.picUrl) { imageUrl ->
                    ImageThumbnail(
                        imageUrl = imageUrl,
                        isSelected = selectedImageUrl == imageUrl,
                        onClick = { selectedImageUrl = imageUrl }
                    )
                }
            }

            // Favorite Icon with toggle logic
            IconButton(
                onClick = {
                    if (isFavorite.value) {
                        managementFavorite.removeFavorite(item)  // Logic for removing from favorites
                    } else {
                        managementFavorite.addFavorite(item)  // Logic for adding to favorites
                    }
                    // Toggle status after the operation
                    isFavorite.value = !isFavorite.value
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        color = colorResource(R.color.lightGrey),
                        shape = CircleShape // Use CircleShape for a circular button
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Add to Favorites",
                    tint = if (isFavorite.value) Color.Red else Color.White // Change color based on favorite status
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 23.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            Text(
                text = formatRupiah(item.price),
                fontSize = 22.sp
            )
        }
        RatingBar(rating = item.rating)

        ModelSelector(
            models = item.model,
            selectedModeIndex = selectedModelIndex,
            onModelSelected = { selectedModelIndex = it }
        )

        Text(
            text = item.description.joinToString("\n"),
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Row to align the "Buy Now" button and Add to Cart icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Padding around the row
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) // Even space between buttons and center alignment
        ) {
            // "Buy Now" button
            Button(
                onClick = {
                    val intent = Intent(context, PaymentActivity::class.java)
                    intent.putExtra("ITEM_TOTAL", itemPrice)
                    intent.putExtra("DELIVERY", delivery) // Example delivery fee
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Blue)),
                modifier = Modifier
                    .weight(1f) // Take up remaining space
                    .height(50.dp) // Consistent height
            ) {
                Text(text = "Buy Now", fontSize = 18.sp)
            }

            // "+ Add to Cart" Button
            Button(
                onClick = {
                    onAddToCartClick() // Handle add to cart logic
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGrey)),
                modifier = Modifier
                    .weight(1f) // Take up remaining space
                    .height(50.dp) // Consistent height
            ) {
                Text(
                    text = "+ Add to Cart",
                    fontSize = 18.sp,
                    color = colorResource(R.color.black) // Set text color to black
                )
            }

            // WhatsApp Icon Button
            IconButton(
                onClick = {
                    val phoneNumber = "+6287833395054" // Replace with the phone number you want to contact
                    val message = "Hello, I am interested in your product!"  // Customize message
                    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .size(50.dp) // Fixed size for the WhatsApp icon button
                    .background(
                        color = colorResource(R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .align(Alignment.CenterVertically) // Align vertically with the buttons
            ) {
                Icon(
                    imageVector = Icons.Default.Whatsapp,
                    contentDescription = "Contact us on WhatsApp",
                    tint = Color.Black // Icon color
                )
            }
        }
    }
}

@Composable
fun RatingBar(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Rating:", // Changed to "Rating:"
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = "$rating") // Removed extra line break
    }
}
@Composable
fun ModelSelector(models: List<String>, selectedModeIndex: Int, onModelSelected: (Int) -> Unit) {
    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        itemsIndexed(models) { index, model ->
            val isSelected = index == selectedModeIndex
            Box(modifier = Modifier
                .padding(end = 8.dp)
                .height(48.dp)
                .border(
                    2.dp,
                    if (isSelected) colorResource(R.color.Blue) else Color.Transparent, // Simplified border logic
                    RoundedCornerShape(10.dp)
                )
                .background(
                    colorResource(R.color.lightGrey),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onModelSelected(index) }
                .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = model,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) colorResource(R.color.Blue) else Color.Black, // Simplified color logic
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ImageThumbnail(
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) colorResource(R.color.Blue) else colorResource(R.color.transparent)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(55.dp)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp)) // Only change the border color
            .background(
                colorResource(R.color.lightGrey),
                shape = RoundedCornerShape(10.dp)
            ) // Keep the background light grey
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}