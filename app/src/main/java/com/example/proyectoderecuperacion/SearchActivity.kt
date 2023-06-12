package com.example.proyectoderecuperacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoderecuperacion.models.Film
import com.example.proyectoderecuperacion.models.SearchAPI
import com.example.proyectoderecuperacion.models.SearchResponse
import com.example.proyectoderecuperacion.recycler.RecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var tmdbSearchApi: SearchAPI
    private var API_KEY: String = "8ca088e222cacf54a2a3f92efdb34c06"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.etSearchBar)
        searchButton = findViewById(R.id.btnSearch)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        tmdbSearchApi = retrofit.create(SearchAPI::class.java)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        val call = tmdbSearchApi.searchMovies(API_KEY, query)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    val movies = searchResponse?.results // Obtén la lista de películas de la respuesta

                    // Crea una instancia del adaptador RecyclerViewAdapter
                    val adapter = RecyclerViewAdapter(movies as MutableList<Film>, this@SearchActivity)

                    // Configura el RecyclerView
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSearch)
                    recyclerView.layoutManager = GridLayoutManager(this@SearchActivity,2);
                    recyclerView.adapter = adapter

                    // Notifica al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                // Manejar el fallo en la solicitud a la API
            }
        })
    }
}
