package com.example.myapplication.model

// Fixed teaching data; these prices are not a live Sulpak offer.
object ShopCatalog {
    val products = listOf(
        Product(1, "Samsung Galaxy A55", "128 ГБ · тёмно-синий", 159_990, ProductCategory.SMARTPHONES),
        Product(2, "Apple iPhone 15", "128 ГБ · чёрный", 349_990, ProductCategory.SMARTPHONES),
        Product(3, "ASUS Vivobook 15", "15,6″ · 16 ГБ / SSD 512 ГБ", 279_990, ProductCategory.LAPTOPS),
        Product(4, "Lenovo IdeaPad Slim 3", "15,6″ · 8 ГБ / SSD 512 ГБ", 229_990, ProductCategory.LAPTOPS),
        Product(5, "JBL Tune 520BT", "Беспроводные наушники · синий", 24_990, ProductCategory.AUDIO),
        Product(6, "Sony WH-CH520", "Беспроводные наушники · чёрный", 29_990, ProductCategory.AUDIO),
        Product(7, "Dyson V8", "Беспроводной пылесос", 189_990, ProductCategory.HOME),
        Product(8, "Samsung EcoBubble", "Стиральная машина · 8 кг", 219_990, ProductCategory.HOME)
    )
}
