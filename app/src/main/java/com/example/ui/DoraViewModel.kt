package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BannerEntity
import com.example.data.model.CartItem
import com.example.data.model.MessageEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ReviewEntity
import com.example.data.repository.DoraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val DELIVERY_LOCATIONS = mapOf(
    "Kampala" to 5000L,
    "Wakiso" to 8000L,
    "Entebbe" to 10000L,
    "Jinja" to 15000L,
    "Other Uganda" to 20000L,
    "Shop pickup (Kampala)" to 0L
)

class DoraViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DoraRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DoraRepository(database)
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<ReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val messages: StateFlow<List<MessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerEntity>> = repository.activeBanners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _selectedLocation = MutableStateFlow("Kampala")
    val selectedLocation: StateFlow<String> = _selectedLocation

    val deliveryFee: StateFlow<Long> = _selectedLocation.combine(_cartItems) { loc, items ->
        if (items.isEmpty()) 0L else (DELIVERY_LOCATIONS[loc] ?: 5000L)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5000L)

    val cartSubtotal: StateFlow<Long> = _cartItems
        .combine(_cartItems) { items, _ ->
            items.sumOf { it.product.price * it.quantity }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val cartCount: StateFlow<Int> = _cartItems
        .combine(_cartItems) { items, _ ->
            items.sumOf { it.quantity }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToCart(product: ProductEntity, color: String, size: String, quantity: Int) {
        val key = "${product.id}_${color}_$size"
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.key == key }
        if (existingIndex >= 0) {
            val item = current[existingIndex]
            val newQty = minOf(item.quantity + quantity, product.stock)
            current[existingIndex] = item.copy(quantity = newQty)
        } else {
            current.add(
                CartItem(
                    key = key,
                    product = product,
                    selectedColor = color,
                    selectedSize = size,
                    quantity = minOf(quantity, product.stock)
                )
            )
        }
        _cartItems.value = current
    }

    fun updateCartQuantity(key: String, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.key == key }
        if (index >= 0) {
            val item = current[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = item.copy(quantity = minOf(newQty, item.product.stock))
            }
            _cartItems.value = current
        }
    }

    fun removeFromCart(key: String) {
        _cartItems.value = _cartItems.value.filterNot { it.key == key }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun setLocation(location: String) {
        _selectedLocation.value = location
    }

    fun postProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun postReview(review: ReviewEntity) {
        viewModelScope.launch {
            repository.insertReview(review)
        }
    }

    fun postOrder(
        customerName: String,
        phone: String,
        email: String,
        address: String,
        location: String,
        paymentMethod: String,
        notes: String
    ): String {
        val orderNo = "DF-${System.currentTimeMillis().toString().takeLast(6)}"
        val items = _cartItems.value
        val itemsSummary = items.joinToString("; ") {
            "${it.product.name} (${it.selectedColor}, ${it.selectedSize}) x${it.quantity}"
        }
        val subtotal = items.sumOf { it.product.price * it.quantity }
        val fee = DELIVERY_LOCATIONS[location] ?: 5000L
        val total = subtotal + fee

        val order = OrderEntity(
            orderNo = orderNo,
            customerName = customerName,
            phone = phone,
            email = email,
            address = address,
            location = location,
            paymentMethod = paymentMethod,
            notes = notes,
            itemsSummary = itemsSummary,
            subtotal = subtotal,
            deliveryFee = fee,
            total = total,
            status = "Pending"
        )

        viewModelScope.launch {
            repository.insertOrder(order)
            items.forEach { cartItem ->
                repository.decrementProductStock(cartItem.product.id, cartItem.quantity)
            }
            clearCart()
        }

        return orderNo
    }

    fun updateOrderStatus(orderNo: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderNo, status)
        }
    }

    fun postMessage(name: String, contact: String, message: String) {
        viewModelScope.launch {
            repository.insertMessage(
                MessageEntity(
                    name = name,
                    contact = contact,
                    message = message
                )
            )
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    // Admin Authentication & Security
    private val prefs = getApplication<Application>().getSharedPreferences("dora_admin_secure", Context.MODE_PRIVATE)
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn

    fun loginAdmin(password: String): Boolean {
        val storedPassword = prefs.getString("admin_password", "dora2026") ?: "dora2026"
        if (password == storedPassword) {
            _isAdminLoggedIn.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    fun changeAdminPassword(newPassword: String): Boolean {
        if (newPassword.trim().length >= 4) {
            prefs.edit().putString("admin_password", newPassword.trim()).apply()
            return true
        }
        return false
    }

    fun toggleReviewApproved(id: Long, approved: Boolean) {
        viewModelScope.launch {
            repository.setReviewApproved(id, approved)
        }
    }

    fun deleteReview(id: Long) {
        viewModelScope.launch {
            repository.deleteReview(id)
        }
    }
}
