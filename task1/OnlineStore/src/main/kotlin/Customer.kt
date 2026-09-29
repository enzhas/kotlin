interface Discountable {
    fun calculateDiscount(price: Double): Double
}

open class Customer(
    val name: String
) : Discountable {

    override fun calculateDiscount(price: Double): Double {
        return price
    }

    open fun showCustomerType() {
        println("$name is a regular customer.")
    }
}

class PremiumCustomer(
    name: String
) : Customer(name) {

    override fun calculateDiscount(price: Double): Double {
        return price * 0.9
    }

    override fun showCustomerType() {
        println("$name is a premium customer with a 10% discount.")
    }
}