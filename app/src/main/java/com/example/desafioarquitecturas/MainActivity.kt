package com.example.desafioarquitecturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.example.desafioarquitecturas.data.MoviesRepository
import com.example.desafioarquitecturas.data.local.LocalDataSource
import com.example.desafioarquitecturas.data.local.MoviesDatabase
import com.example.desafioarquitecturas.data.remote.RemoteDataSource
import com.example.desafioarquitecturas.ui.screens.home.Home

class MainActivity : ComponentActivity() {

    private lateinit var db : MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java,
            "movies.db"
        ).build()

        val repository = MoviesRepository(
            localDataSource = LocalDataSource(db.moviesDao()),
            remoteDataSource = RemoteDataSource()
        )

        setContent {
            Home(repository)
        }
    }
}