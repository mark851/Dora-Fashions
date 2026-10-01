package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BannerEntity
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.DeepBrown
import com.example.ui.theme.GoldPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoraTopBar(
    banners: List<BannerEntity>,
    cartCount: Int,
    isAdminLoggedIn: Boolean,
    onCartClick: () -> Unit,
    onDomainClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bannerIndex by remember { mutableIntStateOf(0) }

    val defaultBanners = listOf(
        "✨ Free delivery in Kampala on bead orders above UGX 100,000 ✨",
        "💎 New crystal strands & pearl bag accessories just arrived!",
        "📱 Direct WhatsApp hotline: +256 775 803 896 — Fast orders & advice"
    )
    val displayBanners = if (banners.isNotEmpty()) banners.map { it.message } else defaultBanners

    LaunchedEffect(displayBanners.size) {
        if (displayBanners.isNotEmpty()) {
            while (true) {
                delay(4500)
                bannerIndex = (bannerIndex + 1) % displayBanners.size
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Announcement Bar
        if (displayBanners.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BronzeBrown)
                    .clickable { onDomainClick() }
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = bannerIndex,
                    transitionSpec = {
                        slideInVertically { height -> height } togetherWith
                                slideOutVertically { height -> -height }
                    },
                    label = "banner_scroll"
                ) { index ->
                    Text(
                        text = displayBanners[index % displayBanners.size],
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }

        // Main App Bar
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Dora ",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        color = BronzeBrown
                    )
                    Text(
                        text = "Fashions",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            actions = {
                // Admin Lock / Portal Button
                IconButton(
                    onClick = onAdminClick,
                    modifier = Modifier.testTag("admin_lock_button")
                ) {
                    Icon(
                        imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                        contentDescription = "Admin Portal (Owner Only)",
                        tint = if (isAdminLoggedIn) GoldPrimary else BronzeBrown
                    )
                }

                // Domain / Web Info
                IconButton(
                    onClick = onDomainClick,
                    modifier = Modifier.testTag("domain_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Live Domain & Web Access",
                        tint = BronzeBrown
                    )
                }

                // Cart with badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.testTag("cart_top_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = GoldPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text("$cartCount", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = DeepBrown
                        )
                    }
                }
            }
        )
    }
}
