package com.example.myapplication.model

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Long,
    val category: ProductCategory
)

enum class ProductCategory {
    SMARTPHONES, LAPTOPS, AUDIO, HOME
}

data class CartItem(val product: Product, val quantity: Int) {
    val subtotal: Long get() = product.price * quantity
}
