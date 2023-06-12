package com.example.proyectoderecuperacion.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoderecuperacion.models.Film
import com.example.proyectoderecuperacion.FilmDetail
import com.example.proyectoderecuperacion.R
import java.io.Serializable

class RecyclerViewAdapter (llistat: MutableList<Film>, context: Context): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    var llistat: MutableList<Film> = llistat;
    var context: Context = context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFilm = llistat[position]

        // Carga la imagen usando Glide
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w200/"+currentFilm.poster_path)
            .placeholder(R.drawable.starwars)
            .into(holder.image)

        holder.name.text = currentFilm.title

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FilmDetail::class.java)
            intent.putExtra("film", currentFilm as Serializable)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return llistat.size;
    }

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.film_name)
        val image: ImageView = view.findViewById((R.id.film_image))
    }
}
