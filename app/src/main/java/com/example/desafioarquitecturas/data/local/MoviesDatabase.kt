package com.example.desafioarquitecturas.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.desafioarquitecturas.data.Movie

// ROOM necesita: base de datos, dao y entidad

@Database(entities = [LocalMovie::class], version = 1) // crea tabal usando la data class LocalMovie
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}

@Dao
interface MoviesDao { // dao para conexion a base de datos
    @Query("SELECT * FROM LocalMovie")
    suspend fun getMovies(): List<LocalMovie>

    @Insert
    suspend fun insertAll(movies: List<LocalMovie>)

    @Update
    suspend fun updateMovie(movie: LocalMovie)

    @Query("SELECT COUNT(*) FROM LocalMovie")
    suspend fun count(): Int
}

@Entity
data class LocalMovie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val favorite: Boolean = false
)



fun LocalMovie.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favorite = favorite
)