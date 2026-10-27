package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShopViewModel(private val savedState: SavedStateHandle = SavedStateHandle()) : ViewModel() {
    private val _uiState = MutableStateFlow(ShopUiState(
        query = savedState["query"] ?: "",
        category = savedState.get<String>("category")?.let(ProductCategory::valueOf),
        favoritesOnly = savedState["favoritesOnly"] ?: false,
        favoriteIds = savedState.get<LongArray>("favorites")?.toSet() ?: emptySet(),
        cart = savedState.get<ArrayList<String>>("cart")?.associate {
            val parts = it.split(":"); parts[0].toLong() to parts[1].toInt()
        } ?: emptyMap(),
        orderTotals = savedState.get<LongArray>("orders")?.toList() ?: emptyList()
    ))

    private fun persist() {
        val state = _uiState.value
        savedState["query"] = state.query
        savedState["category"] = state.category?.name
        savedState["favoritesOnly"] = state.favoritesOnly
        savedState["favorites"] = state.favoriteIds.toLongArray()
        savedState["cart"] = ArrayList(state.cart.map { "${it.key}:${it.value}" })
        savedState["orders"] = state.orderTotals.toLongArray()
    }

    fun placeOrder(name: String, phone: String, address: String): Boolean {
        if (!validDelivery(name, phone, address) || _uiState.value.cartItems.isEmpty()) return false
        _uiState.update { it.copy(orderTotals = it.orderTotals + it.cartTotal, cart = emptyMap()) }
        persist()
        return true
    }

    companion object {
        fun validDelivery(name: String, phone: String, address: String): Boolean =
            name.trim().length >= 2 && phone.count(Char::isDigit) in 10..15 && address.trim().length >= 8
    }
    val uiState = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        persist()
    }

    fun selectCategory(category: ProductCategory?) {
        _uiState.update { it.copy(category = category) }
        persist()
    }

    fun showFavorites(onlyFavorites: Boolean) {
        _uiState.update { it.copy(favoritesOnly = onlyFavorites) }
        persist()
    }

    fun toggleFavorite(id: Long) {
        _uiState.update { state ->
            if (state.products.none { it.id == id }) state
            else state.copy(favoriteIds = if (id in state.favoriteIds) state.favoriteIds - id else state.favoriteIds + id)
        }
        persist()
    }

    fun addToCart(id: Long) {
        _uiState.update { state ->
            if (state.products.none { it.id == id }) state
            else state.copy(cart = state.cart + (id to ((state.cart[id] ?: 0) + 1).coerceAtMost(99)))
        }
        persist()
    }

    fun decreaseQuantity(id: Long) {
        _uiState.update { state ->
            val quantity = state.cart[id] ?: return@update state
            state.copy(cart = if (quantity <= 1) state.cart - id else state.cart + (id to quantity - 1))
        }
        persist()
    }

    fun removeFromCart(id: Long) {
        _uiState.update { it.copy(cart = it.cart - id) }
        persist()
    }
}
