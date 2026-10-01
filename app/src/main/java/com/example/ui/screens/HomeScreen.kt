package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProductEntity
import com.example.data.model.ReviewEntity
import com.example.ui.components.LIVE_STORE_DOMAIN
import com.example.ui.components.ProductCard
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.DeepBrown
import com.example.ui.theme.GoldPrimary

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    reviews: List<ReviewEntity>,
    isAdminLoggedIn: Boolean,
    onNavigateToShop: () -> Unit,
    onNavigateToContact: () -> Unit,
    onAddToCart: (ProductEntity, String, String, Int) -> Unit,
    onBuyNow: (ProductEntity, String, String, Int) -> Unit,
    onAdminAction: () -> Unit,
    onOpenReviewDialog: () -> Unit,
    onOpenDomainDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featured = products.filter { it.isFeatured }
    val newArrivals = products.filter { it.isNew }
    val bestSellers = products.filter { it.isBestSeller }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Banner Section
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dora_hero_banner_1790873136001),
                            contentDescription = "Dora Fashions Artisan Beads",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xAA3B2D22))
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.BottomStart)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldPrimary)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Handcrafted in Kampala", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Beautiful Beads. Beautiful Creations.",
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = BronzeBrown
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Dora Fashions supplies premium bag beads, crystal strands, freshwater pearls, and bead-making supplies across Uganda. Order online or via WhatsApp.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onNavigateToShop,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hero_shop_now_button")
                            ) {
                                Text("Shop Now")
                            }

                            OutlinedButton(
                                onClick = {
                                    if (isAdminLoggedIn) onAdminAction() else onNavigateToContact()
                                },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hero_secondary_button")
                            ) {
                                Text(
                                    text = if (isAdminLoggedIn) "Owner Panel" else "Custom Orders",
                                    color = BronzeBrown,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Domain & Web Access Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOpenDomainDialog() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Live Store Domain Online",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Accessible to every visitor on web & mobile. Tap to view link & share.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Featured Products
        if (featured.isNotEmpty()) {
            item {
                SectionHeader(title = "Featured Products", subtitle = "Hand-picked favorites from our collection")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(featured, key = { it.id }) { prod ->
                        ProductCard(
                            product = prod,
                            onAddToCart = onAddToCart,
                            onBuyNow = onBuyNow,
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }

        // New Arrivals
        if (newArrivals.isNotEmpty()) {
            item {
                SectionHeader(title = "New Arrivals", subtitle = "Fresh bead designs & charms just added")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(newArrivals, key = { it.id }) { prod ->
                        ProductCard(
                            product = prod,
                            onAddToCart = onAddToCart,
                            onBuyNow = onBuyNow,
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }

        // Best Sellers
        if (bestSellers.isNotEmpty()) {
            item {
                SectionHeader(title = "Best Sellers", subtitle = "What our jewelry & bag creators love most")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(bestSellers, key = { it.id }) { prod ->
                        ProductCard(
                            product = prod,
                            onAddToCart = onAddToCart,
                            onBuyNow = onBuyNow,
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }

        // Why Choose Dora Fashions
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(title = "Why Choose Us", subtitle = "Quality you can see and feel in every bead")
                val perks = listOf(
                    Triple("💎", "Premium Quality", "Carefully sourced beads with long-lasting brilliant shine."),
                    Triple("🎨", "Endless Variety", "Bag charms, crystals, pearls, and acrylic mixes for any project."),
                    Triple("🚚", "Nationwide Delivery", "Door-to-door delivery in Kampala, Entebbe, Jinja and across Uganda."),
                    Triple("💬", "Direct WhatsApp Hotline", "Fast ordering, friendly advice, and bespoke custom orders.")
                )
                perks.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { perk ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(perk.first, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(perk.second, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BronzeBrown)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        perk.third,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Customer Reviews & Community Posts
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Customer Reviews",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = BronzeBrown
                        )
                        Text(
                            text = "Live feedback from bead makers across Uganda",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = onOpenReviewDialog,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("write_review_home_button")
                    ) {
                        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Review", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                reviews.take(5).forEach { rev ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = rev.authorName,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBrown
                                )
                                Row {
                                    repeat(rev.rating) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = GoldPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${rev.comment}”",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = rev.date,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = BronzeBrown
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
