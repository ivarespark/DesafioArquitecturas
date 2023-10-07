package com.example.desafioarquitecturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.example.desafioarquitecturas.data.local.MoviesDatabase
import com.example.desafioarquitecturas.ui.screens.home.Home

class MainActivity : ComponentActivity() {

    val db = Room.databaseBuilder(
        applicationContext,
        MoviesDatabase::class.java,
        "movies.db"
    ).build()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home(db.moviesDao())
        }
    }
}