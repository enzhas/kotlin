package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.ui.components.*
import com.example.myapplication.viewmodel.ShopViewModel

@Composable
fun ShopRoute(viewModel: ShopViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    fun catalog() { nav.navigate("catalog") { popUpTo("catalog"); launchSingleTop = true } }
    NavHost(navController = nav, startDestination = "catalog") {
        for (route in listOf("catalog", "cart")) {
            composable(route) {
                ShopScreen(state, viewModel::updateQuery, viewModel::selectCategory,
                    viewModel::showFavorites, viewModel::toggleFavorite, viewModel::addToCart,
                    viewModel::decreaseQuantity, viewModel::removeFromCart,
                    showCart = route == "cart", onCatalog = { catalog() },
                    onCart = { nav.navigate("cart") { launchSingleTop = true } },
                    onDetails = { nav.navigate("product/$it") },
                    onCheckout = { nav.navigate("checkout") },
                    onOrders = { nav.navigate("orders") { launchSingleTop = true } })
            }
        }
        composable("product/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { entry ->
            val product = state.products.find { it.id == entry.arguments?.getLong("id") }
            ShopPage("О товаре", { nav.popBackStack() }) {
                if (product == null) Text("Товар не найден") else {
                    ProductArtwork(product, Modifier.fillMaxWidth().height(220.dp))
                    Text(product.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(product.description, style = MaterialTheme.typography.titleMedium)
                    Text(priceText(product.price), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    Text("Учебный каталог • Доставка по Алматы • Оплата при получении")
                    Text("Проверьте модель, комплектацию и стоимость перед добавлением в корзину. Избранное поможет сохранить товары для сравнения.")
                    OutlinedButton(onClick = { viewModel.toggleFavorite(product.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text(if (product.id in state.favoriteIds) "Убрать из избранного" else "В избранное")
                    }
                    Button(onClick = { viewModel.addToCart(product.id) }, enabled = (state.cart[product.id] ?: 0) < 99,
                        modifier = Modifier.fillMaxWidth().testTag("detail_add")) {
                        Text("В корзину · ${state.cart[product.id] ?: 0} шт.")
                    }
                    TextButton(onClick = { nav.navigate("cart") { popUpTo("catalog") } }, modifier = Modifier.testTag("detail_cart")) { Text("Перейти в корзину") }
                }
            }
        }
        composable("checkout") {
            var name by rememberSaveable { mutableStateOf("") }
            var phone by rememberSaveable { mutableStateOf("") }
            var address by rememberSaveable { mutableStateOf("") }
            var attempted by rememberSaveable { mutableStateOf(false) }
            val valid = remember(name, phone, address) { ShopViewModel.validDelivery(name, phone, address) }
            ShopPage("Оформление заказа", { nav.popBackStack() }) {
                Text("Доставка по Алматы", style = MaterialTheme.typography.headlineMedium)
                Text("Демо-заказ: данные никуда не отправляются. Оплата при получении.")
                OutlinedTextField(name, { name = it }, label = { Text("Имя получателя") }, singleLine = true,
                    isError = attempted && name.trim().length < 2, modifier = Modifier.fillMaxWidth().testTag("delivery_name"))
                OutlinedTextField(phone, { phone = it }, label = { Text("Телефон (10–15 цифр)") }, singleLine = true,
                    isError = attempted && phone.count(Char::isDigit) !in 10..15,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("delivery_phone"))
                OutlinedTextField(address, { address = it }, label = { Text("Улица, дом, квартира") },
                    isError = attempted && address.trim().length < 8, modifier = Modifier.fillMaxWidth().testTag("delivery_address"))
                if (attempted && !valid) Text("Укажите имя (от 2 символов), телефон и адрес (от 8 символов).", color = MaterialTheme.colorScheme.error)
                Text("${state.cartCount} шт. · ${priceText(state.cartTotal)}", style = MaterialTheme.typography.titleLarge)
                Text("Доставка бесплатно • Оплата при получении")
                Button(onClick = {
                    attempted = true
                    if (viewModel.placeOrder(name, phone, address)) nav.navigate("orders") { popUpTo("catalog") }
                }, enabled = state.cartCount > 0, modifier = Modifier.fillMaxWidth().testTag("place_order")) { Text("Подтвердить демо-заказ") }
            }
        }
        composable("orders") {
            ShopPage("Мои заказы", { catalog() }) {
                Text("История демо-заказов", style = MaterialTheme.typography.headlineMedium)
                if (state.orderTotals.isEmpty()) Text("Заказов пока нет. Выберите товары в каталоге и оформите доставку.")
                state.orderTotals.withIndex().reversed().forEach { (index, total) ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Заказ №${index + 1}", style = MaterialTheme.typography.titleLarge)
                            Text("Принят • Демо • Оплата при получении")
                            Text(priceText(total), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                Button(onClick = { catalog() }, modifier = Modifier.fillMaxWidth().testTag("orders_catalog")) { Text("В каталог") }
            }
        }
    }
}

@Composable
private fun ShopPage(title: String, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Scaffold(topBar = {
        Surface { Row(Modifier.fillMaxWidth().statusBarsPadding().padding(12.dp)) {
            TextButton(onClick = onBack) { Text("Назад") }
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(12.dp))
        } }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding()
            .verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}
