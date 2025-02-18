package com.codepath.bestsellerlistapp

import com.google.gson.annotations.SerializedName

/**
 * The model for storing a single book from the NY Times API.
 * The @SerializedName tags must match the JSON keys exactly for correct parsing.
 */
class BestSellerBook {
    @SerializedName("rank")
    var rank: Int = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("author")
    var author: String? = null

    @SerializedName("book_image")
    var bookImageUrl: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("amazon_product_url")
    var amazonUrl: String? = null
}
