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
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.CartItem

@Composable
fun QuantityControl(item: CartItem, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FilledTonalIconButton(onClick = onDecrease, modifier = Modifier.testTag("decrease_${item.product.id}")) {
            ShopIcon(R.drawable.ic_minus, stringResource(R.string.decrease_quantity, item.product.name))
        }
        Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp).testTag("quantity_${item.product.id}"),
            style = MaterialTheme.typography.titleMedium)
        FilledTonalIconButton(onClick = onIncrease, enabled = item.quantity < 99,
            modifier = Modifier.testTag("increase_${item.product.id}")) {
            ShopIcon(R.drawable.ic_plus, stringResource(R.string.increase_quantity, item.product.name))
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onDecrease: () -> Unit, onIncrease: () -> Unit, onRemove: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProductArtwork(item.product, Modifier.size(80.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(item.product.description, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(priceText(item.product.price), style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onRemove, modifier = Modifier.testTag("remove_${item.product.id}")) {
                    ShopIcon(R.drawable.ic_delete, stringResource(R.string.remove_product, item.product.name))
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                QuantityControl(item, onDecrease, onIncrease)
                Text(priceText(item.subtotal), fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary, modifier = Modifier.testTag("subtotal_${item.product.id}"))
            }
        }
    }
}

@Composable
fun CartSummary(count: Int, total: Long, onContinueShopping: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.order_summary), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.items_count, count), color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.total), style = MaterialTheme.typography.titleMedium)
                Text(priceText(total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("cart_total"))
            }
            Button(onClick = onContinueShopping, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.continue_shopping))
            }
        }
    }
}
