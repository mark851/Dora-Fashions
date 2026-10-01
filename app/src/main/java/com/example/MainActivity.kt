package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.DoraViewModel
import com.example.ui.components.AdminLoginDialog
import com.example.ui.components.DomainInfoDialog
import com.example.ui.components.DoraTopBar
import com.example.ui.components.PostProductDialog
import com.example.ui.components.WriteReviewDialog
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CustomerReviewsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderConfirmationScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.theme.BronzeBrown
import com.example.ui.theme.DoraFashionsTheme
import com.example.ui.theme.GoldPrimary

enum class Screen {
    HOME,
    SHOP,
    REVIEWS,
    CART,
    CHECKOUT,
    ORDER_CONFIRMATION,
    CONTACT,
    ADMIN_PANEL
}

class MainActivity : ComponentActivity() {
    private val viewModel: DoraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoraFashionsTheme {
                DoraApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DoraApp(viewModel: DoraViewModel) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val banners by viewModel.banners.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount by viewModel.cartCount.collectAsStateWithLifecycle()
    val cartSubtotal by viewModel.cartSubtotal.collectAsStateWithLifecycle()
    val deliveryFee by viewModel.deliveryFee.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var confirmedOrderNo by remember { mutableStateOf("") }

    var showDomainDialog by remember { mutableStateOf(false) }
    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var showPostProductDialog by remember { mutableStateOf(false) }
    var showWriteReviewDialog by remember { mutableStateOf(false) }

    // Handle back navigation
    BackHandler(enabled = currentScreen != Screen.HOME) {
        currentScreen = when (currentScreen) {
            Screen.CHECKOUT -> Screen.CART
            Screen.ORDER_CONFIRMATION -> Screen.SHOP
            Screen.ADMIN_PANEL -> Screen.HOME
            else -> Screen.HOME
        }
    }

    if (showDomainDialog) {
        DomainInfoDialog(onDismiss = { showDomainDialog = false })
    }

    if (showAdminLoginDialog) {
        AdminLoginDialog(
            onDismiss = { showAdminLoginDialog = false },
            onLoginSuccess = {
                showAdminLoginDialog = false
                currentScreen = Screen.ADMIN_PANEL
            },
            onVerifyPassword = { pass -> viewModel.loginAdmin(pass) }
        )
    }

    if (showPostProductDialog) {
        PostProductDialog(
            onDismiss = { showPostProductDialog = false },
            onProductPosted = { newProduct ->
                viewModel.postProduct(newProduct)
                showPostProductDialog = false
            }
        )
    }

    if (showWriteReviewDialog) {
        WriteReviewDialog(
            onDismiss = { showWriteReviewDialog = false },
            onSubmitReview = { newReview ->
                viewModel.postReview(newReview)
                showWriteReviewDialog = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DoraTopBar(
                banners = banners,
                cartCount = cartCount,
                isAdminLoggedIn = isAdminLoggedIn,
                onCartClick = { currentScreen = Screen.CART },
                onDomainClick = { showDomainDialog = true },
                onAdminClick = {
                    if (isAdminLoggedIn) {
                        currentScreen = Screen.ADMIN_PANEL
                    } else {
                        showAdminLoginDialog = true
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BronzeBrown
            ) {
                NavigationBarItem(
                    selected = currentScreen == Screen.HOME,
                    onClick = { currentScreen = Screen.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.SHOP,
                    onClick = { currentScreen = Screen.SHOP },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Shop") },
                    label = { Text("Shop") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_shop")
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.REVIEWS,
                    onClick = { currentScreen = Screen.REVIEWS },
                    icon = { Icon(Icons.Default.RateReview, contentDescription = "Reviews") },
                    label = { Text("Reviews") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_reviews")
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.CART || currentScreen == Screen.CHECKOUT,
                    onClick = { currentScreen = Screen.CART },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(containerColor = GoldPrimary, contentColor = Color.White) {
                                        Text("$cartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldPrimary,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_cart")
                )

                if (isAdminLoggedIn) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.ADMIN_PANEL,
                        onClick = { currentScreen = Screen.ADMIN_PANEL },
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                        label = { Text("Admin") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_admin")
                    )
                } else {
                    NavigationBarItem(
                        selected = currentScreen == Screen.CONTACT,
                        onClick = { currentScreen = Screen.CONTACT },
                        icon = { Icon(Icons.Default.Phone, contentDescription = "Contact") },
                        label = { Text("Contact") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_contact")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                products = products,
                reviews = reviews,
                isAdminLoggedIn = isAdminLoggedIn,
                onNavigateToShop = { currentScreen = Screen.SHOP },
                onNavigateToContact = { currentScreen = Screen.CONTACT },
                onAddToCart = { prod, col, sz, qty ->
                    viewModel.addToCart(prod, col, sz, qty)
                },
                onBuyNow = { prod, col, sz, qty ->
                    viewModel.addToCart(prod, col, sz, qty)
                    currentScreen = Screen.CART
                },
                onAdminAction = {
                    if (isAdminLoggedIn) {
                        currentScreen = Screen.ADMIN_PANEL
                    } else {
                        showAdminLoginDialog = true
                    }
                },
                onOpenReviewDialog = { showWriteReviewDialog = true },
                onOpenDomainDialog = { showDomainDialog = true },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.SHOP -> ShopScreen(
                products = products,
                isAdminLoggedIn = isAdminLoggedIn,
                onAddToCart = { prod, col, sz, qty ->
                    viewModel.addToCart(prod, col, sz, qty)
                },
                onBuyNow = { prod, col, sz, qty ->
                    viewModel.addToCart(prod, col, sz, qty)
                    currentScreen = Screen.CART
                },
                onOpenAddProductDialog = { showPostProductDialog = true },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.REVIEWS -> CustomerReviewsScreen(
                reviews = reviews,
                onOpenWriteReviewDialog = { showWriteReviewDialog = true },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.CART -> CartScreen(
                cartItems = cartItems,
                subtotal = cartSubtotal,
                deliveryFee = deliveryFee,
                selectedLocation = selectedLocation,
                onLocationSelected = { loc -> viewModel.setLocation(loc) },
                onUpdateQuantity = { key, delta -> viewModel.updateCartQuantity(key, delta) },
                onRemoveItem = { key -> viewModel.removeFromCart(key) },
                onProceedToCheckout = { currentScreen = Screen.CHECKOUT },
                onBrowseShop = { currentScreen = Screen.SHOP },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.CHECKOUT -> CheckoutScreen(
                cartItems = cartItems,
                subtotal = cartSubtotal,
                selectedLocation = selectedLocation,
                onLocationSelected = { loc -> viewModel.setLocation(loc) },
                onConfirmOrder = { name, phone, email, addr, loc, pay, notes ->
                    val no = viewModel.postOrder(name, phone, email, addr, loc, pay, notes)
                    confirmedOrderNo = no
                    currentScreen = Screen.ORDER_CONFIRMATION
                },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.ORDER_CONFIRMATION -> OrderConfirmationScreen(
                orderNo = confirmedOrderNo,
                onContinueShopping = { currentScreen = Screen.SHOP },
                onViewOrders = {
                    if (isAdminLoggedIn) {
                        currentScreen = Screen.ADMIN_PANEL
                    } else {
                        currentScreen = Screen.SHOP
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.CONTACT -> ContactScreen(
                onSendMessage = { name, contact, msg ->
                    viewModel.postMessage(name, contact, msg)
                },
                onOpenDomainDialog = { showDomainDialog = true },
                modifier = Modifier.padding(innerPadding)
            )

            Screen.ADMIN_PANEL -> AdminPanelScreen(
                products = products,
                reviews = reviews,
                orders = orders,
                messages = messages,
                onLogout = {
                    viewModel.logoutAdmin()
                    currentScreen = Screen.HOME
                },
                onOpenAddProductDialog = { showPostProductDialog = true },
                onUpdateOrderStatus = { orderNo, st -> viewModel.updateOrderStatus(orderNo, st) },
                onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                onToggleReviewApproval = { id, app -> viewModel.toggleReviewApproved(id, app) },
                onDeleteReview = { id -> viewModel.deleteReview(id) },
                onChangePassword = { newPass -> viewModel.changeAdminPassword(newPass) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
