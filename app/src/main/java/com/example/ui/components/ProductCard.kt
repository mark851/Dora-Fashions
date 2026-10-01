package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ProductEntity
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WhatsAppGreen
import java.text.NumberFormat
import java.util.Locale

fun formatUgx(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "UGX ${formatter.format(amount)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCart: (product: ProductEntity, color: String, size: String, quantity: Int) -> Unit,
    onBuyNow: (product: ProductEntity, color: String, size: String, quantity: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = product.colorList
    val sizes = product.sizeList

    var selectedColor by remember(product.id) { mutableStateOf(colors.firstOrNull() ?: "Standard") }
    var selectedSize by remember(product.id) { mutableStateOf(sizes.firstOrNull() ?: "Standard") }
    var quantity by remember(product.id) { mutableIntStateOf(1) }

    var colorExpanded by remember { mutableStateOf(false) }
    var sizeExpanded by remember { mutableStateOf(false) }

    val isOutOfStock = product.stock <= 0

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                RoundedCornerShape(18.dp)
            )
            .testTag("product_card_${product.id}")
    ) {
        Column {
            // Visual Header (Image or BeadArtCanvas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            ) {
                if (!product.imageUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = product.imageUri,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                } else {
                    BeadArtCanvas(
                        seed = product.id * 883L + 17L,
                        colors = colors,
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                }

                // Badges
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (product.isNew) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldPrimary)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("New", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (product.isBestSeller) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BronzeBrown)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("Best Seller", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (product.isFeatured && !product.isNew && !product.isBestSeller) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldPrimary)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("Featured", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Product Details
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatUgx(product.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Color and Size Selector Dropdowns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Color Dropdown
                    ExposedDropdownMenuBox(
                        expanded = colorExpanded,
                        onExpandedChange = { colorExpanded = !colorExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedColor,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Color", fontSize = 10.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = colorExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = colorExpanded,
                            onDismissRequest = { colorExpanded = false }
                        ) {
                            colors.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c, fontSize = 12.sp) },
                                    onClick = {
                                        selectedColor = c
                                        colorExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Size Dropdown
                    ExposedDropdownMenuBox(
                        expanded = sizeExpanded,
                        onExpandedChange = { sizeExpanded = !sizeExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedSize,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Size", fontSize = 10.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sizeExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = sizeExpanded,
                            onDismissRequest = { sizeExpanded = false }
                        ) {
                            sizes.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s, fontSize = 12.sp) },
                                    onClick = {
                                        selectedSize = s
                                        sizeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stock status
                val stockText = when {
                    isOutOfStock -> "Out of stock"
                    product.stock < 10 -> "Only ${product.stock} left in stock"
                    else -> "In stock (${product.stock})"
                }
                val stockColor = when {
                    isOutOfStock -> CoralAccent
                    product.stock < 10 -> Color(0xFFD98A1A)
                    else -> WhatsAppGreen
                }
                Text(
                    text = stockText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = stockColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = !isOutOfStock && quantity > 1,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease quantity", modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = { if (quantity < product.stock) quantity++ },
                            enabled = !isOutOfStock && quantity < product.stock,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase quantity", modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Action Buttons
                    Button(
                        onClick = {
                            if (!isOutOfStock) {
                                onAddToCart(product, selectedColor, selectedSize, quantity)
                            }
                        },
                        enabled = !isOutOfStock,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("add_to_cart_${product.id}")
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            if (!isOutOfStock) {
                                onBuyNow(product, selectedColor, selectedSize, quantity)
                            }
                        },
                        enabled = !isOutOfStock,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("buy_now_${product.id}")
                    ) {
                        Text("Buy", fontSize = 12.sp, color = BronzeBrown, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
