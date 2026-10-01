package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.ui.theme.GoldPrimary

val AVAILABLE_CATEGORIES = listOf(
    "Bag Beads",
    "Handmade Beads",
    "Crystal Beads",
    "Pearl Beads",
    "Acrylic Beads",
    "Bead Accessories",
    "Custom Orders"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostProductDialog(
    onDismiss: () -> Unit,
    onProductPosted: (ProductEntity) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(AVAILABLE_CATEGORIES[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var priceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("10") }
    var colorsText by remember { mutableStateOf("Gold, Multi") }
    var sizesText by remember { mutableStateOf("8mm, 10mm") }
    var description by remember { mutableStateOf("") }
    var postedBy by remember { mutableStateOf("Community Artisan") }

    var isFeatured by remember { mutableStateOf(false) }
    var isNew by remember { mutableStateOf(true) }
    var isBestSeller by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Post New Bead Product",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Post a bead creation or product to the live database so every visitor can see and order it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_product_name_input")
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        AVAILABLE_CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price (UGX) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("post_product_price_input")
                    )

                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stock Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = colorsText,
                    onValueChange = { colorsText = it },
                    label = { Text("Colors (e.g. Gold, Pearl, Blue)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sizesText,
                    onValueChange = { sizesText = it },
                    label = { Text("Sizes (e.g. 6mm, 8mm, Medium)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Materials") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = postedBy,
                    onValueChange = { postedBy = it },
                    label = { Text("Posted By (Your Name / Brand)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isNew, onCheckedChange = { isNew = it })
                    Text("New", fontSize = 12.sp)

                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    Text("Featured", fontSize = 12.sp)

                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(checked = isBestSeller, onCheckedChange = { isBestSeller = it })
                    Text("Best Seller", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toLongOrNull() ?: 0L
                    val stock = stockText.toIntOrNull() ?: 1
                    if (name.isBlank()) {
                        Toast.makeText(context, "Please enter a product name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (price <= 0) {
                        Toast.makeText(context, "Please enter a valid price in UGX", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newProduct = ProductEntity(
                        name = name.trim(),
                        category = selectedCategory,
                        price = price,
                        colors = colorsText.trim().ifBlank { "Multi" },
                        sizes = sizesText.trim().ifBlank { "Standard" },
                        stock = stock,
                        isFeatured = isFeatured,
                        isNew = isNew,
                        isBestSeller = isBestSeller,
                        description = description.trim(),
                        postedBy = postedBy.trim().ifBlank { "Dora Fashions" }
                    )
                    onProductPosted(newProduct)
                    Toast.makeText(context, "✨ Posted successfully to live database!", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_post_product_button")
            ) {
                Text("Publish to Feed")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
