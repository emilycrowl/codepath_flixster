package com.example.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val NP_URL = "https://api.themoviedb.org/3/movie/now_playing"
private const val U_URL = "https://api.themoviedb.org/3/movie/upcoming"
private const val TAG = "MovieFragment"

class MovieFragment : Fragment() {

    /* construct view */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        val progressBar = view.findViewById<View>(R.id.pbProgress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.rvList) as RecyclerView
        val context = view.context

        recyclerView.layoutManager = LinearLayoutManager(context)
        updateAdapter(progressBar, recyclerView)

        return view
    }

    /* update RecyclerView adapter */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // set up  AsyncHTTPClient()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // perform the HTTP request
        client[
            // NP_URL, // changing requested data
            U_URL,
            params,
            object : JsonHttpResponseHandler()
            {
                /* onSuccess function called when HTTP response status is "200 OK" */
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    progressBar.hide()

                    val resultsRawJSON : String = json.jsonObject.get("results").toString()
                    val gson = Gson()
                    val arrayTutorialType = object : TypeToken<List<Movie>>() {}.type
                    val models : List<Movie> = gson.fromJson(resultsRawJSON, arrayTutorialType)

                    recyclerView.adapter = MovieAdapter(models, this@MovieFragment)

                    Log.d(TAG, models[0].posterUrl.toString())
                    Log.d(TAG, "onSuccess")
                }

                /* onFailure function called when HTTP response status is "4XX" (eg. 401, 403, 404) */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    progressBar.hide()

                    t?.message?.let {
                        Log.e(TAG, "HTML Error Code: " + statusCode.toString() + ", Headers: " + headers.toString())
                        Log.e(TAG, errorResponse)
                    }
                }
            }]
    }

}