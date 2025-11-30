package com.ayoo.consumer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ayoo.consumer.R
import com.ayoo.consumer.model.Products
import com.ayoo.consumer.model.StoreInfo
import com.ayoo.consumer.viewmodel.CartViewModel
import com.ayoo.consumer.viewmodel.SearchState
import com.ayoo.consumer.viewmodel.StoreState
import com.ayoo.consumer.viewmodel.StoreViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    stores: List<StoreInfo>,
    storeViewModel: StoreViewModel,
    cartViewModel: CartViewModel
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchState by storeViewModel.searchState.collectAsState()
    val storeState by storeViewModel.storeState.collectAsState()

    val isRefreshing = storeState is StoreState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { storeViewModel.fetchStores() }
    )

    val selectedAddressResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("selectedAddress")
    var currentAddress by rememberSaveable { mutableStateOf("San roque, Iligan city") }

    LaunchedEffect(selectedAddressResult) {
        if (selectedAddressResult != null) {
            currentAddress = selectedAddressResult
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selectedAddress")
        }
    }

    Scaffold(
        bottomBar = { AyooBottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Header(navController, currentAddress)
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                            storeViewModel.search(it)
                        }
                    )
                }

                if (searchQuery.isNotBlank()) {
                    SearchResults(
                        searchState = searchState,
                        navController = navController,
                        cartViewModel = cartViewModel
                    )
                } else {
                    HomeForYouContent(navController = navController, stores = stores)
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// ... Rest of the file remains the same ...
// The AyooBottomNavigationBar function is now public
@Composable
fun AyooBottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            "home" to Icons.Default.Home,
            "cart" to Icons.Default.ShoppingCart,
            "myfind" to Icons.Default.Map,
            "search" to Icons.Default.Search,
            "profile" to Icons.Default.Person
        )
        items.forEach { (route, icon) ->
            val label =
                route.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        navController.graph.startDestinationRoute?.let { startRoute ->
                            popUpTo(startRoute) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("What are you looking for?") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun SearchResults(
    searchState: SearchState,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        when (searchState) {
            is SearchState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is SearchState.Success -> {
                if (searchState.stores.isEmpty() && searchState.products.isEmpty()) {
                    item { Text("No results found.", modifier = Modifier.padding(16.dp)) }
                } else {
                    if (searchState.stores.isNotEmpty()) {
                        item {
                            Text(
                                "Stores",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }
                        items(searchState.stores) { store -> StoreListItem(navController, store) }
                    }
                    if (searchState.products.isNotEmpty()) {
                        item {
                            Text(
                                "Products",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                        }
                        items(searchState.products) { product ->
                            ProductListItem(
                                product,
                                cartViewModel
                            )
                        }
                    }
                }
            }

            is SearchState.Error -> {
                item { Text(searchState.message, modifier = Modifier.padding(16.dp)) }
            }

            is SearchState.Idle -> {}
        }
    }
}

@Composable
fun ProductListItem(product: Products, cartViewModel: CartViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name ?: "Unnamed Product", fontWeight = FontWeight.Bold)
                Text(
                    "₱${product.price}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(onClick = { cartViewModel.addItem(product) }) {
                Text("+ Add")
            }
        }
    }
}

@Composable
private fun HomeForYouContent(navController: NavController, stores: List<StoreInfo>) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Home", "For you")

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> HomePage(navController = navController, stores = stores)
                1 -> ForYouPage()
            }
        }
    }
}

@Composable
private fun HomePage(navController: NavController, stores: List<StoreInfo>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { CategoriesSection() }
        item { LimitedDeals() }

        item {
            Text(
                "Featured Stores",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        items(stores) { store ->
            StoreListItem(navController, store)
        }
    }
}

@Composable
private fun ForYouPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("For You content goes here")
    }
}

@Composable
private fun Header(navController: NavController, address: String) {
    Card(
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val encodedAddress = URLEncoder.encode(address, "UTF-8")
                    navController.navigate("address_selection/$encodedAddress")
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Deliver to", fontSize = 12.sp)
                Text(
                    text = address,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ayoo_logo),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun ShopCategory() {
    Column {
        Text(
            "Shop Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val categories = listOf("Food", "Grocery", "Pharmacy", "Pabili")
            categories.forEach { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ayoo_logo),
                            contentDescription = category,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(category, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun CategoriesSection() {
    val categories = listOf("Fast Food", "Filipino Meals", "Desserts", "Halal", "Pizza", "Ice Cream")
    Column {
        Text(
            "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(categories) { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ayoo_logo),
                        contentDescription = category,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(category, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun LimitedDeals() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Limited Deals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("See All", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
        }
    }
}

@Composable
private fun StoreListItem(navController: NavController, store: StoreInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("store/${store.objectId}") },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ayoo_logo),
                contentDescription = store.storeName ?: "Store logo",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(store.storeName ?: "Unnamed Store", fontWeight = FontWeight.Bold)
                Text(store.storeType ?: "Unknown", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
