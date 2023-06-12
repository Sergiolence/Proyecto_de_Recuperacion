package com.example.proyectoderecuperacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyectoderecuperacion.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FilmDetail : AppCompatActivity() {
    private lateinit var button: Button
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
        button = findViewById<Button>(R.id.button2)

        Glide.with(this).load("https://image.tmdb.org/t/p/w200/"+film.poster_path).into(posterImageView)
        titleTextView.text = film.title
        genresTextView.text = film.genre.toString()
        overviewTextView.text = film.overview
        releaseDateTextView.text = film.release_date.toString()

        button.isEnabled = false
        checkFilmExistence(film)

        // Personaliza la apariencia de la actividad
        title = film.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkFilmExistence(film: Film) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val filmRef = firebaseDatabase.getReference("films")

        filmRef.orderByChild("id").equalTo(film.id.toDouble()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // La peli existe
                    button.isEnabled = false
                } else {
                    // La peli no existe en firebase
                    button.isEnabled = true
                    button.setOnClickListener {
                        val newFilmRef = filmRef.push()
                        newFilmRef.setValue(film)
                            .addOnSuccessListener {
                                button.isEnabled = false
                            }
                            .addOnFailureListener {
                                Log.e("Firebase", "Error al añadir la película: ${film.title}")
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al consultar la base de datos: ${error.message}")
            }
        })
    }

}