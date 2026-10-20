package com.example.myapplication

import com.example.myapplication.model.ProductCategory
import com.example.myapplication.viewmodel.ShopViewModel
import org.junit.Assert.*
import org.junit.Test

class ShopViewModelTest {
    @Test
    fun searchCategoryAndFavoritesWorkTogether() {
        val model = ShopViewModel()
        model.updateQuery("  SAMSUNG  ")
        assertEquals(listOf(1L, 8L), model.uiState.value.visibleProducts.map { it.id })
        model.selectCategory(ProductCategory.SMARTPHONES)
        assertEquals(listOf(1L), model.uiState.value.visibleProducts.map { it.id })
        model.showFavorites(true)
        assertTrue(model.uiState.value.visibleProducts.isEmpty())
        model.toggleFavorite(1)
        assertEquals(listOf(1L), model.uiState.value.visibleProducts.map { it.id })
        model.toggleFavorite(1)
        assertTrue(model.uiState.value.visibleProducts.isEmpty())
        assertEquals(8, model.uiState.value.products.size)
    }

    @Test
    fun cartCalculatesQuantitiesSubtotalsAndTotal() {
        val model = ShopViewModel()
        model.addToCart(1)
        model.addToCart(1)
        model.addToCart(5)
        val state = model.uiState.value
        assertEquals(3, state.cartCount)
        assertEquals(2, state.cartItems.first().quantity)
        assertEquals(319_980L, state.cartItems.first().subtotal)
        assertEquals(344_970L, state.cartTotal)
        model.decreaseQuantity(1)
        assertEquals(184_980L, model.uiState.value.cartTotal)
        model.removeFromCart(5)
        assertEquals(159_990L, model.uiState.value.cartTotal)
        model.decreaseQuantity(1)
        assertEquals(0, model.uiState.value.cartCount)
        assertEquals(0L, model.uiState.value.cartTotal)
    }

    @Test
    fun invalidIdsAndEmptyCartOperationsAreIgnored() {
        val model = ShopViewModel()
        model.addToCart(-1)
        model.toggleFavorite(-1)
        model.decreaseQuantity(1)
        model.removeFromCart(1)
        assertTrue(model.uiState.value.cart.isEmpty())
        assertTrue(model.uiState.value.favoriteIds.isEmpty())
    }

    @Test
    fun quantityHasAnUpperLimitAndFiltersDoNotChangeCart() {
        val model = ShopViewModel()
        repeat(110) { model.addToCart(1) }
        assertEquals(99, model.uiState.value.cartCount)
        model.updateQuery("not a product")
        model.selectCategory(ProductCategory.HOME)
        model.showFavorites(true)
        assertTrue(model.uiState.value.visibleProducts.isEmpty())
        assertEquals(99, model.uiState.value.cartCount)
        assertEquals(159_990L * 99, model.uiState.value.cartTotal)
    }
}
