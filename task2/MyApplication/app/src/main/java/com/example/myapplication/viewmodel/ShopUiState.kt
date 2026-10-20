package com.example.myapplication.viewmodel

import com.example.myapplication.model.CartItem
import com.example.myapplication.model.Product
import com.example.myapplication.model.ProductCategory
import com.example.myapplication.model.ShopCatalog

data class ShopUiState(
    val products: List<Product> = ShopCatalog.products,
    val query: String = "",
    val category: ProductCategory? = null,
    val favoriteIds: Set<Long> = emptySet(),
    val favoritesOnly: Boolean = false,
    val cart: Map<Long, Int> = emptyMap()
) {
    val visibleProducts: List<Product>
        get() = products.filter { product ->
            (category == null || product.category == category) &&
                (!favoritesOnly || product.id in favoriteIds) &&
                "${product.name} ${product.description}".contains(query.trim(), ignoreCase = true)
        }

    val cartItems: List<CartItem>
        get() = products.mapNotNull { product ->
            cart[product.id]?.takeIf { it > 0 }?.let { CartItem(product, it) }
        }

    val cartCount: Int get() = cartItems.sumOf { it.quantity }
    val cartTotal: Long get() = cartItems.sumOf { it.subtotal }
}
