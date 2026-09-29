import kotlinx.coroutines.delay

class Store {

    // List
    private val products = mutableListOf(
        Product(1, "Laptop", "Electronics", 1200.0, 5),
        Product(2, "Smartphone", "Electronics", 800.0, 10),
        Product(3, "Headphones", "Electronics", 150.0, 15),
        Product(4, "T-Shirt", "Clothing", 25.0, 20),
        Product(5, "Jeans", "Clothing", 60.0, 12),
        Product(6, "Kotlin Book", "Books", 40.0, 8),
        Product(7, "Gaming Mouse", "Electronics", 70.0, 9),
        Product(8, "Backpack", "Accessories", 50.0, 7)
    )

    private val cart = mutableListOf<Product>()

    // Set
    private val categories: Set<String> =
        products.map { it.category }.toSet()

    // Map
    private val productMap: Map<Int, Product> =
        products.associateBy { it.id }

    fun showProducts() {
        println("\n--- PRODUCTS ---")

        products.forEach { product ->
            println(
                "${product.id}. ${product.name} | " +
                        "${product.category} | " +
                        "$${product.price} | " +
                        "Stock: ${product.stock}"
            )
        }
    }

    fun showCategories() {
        println("\n--- CATEGORIES ---")

        categories.forEach {
            println("- $it")
        }
    }

    fun filterByCategory() {
        showCategories()

        print("\nEnter category: ")
        val category = readlnOrNull() ?: return

        // filter operation
        val filteredProducts = products.filter {
            it.category.equals(category, ignoreCase = true)
        }

        if (filteredProducts.isEmpty()) {
            println("No products found.")
        } else {
            filteredProducts.forEach {
                println("${it.id}. ${it.name} - $${it.price}")
            }
        }
    }

    fun searchProduct() {
        print("Enter product name: ")
        val query = readlnOrNull() ?: return

        val results = products.filter {
            it.name.contains(query, ignoreCase = true)
        }

        if (results.isEmpty()) {
            println("Product not found.")
            return
        }

        results.forEach {
            println(
                "${it.id}. ${it.name} | " +
                        "${it.category} | $${it.price}"
            )
        }
    }

    fun addToCart() {
        showProducts()

        print("\nEnter product ID: ")
        val id = (readlnOrNull() ?: return).toIntOrNull()

        if (id == null) {
            println("Invalid ID.")
            return
        }

        // Using Map to find product by ID
        val product = productMap[id]

        if (product == null) {
            println("Product does not exist.")
            return
        }

        if (product.stock <= 0) {
            println("Product is out of stock.")
            return
        }

        cart.add(product)
        product.stock--

        println("${product.name} added to cart.")
    }

    fun showCart() {
        println("\n--- YOUR CART ---")

        if (cart.isEmpty()) {
            println("Cart is empty.")
            return
        }

        cart.forEachIndexed { index, product ->
            println(
                "${index + 1}. ${product.name} - $${product.price}"
            )
        }

        val total = calculateCartTotal()

        println("----------------")
        println("Total: $$total")
    }

    private fun calculateCartTotal(): Double {

        if (cart.isEmpty()) {
            return 0.0
        }

        // map + reduce
        return cart
            .map { it.price }
            .reduce { total, price ->
                total + price
            }
    }

    // Higher-order function
    fun findProducts(
        condition: (Product) -> Boolean
    ): List<Product> {
        return products.filter(condition)
    }

    fun showCheapProducts() {
        println("\n--- PRODUCTS UNDER $100 ---")

        // Lambda passed to higher-order function
        val cheapProducts = findProducts { product ->
            product.price < 100
        }

        cheapProducts.forEach {
            println("${it.name} - $${it.price}")
        }
    }

    fun showStatistics() {
        println("\n--- STORE STATISTICS ---")

        println("Number of products: ${products.size}")
        println("Number of categories: ${categories.size}")

        // map
        val productNames = products.map {
            it.name
        }

        println("Products:")
        println(productNames.joinToString(", "))

        val availableProducts = products.filter {
            it.stock > 0
        }

        println("Available products: ${availableProducts.size}")

        showCheapProducts()
    }

    // Suspend function
    suspend fun checkout(
        customer: Customer
    ): OrderResult {

        if (cart.isEmpty()) {
            return OrderResult.Error(
                "Cannot checkout because cart is empty."
            )
        }

        println("\nProcessing order...")

        // Simulate network/database operation
        delay(1500)

        val originalPrice = calculateCartTotal()

        // Polymorphism
        val finalPrice =
            customer.calculateDiscount(originalPrice)

        cart.clear()

        return OrderResult.Success(finalPrice)
    }
}
