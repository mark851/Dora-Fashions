package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val price: Long,
    val colors: String, // comma-separated, e.g. "Gold, Silver"
    val sizes: String,  // comma-separated, e.g. "8mm, 10mm"
    val stock: Int,
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val isBestSeller: Boolean = false,
    val imageUri: String? = null,
    val description: String = "",
    val postedBy: String = "Dora Fashions",
    val postedTimestamp: Long = System.currentTimeMillis()
) {
    val colorList: List<String>
        get() = colors.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("Standard") }

    val sizeList: List<String>
        get() = sizes.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("Standard") }
}

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val isApproved: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNo: String,
    val customerName: String,
    val phone: String,
    val email: String = "",
    val address: String,
    val location: String,
    val paymentMethod: String,
    val notes: String = "",
    val itemsSummary: String,
    val subtotal: Long,
    val deliveryFee: Long,
    val total: Long,
    val status: String = "Pending", // Pending, Processing, Shipped, Completed, Cancelled
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val contact: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val isActive: Boolean = true
)

data class CartItem(
    val key: String,
    val product: ProductEntity,
    val selectedColor: String,
    val selectedSize: String,
    var quantity: Int
)
