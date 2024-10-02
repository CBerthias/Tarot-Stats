package com.berthias.tarotstats.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.model.Joueur
import com.berthias.tarotstats.model.Partie
import kotlinx.coroutines.flow.Flow

@Dao
interface PartieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(partie: Partie)

    @Delete
    suspend fun delete(partie: Partie)

    @Query("SELECT * FROM partie WHERE id = :id")
    fun getPartieById(id: Long): Flow<Partie>

    @Query("SELECT * FROM partie WHERE couleur = :couleur")
    fun getPartiesByCouleur(couleur: CouleurEnum): Flow<List<Partie>>

    @Query("SELECT * FROM partie WHERE nomJoueur = :nomJoueur")
    fun getPartiesByJoueur(nomJoueur: String): Flow<List<Partie>>

    @Query("SELECT * FROM partie")
    fun getAllParties(): Flow<List<Partie>>
}