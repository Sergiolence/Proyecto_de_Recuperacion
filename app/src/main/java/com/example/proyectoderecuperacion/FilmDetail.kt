package com.example.proyectoderecuperacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyectoderecuperacion.models.Film

class FilmDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        // Recupera la información de la película pasada en el Intent
        val film = intent.getSerializableExtra("film") as Film

        // Utiliza los elementos del layout para mostrar la información de la película
        val posterImageView = findViewById<ImageView>(R.id.poster)
        val titleTextView = findViewById<TextView>(R.id.filmTitle)
        val genresTextView = findViewById<TextView>(R.id.genresContent)
        val overviewTextView = findViewById<TextView>(R.id.summaryContent)
        val releaseDateTextView = findViewById<TextView>(R.id.dateContent)

        Glide.with(this).load("https://image.tmdb.org/t/p/w200/"+film.poster_path).into(posterImageView)
        titleTextView.text = film.title
        genresTextView.text = film.genre.toString()

        //?.joinToString(", ")
        overviewTextView.text = film.overview
        releaseDateTextView.text = film.release_date.toString()

        // Personaliza la apariencia de la actividad
        title = film.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}