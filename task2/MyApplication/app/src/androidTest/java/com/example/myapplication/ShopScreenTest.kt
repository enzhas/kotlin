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
    fun captureCatalogAndCartScreenshots() {
        compose.onNodeWithTag("shop_list").performScrollToIndex(0)
        saveScreenshot("sulpak-catalog.png")
        scrollTo("add_to_cart_1").performClick()
        scrollTo("add_to_cart_2").performClick()
        compose.onNodeWithTag("nav_cart").performClick()
        scrollTo("increase_1").performClick()
        compose.onNodeWithTag("shop_list").performScrollToIndex(0)
        saveScreenshot("sulpak-cart.png")
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
