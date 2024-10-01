package com.berthias.tarotstats.data.repository

import com.berthias.tarotstats.data.dao.JoueurDao
import com.berthias.tarotstats.model.Joueur
import kotlinx.coroutines.flow.Flow

class JoueurRepository(private val joueurDao: JoueurDao) {

    suspend fun insert(joueur: Joueur) = joueurDao.insert(joueur)
    suspend fun delete(joueur: Joueur) = joueurDao.delete(joueur)
    fun getJoueurByNom(nom: String): Flow<Joueur?> = joueurDao.getJoueurByNom(nom)
    fun getAllJoueurs(): Flow<List<Joueur>> = joueurDao.getAllJoueurs()

}