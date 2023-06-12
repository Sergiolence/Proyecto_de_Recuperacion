package com.example.proyectoderecuperacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.proyectoderecuperacion.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FilmDetail : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var deleteButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        // Recupera la información de la película pasada en el Intent
        val film = intent.getSerializableExtra("film") as Film

        // Utiliza los elementos del layout para mostrar la información de la película
        val posterImageView = findViewById<ImageView>(R.id.poster)
        val titleTextView = findViewById<TextView>(R.id.filmTitle)
        val votesTextView = findViewById<TextView>(R.id.votesContent)
        val overviewTextView = findViewById<TextView>(R.id.summaryContent)
        val releaseDateTextView = findViewById<TextView>(R.id.dateContent)
        button = findViewById<Button>(R.id.button2)
        deleteButton = findViewById<Button>(R.id.deleteButton)

        Glide.with(this).load("https://image.tmdb.org/t/p/w200/"+film.poster_path).into(posterImageView)
        titleTextView.text = film.title
        votesTextView.text = String.format("%.2f", film.vote_average)
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
                    deleteButton.setOnClickListener {
                        val builder = AlertDialog.Builder(this@FilmDetail)
                        builder.setTitle("Eliminar película")
                        builder.setMessage("¿Estás seguro de que deseas eliminar esta película?")
                        builder.setPositiveButton("Eliminar") { _, _ ->
                            val filmId = film.id.toString()
                            val filmRef = FirebaseDatabase.getInstance().getReference("films").child(filmId)
                            filmRef.removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(this@FilmDetail, "Película eliminada", Toast.LENGTH_SHORT).show()
                                    finish() // Cierra la actividad después de eliminar la película
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@FilmDetail, "Error al eliminar la película", Toast.LENGTH_SHORT).show()
                                }
                        }
                        builder.setNegativeButton("Cancelar", null)
                        val dialog = builder.create()
                        dialog.show()

                    }

                } else {
                    // La peli no existe en firebase
                    button.isEnabled = true
                    button.setOnClickListener {
                        val newFilm = Film()
                        newFilm.id = film.id
                        newFilm.title = film.title
                        newFilm.poster_path =film.poster_path
                        newFilm.release_date = film.release_date
                        newFilm.overview = film.overview
                        newFilm.vote_average = film.vote_average
                        val newFilmRef = FirebaseDatabase.getInstance().getReference("/films/"+newFilm.id)
                        newFilmRef.setValue(film)
                        newFilmRef.push()
                        button.isEnabled = false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al consultar la base de datos: ${error.message}")
            }
        })
    }

}