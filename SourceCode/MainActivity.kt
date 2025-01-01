package com.example.el_clothstoree.Activity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.el_clothstoree.Model.CategoryModel
import com.example.el_clothstoree.Model.ItemsModel
import com.example.el_clothstoree.Model.SliderModel
import com.example.el_clothstoree.R
import com.example.el_clothstoree.ViewModel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityScreen {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }
}

@Composable
fun MainActivityScreen(onCartClick: () -> Unit) {
    val viewModel: MainViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application))
    val context = LocalContext.current
    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val recommended = remember { mutableStateListOf<ItemsModel>() }
    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showRecommendedLoading by remember { mutableStateOf(true) }
    var activeMenuItem by remember { mutableStateOf("Home") } // Track the active menu item

    // Banner, Category, and Recommended Data loading
    LaunchedEffect(Unit) {
        viewModel.loadBanners()
        viewModel.banners.observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategory()
        viewModel.categories.observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadRecommended()
        viewModel.recommended.observeForever {
            recommended.clear()
            recommended.addAll(it)
            showRecommendedLoading = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        val (scrollList) = createRefs()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(scrollList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .systemBarsPadding()
        ) {

            // Header Section (App Name + Search Bar)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                    .clickable { // Adding click functionality
                            val intent = Intent(context, SearchActivity::class.java)
                            context.startActivity(intent)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search bar spanning the width
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(25.dp))
                                .fillMaxWidth()
                                .height(50.dp)  // Adjusted height to 50.dp
                                .padding(horizontal = 16.dp),  // Horizontal padding
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Search for products...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Banner Section
            item {
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Banners(banners)
                }
            }

            // Category Section
            item {
                SectionTitle("Categories", "")
            }
            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(categories)
                }
            }

            // Recommendation Section
            item {
                SectionTitle("Recommendation", "See All", onClick = {
                    val intent = Intent(context, ProdukActivity::class.java)
                    context.startActivity(intent)
                })
            }
            item {
                if (showRecommendedLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(recommended)
                }
            }

            // Spacer for better bottom padding
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Bottom Navigation Menu
        Box(modifier = Modifier.fillMaxSize()) {
            BottomMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                activeMenuItem = activeMenuItem,  // Pass active menu item
                onHomeClick = {
                    activeMenuItem = "Home"
                },
                onCartClick = {
                    activeMenuItem = "Cart"
                    onCartClick()  // Trigger cart action
                },
                onFavoriteClick = {
                    activeMenuItem = "Favorite"
                    val intent = Intent(context, FavoriteActivity::class.java)
                    context.startActivity(intent)
                },
                onProfileClick = {
                    activeMenuItem = "Profile"
                    // Navigate to Profile Activity
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banners(banners: List<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    pagerState: PagerState = remember { PagerState() },
    banners: List<SliderModel>
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(count = banners.size, state = pagerState) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banners[page].url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .height(180.dp) // Adjust height for compact view
                    .clip(RoundedCornerShape(10.dp)) // Rounded corners for the banner
            )
        }

        // Indicator for page change
        DotIndicator(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banners.size,
            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.currentPage,
            dotSize = 8.dp
        )
    }
}

@Composable
fun CategoryList(categories: SnapshotStateList<CategoryModel>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(categories.size) { index ->
            CategoryItem(
                item = categories[index],
                isSelected = selectedIndex == index,
                onItemClick = {
                    selectedIndex = index
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(context, ListItemsActivity::class.java).apply {
                            putExtra("id", categories[index].id.toString())
                            putExtra("title", categories[index].title)
                        }
                        startActivity(context, intent, null)
                    }, 500) // Adjust delay for smoother transition
                }
            )
        }
    }
}

@Composable
fun CategoryItem(item: CategoryModel, isSelected: Boolean, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .background(
                color = if (isSelected) Color(0xFF3F51B5) else Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(30.dp) // Adjusted height for better proportionality
            .fillMaxWidth(0.3f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.title,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// Bottom Navigation Menu
@Composable
fun BottomMenu(
    modifier: Modifier,
    activeMenuItem: String, // Track the active menu item
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(Color(0xFF3F51B5), shape = RoundedCornerShape(20.dp)) // Rounded corners for menu
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomMenuItem(
            icon = Icons.Filled.Home,
            text = "Home",
            isActive = activeMenuItem == "Home",  // Check if this is the active item
            onItemClick = onHomeClick
        )

        BottomMenuItem(
            icon = Icons.Filled.ShoppingCart,
            text = "Cart",
            isActive = activeMenuItem == "Cart",
            onItemClick = onCartClick
        )

        BottomMenuItem(
            icon = Icons.Filled.Favorite,
            text = "Favorite",
            isActive = activeMenuItem == "Favorite",
            onItemClick = onFavoriteClick
        )

        BottomMenuItem(
            icon = Icons.Filled.Person,
            text = "Profile",
            isActive = activeMenuItem == "Profile",
            onItemClick = onProfileClick
        )
    }
}

@Composable
fun BottomMenuItem(
    icon: ImageVector,
    text: String,
    isActive: Boolean,  // Check if this menu item is active
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(60.dp)
            .clickable { onItemClick() }
            .padding(8.dp)
            .background(
                color = if (isActive) Color(0xFF3F51B5) else Color.Transparent, // Highlight active item
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = text,
            tint = if (isActive) Color.White else Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = if (isActive) Color.White else Color.White,
            fontSize = 10.sp
        )
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color(0xFF3A7DFF), // Warna biru muda
    unSelectedColor: Color = Color(0xFFBDBDBD), // Warna abu-abu terang
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )

}

@Composable
fun SectionTitle(title: String, actionText: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = Color(0xFF3F51B5),
            modifier = Modifier.clickable { onClick() }
        )
    }
}