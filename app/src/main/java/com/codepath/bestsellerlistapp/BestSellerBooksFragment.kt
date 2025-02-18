package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "afVO01Nkuh1MxmR6CYjvwIULunipuGZ4"

class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("BestSellerBooksFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams().apply { put("api-key", API_KEY) }
        val url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json"

        // Use AsyncHttpResponseHandler to get a response as a byte array
        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?) {
                progressBar.hide()
                val responseString = responseBody?.toString(Charsets.UTF_8)
                Log.d("BestSellerBooksFragment", "response successful: $responseString")
                try {
                    val response = JSONObject(responseString)
                    val resultsJSON = response.getJSONObject("results")
                    val booksJSONArray = resultsJSON.getJSONArray("books")
                    val booksRawJSON = booksJSONArray.toString()
                    Log.d("BestSellerBooksFragment", "Books JSON: $booksRawJSON")

                    val gson = Gson()
                    val arrayBookType = object : TypeToken<List<BestSellerBook>>() {}.type
                    val models: List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayBookType)

                    recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)
                } catch (e: Exception) {
                    Log.e("BestSellerBooksFragment", "Parsing error", e)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
                progressBar.hide()
                val errorResponse = responseBody?.toString(Charsets.UTF_8) ?: "No response"
                Log.e("BestSellerBooksFragment", "API request failed: $errorResponse", error)
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "Clicked: ${item.title}", Toast.LENGTH_LONG).show()
    }
}
