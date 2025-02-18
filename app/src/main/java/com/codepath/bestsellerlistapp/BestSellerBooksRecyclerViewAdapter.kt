package com.codepath.bestsellerlistapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.bestsellerlistapp.R

/**
 * [RecyclerView.Adapter] that can display a [BestSellerBook] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class BestSellerBooksRecyclerViewAdapter(
    private val books: List<BestSellerBook>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<BestSellerBooksRecyclerViewAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_best_seller_book, parent, false)
        return BookViewHolder(view)
    }

    /**
     * This inner class holds references to all the View elements for a single book item.
     */
    inner class BookViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: BestSellerBook? = null
        val mRanking: TextView = mView.findViewById(R.id.ranking)
        val mBookTitle: TextView = mView.findViewById(R.id.book_title)
        val mBookAuthor: TextView = mView.findViewById(R.id.book_author)
        val mBookImage: ImageView = mView.findViewById(R.id.book_image)
        val mBookDescription: TextView = mView.findViewById(R.id.book_description)
        val mBuyButton: Button = mView.findViewById(R.id.buy_button)

        override fun toString(): String {
            return "${mBookTitle.text} by ${mBookAuthor.text}"
        }
    }

    /**
     * Bind each book's data to its corresponding views.
     */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.mItem = book

        // Bind text values
        holder.mRanking.text = book.rank.toString()
        holder.mBookTitle.text = book.title
        holder.mBookAuthor.text = book.author
        holder.mBookDescription.text = book.description ?: "No description available"

        // Load the book cover image using Glide
        Glide.with(holder.mView.context)
            .load(book.bookImageUrl)
            .centerInside()
            .into(holder.mBookImage)

        // Set the Buy button to open the Amazon URL in a browser
        holder.mBuyButton.setOnClickListener {
            book.amazonUrl?.let { url ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ContextCompat.startActivity(holder.mView.context, browserIntent, null)
            }
        }

        // Set an item click listener to notify when a book is clicked
        holder.mView.setOnClickListener {
            holder.mItem?.let { book ->
                mListener?.onItemClick(book)
            }
        }
    }

    override fun getItemCount(): Int = books.size
}
