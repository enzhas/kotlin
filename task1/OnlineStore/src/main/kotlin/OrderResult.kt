sealed class OrderResult {

    data class Success(
        val totalPrice: Double
    ) : OrderResult()

    data class Error(
        val message: String
    ) : OrderResult()
}