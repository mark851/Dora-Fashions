package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ReviewEntity
import com.example.ui.components.BeadArtCanvas
import com.example.ui.components.formatUgx
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.DeepBrown
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WhatsAppGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPanelScreen(
    products: List<ProductEntity>,
    reviews: List<ReviewEntity>,
    orders: List<OrderEntity>,
    messages: List<MessageEntity>,
    onLogout: () -> Unit,
    onOpenAddProductDialog: () -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onToggleReviewApproval: (Long, Boolean) -> Unit,
    onDeleteReview: (Long) -> Unit,
    onChangePassword: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("📊 Overview", "📦 Products", "🧾 Orders", "⭐ Reviews", "💬 Inquiries")

    Column(modifier = modifier.fillMaxSize()) {
        // Admin Security Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BronzeBrown)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🔒 Owner Admin Center",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Private management mode (markniaras@gmail.com)",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }

                OutlinedButton(
                    onClick = onLogout,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("admin_logout_btn")
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Exit Admin", fontSize = 12.sp)
                }
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldPrimary
        ) {
            tabs.forEachIndexed { idx, label ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = {
                        Text(
                            text = label,
                            fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> AdminDashboardTab(
                products = products,
                orders = orders,
                onChangePassword = onChangePassword
            )
            1 -> AdminProductsTab(
                products = products,
                onOpenAddProductDialog = onOpenAddProductDialog,
                onDeleteProduct = onDeleteProduct
            )
            2 -> AdminOrdersTab(
                orders = orders,
                onUpdateOrderStatus = onUpdateOrderStatus
            )
            3 -> AdminReviewsTab(
                reviews = reviews,
                onToggleApproval = onToggleReviewApproval,
                onDeleteReview = onDeleteReview
            )
            4 -> AdminInquiriesTab(messages = messages)
        }
    }
}

@Composable
fun AdminDashboardTab(
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    onChangePassword: (String) -> Boolean
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }

    val activeOrders = orders.filter { it.status != "Cancelled" }
    val totalRevenue = activeOrders.sumOf { it.total }
    val pendingCount = orders.count { it.status == "Pending" }
    val completedCount = orders.count { it.status == "Completed" }
    val totalStock = products.sumOf { it.stock }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Store Operations & Financials",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BronzeBrown
            )
        }

        // Stats grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Gross Sales",
                        value = formatUgx(totalRevenue),
                        tint = GoldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Total Orders",
                        value = "${orders.size}",
                        tint = DeepBrown,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Pending Orders",
                        value = "$pendingCount",
                        tint = if (pendingCount > 0) CoralAccent else WhatsAppGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Completed",
                        value = "$completedCount",
                        tint = WhatsAppGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Units in Stock",
                        value = "$totalStock",
                        tint = BronzeBrown,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Admin Security: Change Password Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LockReset, contentDescription = null, tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Admin Passcode", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "Set a new confidential passcode so only you can unlock this admin panel.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Passcode (min 4 characters)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (onChangePassword(newPassword)) {
                                Toast.makeText(context, "Admin passcode updated successfully!", Toast.LENGTH_SHORT).show()
                                newPassword = ""
                            } else {
                                Toast.makeText(context, "Passcode must be at least 4 characters", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Update Passcode")
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = tint)
        }
    }
}

@Composable
fun AdminProductsTab(
    products: List<ProductEntity>,
    onOpenAddProductDialog: () -> Unit,
    onDeleteProduct: (Long) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Catalog & Inventory (${products.size} items)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BronzeBrown
                )
                Button(
                    onClick = onOpenAddProductDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("admin_add_product_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Product")
                }
            }
        }

        items(products, key = { it.id }) { p ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        BeadArtCanvas(seed = p.id * 883L, colors = p.colorList, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(p.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${p.category} · ${formatUgx(p.price)}", fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Stock: ${p.stock} units | Sizes: ${p.sizes}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(
                        onClick = {
                            onDeleteProduct(p.id)
                            Toast.makeText(context, "Deleted ${p.name}", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete product", tint = CoralAccent, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersTab(
    orders: List<OrderEntity>,
    onUpdateOrderStatus: (String, String) -> Unit
) {
    val statuses = listOf("Pending", "Processing", "Shipped", "Completed", "Cancelled")

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Customer Orders Fulfillment (${orders.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BronzeBrown
            )
        }

        if (orders.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No customer orders yet.")
                }
            }
        } else {
            items(orders, key = { it.orderNo }) { ord ->
                var expanded by remember { mutableStateOf(false) }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(ord.orderNo, fontWeight = FontWeight.ExtraBold, color = GoldPrimary)

                            // Status Dropdown
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = ord.status,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.width(140.dp).menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    statuses.forEach { st ->
                                        DropdownMenuItem(
                                            text = { Text(st) },
                                            onClick = {
                                                onUpdateOrderStatus(ord.orderNo, st)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Text("Customer: ${ord.customerName} · ${ord.phone}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Address: ${ord.location}, ${ord.address}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Items: ${ord.itemsSummary}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (ord.notes.isNotBlank()) {
                            Text("Notes: ${ord.notes}", fontSize = 11.sp, color = BronzeBrown)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Payment: ${ord.paymentMethod}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatUgx(ord.total), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepBrown)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminReviewsTab(
    reviews: List<ReviewEntity>,
    onToggleApproval: (Long, Boolean) -> Unit,
    onDeleteReview: (Long) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Review Moderation (${reviews.size} total)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BronzeBrown
            )
        }

        items(reviews, key = { it.id }) { rev ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(rev.authorName, fontWeight = FontWeight.Bold, color = DeepBrown)
                        Row {
                            repeat(rev.rating) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(rev.comment, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (rev.isApproved) "Status: Visible to visitors" else "Status: Hidden",
                            fontSize = 11.sp,
                            color = if (rev.isApproved) WhatsAppGreen else Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = { onToggleApproval(rev.id, !rev.isApproved) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (rev.isApproved) "Hide" else "Approve", fontSize = 11.sp)
                            }
                            IconButton(onClick = { onDeleteReview(rev.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CoralAccent, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminInquiriesTab(messages: List<MessageEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Customer Inquiries & Messages (${messages.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BronzeBrown
            )
        }

        if (messages.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No customer messages yet.")
                }
            }
        } else {
            items(messages, key = { it.id }) { msg ->
                val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(msg.timestamp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(msg.name, fontWeight = FontWeight.Bold)
                            Text(dateStr, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(msg.contact, color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(msg.message, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
