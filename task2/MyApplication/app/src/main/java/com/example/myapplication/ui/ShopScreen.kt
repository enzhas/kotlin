package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.ProductCategory
import com.example.myapplication.ui.components.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.ShopUiState
import com.example.myapplication.viewmodel.ShopViewModel

@Composable
fun ShopRoute(viewModel: ShopViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ShopScreen(state, viewModel::updateQuery, viewModel::selectCategory,
        viewModel::showFavorites, viewModel::toggleFavorite, viewModel::addToCart,
        viewModel::decreaseQuantity, viewModel::removeFromCart)
}

@Composable
fun ShopScreen(
    state: ShopUiState,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (ProductCategory?) -> Unit,
    onFavoritesOnly: (Boolean) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onAddToCart: (Long) -> Unit,
    onDecrease: (Long) -> Unit,
    onRemove: (Long) -> Unit
) {
    // Navigation is small UI state, saved separately from the ViewModel's shop data.
    var showCart by rememberSaveable { mutableStateOf(false) }
    val catalogScroll = rememberLazyListState()
    val cartScroll = rememberLazyListState()
    val visibleProducts = state.visibleProducts

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ShopHeader() },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = !showCart, onClick = { showCart = false },
                    icon = { ShopIcon(R.drawable.ic_catalog, null) },
                    label = { Text(stringResource(R.string.catalog)) },
                    modifier = Modifier.testTag("nav_catalog")
                )
                NavigationBarItem(
                    selected = showCart, onClick = { showCart = true },
                    icon = {
                        BadgedBox(badge = { if (state.cartCount > 0) Badge { Text(state.cartCount.toString()) } }) {
                            ShopIcon(R.drawable.ic_cart, null)
                        }
                    },
                    label = { Text(stringResource(R.string.cart)) },
                    modifier = Modifier.testTag("nav_cart")
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding).imePadding(),
            contentAlignment = Alignment.TopCenter) {
            LazyColumn(
                state = if (showCart) cartScroll else catalogScroll,
                modifier = Modifier.widthIn(max = 720.dp).fillMaxSize().testTag("shop_list"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (showCart) {
                    item(key = "cart_header") {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.cart_heading), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text(stringResource(R.string.cart_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    if (state.cartItems.isEmpty()) {
                        item(key = "cart_empty") { EmptyShopState(isCart = true, onOpenCatalog = { showCart = false }) }
                    } else {
                        items(state.cartItems, key = { "cart_${it.product.id}" }) { item ->
                            CartItemCard(item, onDecrease = { onDecrease(item.product.id) },
                                onIncrease = { onAddToCart(item.product.id) }, onRemove = { onRemove(item.product.id) })
                        }
                        item(key = "cart_summary") {
                            CartSummary(state.cartCount, state.cartTotal, onContinueShopping = { showCart = false })
                        }
                    }
                } else {
                    item(key = "banner") { CatalogBanner() }
                    item(key = "search") { ProductSearchField(state.query, onQueryChange) }
                    item(key = "categories") { CategoryFilterRow(state.category, onCategoryChange) }
                    item(key = "favorites") { FavoritesFilter(state.favoritesOnly, onFavoritesOnly) }
                    item(key = "heading") {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.products_heading), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(stringResource(R.string.results_count, visibleProducts.size),
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    if (visibleProducts.isEmpty()) {
                        item(key = "no_results") { EmptyShopState(isCart = false) }
                    } else {
                        items(visibleProducts, key = { "product_${it.id}" }) { product ->
                            ProductCard(product, isFavorite = product.id in state.favoriteIds,
                                cartQuantity = state.cart[product.id] ?: 0,
                                onFavorite = { onToggleFavorite(product.id) }, onAddToCart = { onAddToCart(product.id) })
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun ShopPreview() {
    MyApplicationTheme {
        ShopScreen(ShopUiState(), {}, {}, {}, {}, {}, {}, {})
    }
}
