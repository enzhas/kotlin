package com.example.myapplication

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ShopScreenTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun filtersFavoritesAndCartSurviveActivityRecreation() {
        scrollTo("product_search").performTextInput("samsung")
        closeSoftKeyboard()
        scrollTo("category_SMARTPHONES").performClick()
        scrollTo("favorite_1").performClick()
        scrollTo("favorites_filter").performClick()
        scrollTo("add_to_cart_1").performClick()
        compose.onNodeWithTag("nav_cart").performClick()
        scrollTo("increase_1").performClick()

        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("nav_cart").assertIsSelected()
        scrollTo("quantity_1").assertTextEquals("2")
        scrollTo("cart_total").assertTextEquals("319\u00a0980 ₸")
        compose.onNodeWithTag("nav_catalog").performClick()
        scrollTo("product_search").assertTextContains("samsung")
        scrollTo("category_SMARTPHONES").assertIsSelected()
        scrollTo("favorites_filter").assertIsOn()
        scrollTo("favorite_1").assertIsOn()

        compose.onNodeWithTag("nav_cart").performClick()
        scrollTo("decrease_1").performClick()
        scrollTo("quantity_1").assertTextEquals("1")
        scrollTo("remove_1").performClick()
        compose.onNodeWithText("Ваша корзина пока пуста").assertExists()
    }

    @Test
    fun checkoutValidationNavigationAndScreenshots() {
        compose.onNodeWithTag("shop_list").performScrollToIndex(0)
        saveScreenshot("01-catalog.png")
        scrollTo("details_1").performClick()
        compose.onNodeWithText("Samsung Galaxy A55").assertExists()
        saveScreenshot("02-product-details.png")
        compose.onNodeWithTag("detail_add").performScrollTo().performClick()
        compose.onNodeWithTag("detail_cart").performScrollTo().performClick()
        scrollTo("quantity_1").assertTextEquals("1")
        saveScreenshot("03-cart.png")
        scrollTo("checkout").performClick()
        compose.onNodeWithTag("place_order").performScrollTo().performClick()
        compose.onNodeWithText("Укажите имя (от 2 символов), телефон и адрес (от 8 символов).").assertExists()
        compose.onNodeWithTag("delivery_name").performScrollTo().performTextInput("Demo Customer")
        compose.onNodeWithTag("delivery_phone").performScrollTo().performTextInput("77001234567")
        compose.onNodeWithTag("delivery_address").performScrollTo().performTextInput("Almaty, Abay 10, apt 5")
        closeSoftKeyboard()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("delivery_name").assertTextContains("Demo Customer")
        saveScreenshot("04-checkout.png")
        compose.onNodeWithTag("place_order").performScrollTo().performClick()
        compose.onNodeWithText("Заказ №1").assertExists()
        saveScreenshot("05-orders.png")
        compose.onNodeWithTag("orders_catalog").performScrollTo().performClick()
        compose.onNodeWithTag("nav_cart").performClick()
        compose.onNodeWithText("Ваша корзина пока пуста").assertExists()
    }

    private fun scrollTo(tag: String): SemanticsNodeInteraction {
        compose.onNodeWithTag("shop_list").performScrollToNode(hasTestTag(tag))
        return compose.onNodeWithTag(tag)
    }

    private fun saveScreenshot(name: String) {
        closeSoftKeyboard()
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val directory = File(instrumentation.targetContext.getExternalFilesDir(null), "screenshots")
        check(directory.exists() || directory.mkdirs())
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(directory, name).outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
