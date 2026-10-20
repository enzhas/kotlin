package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.Product

@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    cartQuantity: Int,
    onFavorite: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier.fillMaxWidth().testTag("product_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
                ProductArtwork(product, Modifier.size(width = 96.dp, height = 112.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(categoryLabel(product.category)), style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(product.description, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(priceText(product.price), style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                IconToggleButton(
                    checked = isFavorite, onCheckedChange = { onFavorite() },
                    modifier = Modifier.size(48.dp).testTag("favorite_${product.id}")
                ) {
                    Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.ic_heart),
                        contentDescription = stringResource(if (isFavorite) R.string.favorite_remove else R.string.favorite_add, product.name),
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(24.dp))
                }
            }
            Button(
                onClick = onAddToCart,
                enabled = cartQuantity < 99,
                modifier = Modifier.fillMaxWidth().testTag("add_to_cart_${product.id}"),
                shape = RoundedCornerShape(12.dp)
            ) {
                ShopIcon(R.drawable.ic_cart, null)
                Spacer(Modifier.width(8.dp))
                Text(when {
                    cartQuantity == 0 -> stringResource(R.string.add_to_cart)
                    cartQuantity >= 99 -> stringResource(R.string.cart_limit)
                    else -> stringResource(R.string.in_cart, cartQuantity)
                })
            }
        }
    }
}
