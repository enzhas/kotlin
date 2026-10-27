package com.example.myapplication.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.ProductCategory

@Composable
fun ProductSearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search_products)) },
        leadingIcon = { ShopIcon(R.drawable.ic_search, null) },
        trailingIcon = {
            if (query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) {
                ShopIcon(R.drawable.ic_close, stringResource(R.string.clear_search))
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        modifier = modifier.fillMaxWidth().testTag("product_search")
    )
}

@Composable
fun CategoryFilterRow(selected: ProductCategory?, onSelect: (ProductCategory?) -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (listOf<ProductCategory?>(null) + ProductCategory.entries).forEach { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelect(category) },
                label = { Text(stringResource(categoryLabel(category))) },
                modifier = Modifier.testTag("category_${category?.name ?: "ALL"}")
            )
        }
    }
}

@Composable
fun FavoritesFilter(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.testTag("favorites_filter").toggleable(
            value = checked, role = Role.Checkbox, onValueChange = onCheckedChange
        ).padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Text(stringResource(R.string.favorites_only), style = MaterialTheme.typography.bodyMedium)
    }
}
