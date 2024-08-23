package com.example.flixster.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixster.API_KEY
import com.example.flixster.CreditsResponse
import com.example.flixster.R
import com.example.flixster.models.Movie
import com.google.gson.Gson
import okhttp3.Headers

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

/** To include a back button, must create own action bar. Currently using "default"*/
//        // Set up the toolbar as the action bar
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        // Enable the up button (back button) in the action bar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        val movieId = intent.getIntExtra("MOVIE_ID", 0)
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Int) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        val movieDetailsUrl = "https://api.themoviedb.org/3/movie/$movieId"
        val creditsUrl = "https://api.themoviedb.org/3/movie/$movieId/credits"

        // Fetch movie details
        client.get(movieDetailsUrl, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val movie = Gson().fromJson(json.jsonObject.toString(), Movie::class.java)
                updateUIWithMovieDetails(movie)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, t: Throwable?) {
                Log.e("MovieDetails", "Failed to fetch movie details: $errorResponse")
            }
        })

        // Fetch cast and crew
        client.get(creditsUrl, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val creditsResponse = Gson().fromJson(json.jsonObject.toString(), CreditsResponse::class.java)
                updateUIWithCredits(creditsResponse)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, t: Throwable?) {
                Log.e("MovieDetails", "Failed to fetch credits: $errorResponse")
            }
        })
    }

    private fun updateUIWithMovieDetails(movie: Movie) {
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvTagline: TextView = findViewById(R.id.tvTagline)
        val tvGenres: TextView = findViewById(R.id.tvGenres)
        val tvDescription: TextView = findViewById(R.id.tvDetailDescription)
        val ivPoster: ImageView = findViewById(R.id.ivDetailPoster)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/" + movie.backdropUrl)
            .placeholder(R.drawable.ic_loading)
            .into(ivPoster)

        tvTitle.text = movie.title
        // Check if tagline is null and update TextView accordingly
        tvTagline.text = movie.tagline ?: "no tagline available"
        tvDescription.text = movie.description
        tvGenres.text = movie.genres?.joinToString(", ") { it.name.toString() }
    }

    private fun updateUIWithCredits(creditsResponse: CreditsResponse) {
        val tvCast: TextView = findViewById(R.id.tvCast)
        val tvCrew: TextView = findViewById(R.id.tvCrew)

        val castList = creditsResponse.cast?.joinToString(", ") { "${it.name} as ${it.character}" }
        val crewList = creditsResponse.crew?.joinToString(", ") { "${it.name} (${it.job})" }

        tvCast.text = castList
        tvCrew.text = crewList
    }

//    // Handle the up button click
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
}
