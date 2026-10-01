package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.BannerEntity
import com.example.data.model.MessageEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ReviewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class DoraRepository(private val database: AppDatabase) {
    private val productDao = database.productDao()
    private val reviewDao = database.reviewDao()
    private val orderDao = database.orderDao()
    private val messageDao = database.messageDao()
    private val bannerDao = database.bannerDao()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val allReviews: Flow<List<ReviewEntity>> = reviewDao.getAllReviews()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allMessages: Flow<List<MessageEntity>> = messageDao.getAllMessages()
    val activeBanners: Flow<List<BannerEntity>> = bannerDao.getActiveBanners()

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        if (productDao.getProductCount() == 0) {
            val initialProducts = listOf(
                ProductEntity(
                    id = 1,
                    name = "Gold Bag Bead Charm Set",
                    category = "Bag Beads",
                    price = 15000,
                    colors = "Gold, Silver, Rose Gold",
                    sizes = "8mm, 10mm, 12mm",
                    stock = 40,
                    isFeatured = true,
                    isBestSeller = true,
                    description = "Dazzling metallic finish bead charms crafted to elevate handbags, clutches, and statement totes. Resistant to tarnishing.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 2,
                    name = "Rainbow Bag Bead Strand",
                    category = "Bag Beads",
                    price = 12000,
                    colors = "Multi, Rainbow, Pastel",
                    sizes = "8mm, 12mm",
                    stock = 25,
                    isNew = true,
                    description = "Vibrant multi-colored bead strands specially designed for colorful woven summer bags and wristlets.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 3,
                    name = "Handmade Clay Bead Pack",
                    category = "Handmade Beads",
                    price = 18000,
                    colors = "Brown, Orange, Terracotta, Green",
                    sizes = "Small, Medium",
                    stock = 18,
                    isFeatured = true,
                    description = "Authentic terracotta and glazed ceramic beads hand-rolled by local artisans. Earthy organic textures.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 4,
                    name = "African Print Handmade Beads",
                    category = "Handmade Beads",
                    price = 22000,
                    colors = "Multi, Earth, Ankara Gold",
                    sizes = "Medium, Large",
                    stock = 12,
                    isBestSeller = true,
                    description = "Distinctive hand-painted African motif beads for high-fashion jewelry, statement necklaces, and purse accents.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 5,
                    name = "Crystal Glass Strand",
                    category = "Crystal Beads",
                    price = 25000,
                    colors = "Clear, Pink, Royal Blue",
                    sizes = "6mm, 8mm",
                    stock = 30,
                    isFeatured = true,
                    isNew = true,
                    description = "High-refraction faceted crystal glass with brilliant rainbow glints. Ideal for bridal and luxury bag designs.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 6,
                    name = "Bicone Crystal Beads (500pcs)",
                    category = "Crystal Beads",
                    price = 35000,
                    colors = "Clear, Purple, Emerald, Amber",
                    sizes = "4mm, 6mm",
                    stock = 15,
                    description = "Precision faceted bicone cut crystals in bulk pack. Perfectly uniform for tight beading weaves.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 7,
                    name = "Freshwater Pearl Strand",
                    category = "Pearl Beads",
                    price = 30000,
                    colors = "Pearl, White, Soft Pink",
                    sizes = "6mm, 8mm",
                    stock = 22,
                    isFeatured = true,
                    isBestSeller = true,
                    description = "Luminous natural luster freshwater cultured pearls. Smooth silky feel and timeless beauty.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 8,
                    name = "Pearl Bag Chain Beads",
                    category = "Pearl Beads",
                    price = 28000,
                    colors = "Pearl, Gold, Cream",
                    sizes = "10mm, 14mm",
                    stock = 15,
                    isNew = true,
                    description = "Large statement pearls linked with durable wire inserts for making elegant handbag strap handles.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 9,
                    name = "Chunky Acrylic Bead Mix",
                    category = "Acrylic Beads",
                    price = 10000,
                    colors = "Multi, Neon, Pastel",
                    sizes = "12mm, 16mm",
                    stock = 60,
                    isBestSeller = true,
                    description = "Lightweight chunky acrylic beads with smooth matte and glossy finishes. Great for kids crafts and trendy bags.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 10,
                    name = "Acrylic Letter Beads (A-Z)",
                    category = "Acrylic Beads",
                    price = 14000,
                    colors = "White, Black, Pink, Gold",
                    sizes = "7mm",
                    stock = 35,
                    isNew = true,
                    description = "Crisp typography letter beads for custom name bracelets, keyrings, and personalized bag charms.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 11,
                    name = "Beading Needle & Thread Kit",
                    category = "Bead Accessories",
                    price = 8000,
                    colors = "Black, White, Transparent",
                    sizes = "Standard",
                    stock = 50,
                    isFeatured = true,
                    description = "Heavy duty bonded nylon beading thread and assorted extra-fine beading needles for smooth threading.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 12,
                    name = "Gold Clasps & Findings Set",
                    category = "Bead Accessories",
                    price = 12000,
                    colors = "Gold, Silver, Antique Bronze",
                    sizes = "Assorted",
                    stock = 45,
                    isBestSeller = true,
                    description = "Lobster clasps, jump rings, crimp beads, and bead caps in premium gold plating.",
                    postedBy = "Dora Fashions"
                ),
                ProductEntity(
                    id = 13,
                    name = "Custom Bag Bead Design (from)",
                    category = "Custom Orders",
                    price = 50000,
                    colors = "Custom, Multi",
                    sizes = "Custom",
                    stock = 99,
                    isNew = true,
                    description = "Commission a bespoke handmade bead bag or custom accessory strand crafted specifically for your event or brand.",
                    postedBy = "Dora Fashions"
                )
            )
            productDao.insertProducts(initialProducts)
        }

        if (reviewDao.getReviewCount() == 0) {
            val initialReviews = listOf(
                ReviewEntity(
                    id = 1,
                    authorName = "Sarah N.",
                    rating = 5,
                    comment = "The pearl strands are gorgeous and arrived quickly to Muyenga. My beaded handbags have never looked better! Excellent craftsmanship.",
                    date = "2 days ago",
                    isApproved = true
                ),
                ReviewEntity(
                    id = 2,
                    authorName = "Grace A.",
                    rating = 5,
                    comment = "Great quality beads and very fair prices. Ordering on WhatsApp was so easy and customer service is 10/10.",
                    date = "1 week ago",
                    isApproved = true
                ),
                ReviewEntity(
                    id = 3,
                    authorName = "Miriam K.",
                    rating = 5,
                    comment = "Dora made a custom bead set for my bridal entourage in Kampala. Absolutely breathtaking sparkle in person!",
                    date = "2 weeks ago",
                    isApproved = true
                ),
                ReviewEntity(
                    id = 4,
                    authorName = "Brenda T.",
                    rating = 5,
                    comment = "I run a handmade accessory shop in Jinja and order all my findings and crystal beads here. Reliable delivery every time.",
                    date = "3 weeks ago",
                    isApproved = true
                )
            )
            reviewDao.insertReviews(initialReviews)
        }

        if (bannerDao.getBannerCount() == 0) {
            val initialBanners = listOf(
                BannerEntity(
                    id = 1,
                    message = "✨ Free delivery in Kampala on bead orders above UGX 100,000 ✨"
                ),
                BannerEntity(
                    id = 2,
                    message = "💎 New arrivals: Crystal bicones & pearl bag chain beads now in stock!"
                ),
                BannerEntity(
                    id = 3,
                    message = "📱 Direct WhatsApp hotline: +256 775 803 896 — Fast orders & advice"
                )
            )
            bannerDao.insertBanners(initialBanners)
        }
    }

    suspend fun insertProduct(product: ProductEntity): Long = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(id: Long) = withContext(Dispatchers.IO) {
        productDao.deleteProductById(id)
    }

    suspend fun insertReview(review: ReviewEntity): Long = withContext(Dispatchers.IO) {
        reviewDao.insertReview(review)
    }

    suspend fun setReviewApproved(id: Long, approved: Boolean) = withContext(Dispatchers.IO) {
        reviewDao.setApproved(id, approved)
    }

    suspend fun deleteReview(id: Long) = withContext(Dispatchers.IO) {
        reviewDao.deleteReviewById(id)
    }

    suspend fun insertOrder(order: OrderEntity): Long = withContext(Dispatchers.IO) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(orderNo: String, status: String) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderNo, status)
    }

    suspend fun insertMessage(message: MessageEntity): Long = withContext(Dispatchers.IO) {
        messageDao.insertMessage(message)
    }

    suspend fun decrementProductStock(productId: Long, quantity: Int) = withContext(Dispatchers.IO) {
        val prod = productDao.getProductById(productId).firstOrNull()
        if (prod != null) {
            val updated = maxOf(0, prod.stock - quantity)
            productDao.updateStock(productId, updated)
        }
    }
}
