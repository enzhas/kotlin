package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Product
import com.example.myapplication.model.ProductCategory
import com.example.myapplication.ui.theme.SulpakRed
import java.text.NumberFormat
import java.util.Locale

fun categoryLabel(category: ProductCategory?): Int = when (category) {
    null -> R.string.category_all
    ProductCategory.SMARTPHONES -> R.string.category_smartphones
    ProductCategory.LAPTOPS -> R.string.category_laptops
    ProductCategory.AUDIO -> R.string.category_audio
    ProductCategory.HOME -> R.string.category_home
}

@Composable
fun priceText(amount: Long): String {
    // This formatter is a UI helper; it can be recreated after rotation.
    val formatter = remember { NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-KZ")) }
    return stringResource(R.string.price_tenge, formatter.format(amount))
}

@Composable
fun ShopIcon(resource: Int, description: String?, modifier: Modifier = Modifier) {
    Icon(painterResource(resource), contentDescription = description, modifier = modifier.size(22.dp))
}

@Composable
fun ProductArtwork(product: Product, modifier: Modifier = Modifier) {
    val drawable = when (product.category) {
        ProductCategory.SMARTPHONES -> R.drawable.product_phone
        ProductCategory.LAPTOPS -> R.drawable.product_laptop
        ProductCategory.AUDIO -> R.drawable.product_headphones
        ProductCategory.HOME -> if (product.id == 7L) R.drawable.product_vacuum else R.drawable.product_home
    }
    Box(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(drawable), contentDescription = product.name, modifier = Modifier.fillMaxSize().padding(12.dp))
    }
}

@Composable
fun ShopHeader() {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.sulpak_logo),
                contentDescription = stringResource(R.string.brand),
                modifier = Modifier.width(132.dp).aspectRatio(387f / 127f)
            )
            Text(stringResource(R.string.brand_tagline), style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CatalogBanner(modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SulpakRed), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(stringResource(R.string.hero_label), color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
            Text(stringResource(R.string.hero_title), color = Color.White,
                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.hero_subtitle), color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EmptyShopState(isCart: Boolean, onOpenCatalog: () -> Unit = {}) {
    Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 36.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ShopIcon(if (isCart) R.drawable.ic_cart else R.drawable.ic_search, null)
            Text(stringResource(if (isCart) R.string.empty_cart else R.string.no_results),
                style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(stringResource(if (isCart) R.string.empty_cart_hint else R.string.no_results_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            if (isCart) Button(onClick = onOpenCatalog) { Text(stringResource(R.string.back_to_catalog)) }
        }
    }
}
