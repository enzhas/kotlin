package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShopViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun selectCategory(category: ProductCategory?) {
        _uiState.update { it.copy(category = category) }
    }

    fun showFavorites(onlyFavorites: Boolean) {
        _uiState.update { it.copy(favoritesOnly = onlyFavorites) }
    }

    fun toggleFavorite(id: Long) {
        _uiState.update { state ->
            if (state.products.none { it.id == id }) state
            else state.copy(favoriteIds = if (id in state.favoriteIds) state.favoriteIds - id else state.favoriteIds + id)
        }
    }

    fun addToCart(id: Long) {
        _uiState.update { state ->
            if (state.products.none { it.id == id }) state
            else state.copy(cart = state.cart + (id to ((state.cart[id] ?: 0) + 1).coerceAtMost(99)))
        }
    }

    fun decreaseQuantity(id: Long) {
        _uiState.update { state ->
            val quantity = state.cart[id] ?: return@update state
            state.copy(cart = if (quantity <= 1) state.cart - id else state.cart + (id to quantity - 1))
        }
    }

    fun removeFromCart(id: Long) {
        _uiState.update { it.copy(cart = it.cart - id) }
    }
}
