package com.berthias.tarotstats.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berthias.tarotstats.model.Joueur
import kotlinx.coroutines.flow.Flow

@Dao
interface JoueurDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(joueur: Joueur)

    @Delete
    suspend fun delete(joueur: Joueur)

    @Query("SELECT * FROM joueur j WHERE nom = :nom")
    fun getJoueurByNom(nom: String): Flow<Joueur>

    @Query("SELECT * FROM joueur")
    fun getAllJoueurs(): Flow<List<Joueur>>
}