import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val store = Store()

    println("==========================")
    println("      ONLINE STORE")
    println("==========================")

    print("Enter your name: ")
    val name: String = readlnOrNull() ?: return@runBlocking

    println()
    println("Choose customer type:")
    println("1. Regular")
    println("2. Premium")

    print("Choice: ")
    val customerType: Int =
        (readlnOrNull() ?: return@runBlocking).toIntOrNull() ?: 1

    // Polymorphism
    val customer: Customer =
        if (customerType == 2) {
            PremiumCustomer(name)
        } else {
            Customer(name)
        }

    println()
    customer.showCustomerType()

    var running: Boolean = true

    // Loop
    while (running) {

        println(
            """
            
            ===== ONLINE STORE =====
            1. Show all products
            2. Show categories
            3. Filter by category
            4. Search product
            5. Add product to cart
            6. Show cart
            7. Checkout
            8. Store statistics
            0. Exit
            ========================
            """.trimIndent()
        )

        print("Choose option: ")

        val choice: Int =
            (readlnOrNull() ?: break).toIntOrNull() ?: -1

        // Condition
        when (choice) {

            1 -> store.showProducts()

            2 -> store.showCategories()

            3 -> store.filterByCategory()

            4 -> store.searchProduct()

            5 -> store.addToCart()

            6 -> store.showCart()

            7 -> {

                // Calling suspend function
                val result =
                    store.checkout(customer)

                when (result) {

                    is OrderResult.Success -> {
                        println()
                        println("Order completed successfully!")
                        println(
                            "Final price: $${result.totalPrice}"
                        )
                    }

                    is OrderResult.Error -> {
                        println(
                            "Order error: ${result.message}"
                        )
                    }
                }
            }

            8 -> store.showStatistics()

            0 -> {
                running = false
                println("Thank you for using Online Store!")
            }

            else -> {
                println("Invalid option.")
            }
        }
    }
}
