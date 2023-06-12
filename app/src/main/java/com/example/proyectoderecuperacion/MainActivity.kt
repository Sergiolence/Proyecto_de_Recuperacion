package com.example.proyectoderecuperacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoderecuperacion.recycler.RecyclerViewAdapter
import com.example.proyectoderecuperacion.models.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var llistat: ArrayList<Film> = ArrayList()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val database = FirebaseDatabase.getInstance()
        val filmsRef = database.getReference("films")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2);
        val adapter: RecyclerViewAdapter = RecyclerViewAdapter(llistat, this)
        recyclerView.adapter = adapter

        filmsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    llistat.clear()
                    for (filmSnapshot in dataSnapshot.children) {
                        val film = filmSnapshot.getValue(Film::class.java)
                        if (film != null) {
                            llistat.add(film)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "Failed to read value.", databaseError.toException())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_fav -> {
                val intent: Intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showRecycler(){
        recyclerView.layoutManager = GridLayoutManager(this,2);
        val adapter : RecyclerViewAdapter = RecyclerViewAdapter(llistat, this);
        recyclerView.adapter = adapter
    }
}