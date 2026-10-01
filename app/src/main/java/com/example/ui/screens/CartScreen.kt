package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CartItem
import com.example.ui.DELIVERY_LOCATIONS
import com.example.ui.components.BeadArtCanvas
import com.example.ui.components.formatUgx
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.DeepBrown
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WhatsAppGreen
import java.net.URLEncoder

const val WHATSAPP_PHONE = "256775803896"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    subtotal: Long,
    deliveryFee: Long,
    selectedLocation: String,
    onLocationSelected: (String) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onProceedToCheckout: () -> Unit,
    onBrowseShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var locationExpanded by remember { mutableStateOf(false) }
    val total = subtotal + deliveryFee

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Your bead cart is empty",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Discover sparkling crystals, bag charms, and pearl strands in our shop.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onBrowseShop,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Browse Shop Now")
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Your Cart (${cartItems.sumOf { it.quantity }} items)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BronzeBrown
                )
            }

            // Items List
            items(cartItems, key = { it.key }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            if (!item.product.imageUri.isNullOrEmpty()) {
                                AsyncImage(
                                    model = item.product.imageUri,
                                    contentDescription = item.product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                BeadArtCanvas(
                                    seed = item.product.id * 977L,
                                    colors = item.product.colorList,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${item.selectedColor} · ${item.selectedSize}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formatUgx(item.product.price),
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                fontSize = 13.sp
                            )
                        }

                        // Stepper
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 2.dp)
                        ) {
                            IconButton(
                                onClick = { onUpdateQuantity(item.key, -1) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "${item.quantity}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = { onUpdateQuantity(item.key, 1) },
                                enabled = item.quantity < item.product.stock,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                            }
                        }

                        IconButton(
                            onClick = { onRemoveItem(item.key) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        // Delivery Location Dropdown
                        ExposedDropdownMenuBox(
                            expanded = locationExpanded,
                            onExpandedChange = { locationExpanded = !locationExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = "$selectedLocation (${formatUgx(deliveryFee)})",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Delivery Location") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = locationExpanded,
                                onDismissRequest = { locationExpanded = false }
                            ) {
                                DELIVERY_LOCATIONS.forEach { (loc, fee) ->
                                    DropdownMenuItem(
                                        text = { Text("$loc — ${formatUgx(fee)}") },
                                        onClick = {
                                            onLocationSelected(loc)
                                            locationExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatUgx(subtotal), fontWeight = FontWeight.SemiBold)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery Fee", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatUgx(deliveryFee), fontWeight = FontWeight.SemiBold)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DeepBrown)
                            Text(formatUgx(total), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = GoldPrimary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Checkout button
                        Button(
                            onClick = onProceedToCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("proceed_checkout_button")
                        ) {
                            Text("Proceed to In-App Checkout", fontWeight = FontWeight.Bold)
                        }

                        // WhatsApp Order Button
                        Button(
                            onClick = {
                                launchWhatsAppOrder(context, cartItems, subtotal, deliveryFee, selectedLocation)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("whatsapp_order_button")
                        ) {
                            Text("Order on WhatsApp Instant (+256)", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

fun launchWhatsAppOrder(
    context: Context,
    items: List<CartItem>,
    subtotal: Long,
    fee: Long,
    location: String
) {
    val total = subtotal + fee
    val itemsText = items.mapIndexed { idx, it ->
        "${idx + 1}. ${it.product.name} (${it.selectedColor}, ${it.selectedSize}) x${it.quantity} = ${formatUgx(it.product.price * it.quantity)}"
    }.joinToString("\n")

    val message = "Hello Dora Fashions! I'd like to place an order:\n\n" +
            "$itemsText\n\n" +
            "Subtotal: ${formatUgx(subtotal)}\n" +
            "Delivery to: $location (${formatUgx(fee)})\n" +
            "Total: ${formatUgx(total)}\n\n" +
            "Please confirm availability and delivery timing. Thank you!"

    try {
        val encoded = URLEncoder.encode(message, "UTF-8")
        val url = "https://wa.me/$WHATSAPP_PHONE?text=$encoded"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open WhatsApp: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}
