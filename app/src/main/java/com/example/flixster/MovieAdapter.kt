package com.example.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter {

    class MovieAdapter(private val movies: List<Movie>, movieFragment: MovieFragment)
        : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

        inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var movie: Movie? = null
            val movieTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
            val movieDescription: TextView = view.findViewById<View>(R.id.tvDescription) as TextView
            val moviePoster: ImageView = view.findViewById<View>(R.id.ivPoster) as ImageView

            override fun toString(): String {
                return movieTitle.toString()
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_movie, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = movies[position]

            holder.movie = movie
            holder.movieTitle.text = movie.title
            holder.movieDescription.text = movie.description

            Glide.with(holder.view)
                .load("https://image.tmdb.org/t/p/w500/" + movie.posterUrl)
                .placeholder(R.drawable.ic_loading)
                .centerInside()
                .into(holder.moviePoster)
        }

        override fun getItemCount(): Int {
            return movies.size
        }
    }
}