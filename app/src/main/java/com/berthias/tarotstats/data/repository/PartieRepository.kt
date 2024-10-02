package com.berthias.tarotstats.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berthias.tarotstats.data.dao.PartieDao
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.model.Joueur
import com.berthias.tarotstats.model.Partie
import kotlinx.coroutines.flow.Flow

class PartieRepository(private val partieDao: PartieDao) {
    suspend fun insert(partie: Partie) = partieDao.insert(partie)
    suspend fun delete(partie: Partie) = partieDao.delete(partie)
    fun getPartieById(id: Long): Flow<Partie> = partieDao.getPartieById(id)
    fun getPartiesByCouleur(couleur: CouleurEnum): Flow<List<Partie>> = partieDao.getPartiesByCouleur(couleur)
    fun getPartiesByJoueur(nomJoueur: String): Flow<List<Partie>> = partieDao.getPartiesByJoueur(nomJoueur)
    fun getAllParties(): Flow<List<Partie>> = partieDao.getAllParties()
}