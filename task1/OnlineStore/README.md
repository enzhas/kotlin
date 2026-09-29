# Online Store Console Application

A simple Kotlin/JVM console application that simulates an online store.

The entry point is `main()` in `src/main/kotlin/Main.kt`.
This project runs on the JVM and does not require an Android SDK or emulator.

The application allows users to browse products, search products,
filter products by category, add products to a shopping cart,
checkout orders, and view basic store statistics.

## Technologies

- Kotlin
- Kotlin/JVM
- Gradle
- Kotlin Coroutines

## How to Run

### Terminal

From the `task1/OnlineStore` directory, run:

```sh
./gradlew run --console=plain
```

On Windows, use `gradlew.bat run --console=plain`.

To compile and package the application, run `./gradlew build`.
Gradle downloads the required Java toolchain automatically.

### IntelliJ IDEA

1. Clone the repository.
2. Open the `task1/OnlineStore` folder in IntelliJ IDEA as a Gradle project.
3. Wait for Gradle to finish loading dependencies.
4. Open `Main.kt`.
5. Run the `main()` function.

The application will start in the console.

## Main Features

- Display all products
- Display product categories
- Filter products by category
- Search products
- Add products to shopping cart
- Checkout
- Regular and Premium customers
- Premium customer discount
- Store statistics

## Homework Requirements

### Variables and Data Types

The application uses different Kotlin data types such as:

- String
- Int
- Double
- Boolean

They are used for product names, IDs, prices, stock quantities,
customer information, and application state.

### Conditions and Loops

`if` and `when` are used for conditions.

A `while` loop is used for the main console menu.

### List, Set and Map

`List` is used for the product catalog and shopping cart.

`Set` stores unique product categories.

`Map` allows products to be found using their ID.

### Collection Operations

`filter` is used to search and filter products.

`map` is used to transform the product collection.

`reduce` is used to calculate the total price of the shopping cart.

### Functions

The project contains functions such as:

- `showProducts()`
- `searchProduct()`
- `addToCart()`
- `showCart()`
- `checkout()`

### Higher-Order Functions and Lambdas

`findProducts()` accepts a function as an argument.

Example:

```kotlin
findProducts { product ->
    product.price < 100
}
```

### Classes and Objects

`Store` manages the catalog and cart. `main()` creates a `Store` object and
a customer object for the current session.

### Inheritance, Interfaces and Polymorphism

`Customer` implements the `Discountable` interface. `PremiumCustomer` extends
`Customer` and overrides its discount and customer-description functions.
The program uses a `Customer` reference for either customer type, so checkout
automatically applies the correct pricing: regular customers pay the full total,
while premium customers receive a 10% discount.

### Data Class

`Product` is a data class containing an ID, name, category, price and stock count.
Adding a product to the cart reserves one unit by decreasing its stock.

### Sealed Class

`OrderResult` is a sealed class with `Success` and `Error` variants. Checkout
returns an error for an empty cart or a success containing the final price.
The menu handles both variants with `when`.

### Suspend Function and Coroutine

`main()` uses `runBlocking` to create a coroutine for the console session.
The suspend function `Store.checkout()` calls `delay(1500)` to simulate order
processing before applying the customer's discount and clearing the cart.

## Example Session

1. Enter your name and select `2` for a premium customer.
2. Select `5` to add a product, then enter product ID `4` (T-Shirt, $25).
3. Select `6` to see the cart and its $25 total.
4. Select `7` to checkout. The final price is $22.5 after the 10% discount.
5. Select `6` again to confirm the cart is empty, then `0` to exit.

The catalog and cart are stored in memory and reset when the application restarts.
