package com.berthias.tarotstats.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berthias.tarotstats.TarotApplication
import com.berthias.tarotstats.model.Joueur
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

data class JoueurUI(var nom: String) {
    fun toJoueur(): Joueur = Joueur(nom)

    companion object {
        fun fromJoueur(joueur: Joueur) = JoueurUI(joueur.nom)
    }
}

class JoueurViewModel : ViewModel() {
    val joueurRepository = TarotApplication.application.appContainer.joueurRepository

    val listJoueurs: StateFlow<List<Joueur>> = joueurRepository.getAllJoueurs().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10_000L),
        initialValue = emptyList()
    )

    suspend fun saveJoueur(joueurUI: JoueurUI) {
        joueurRepository.insert(joueurUI.toJoueur())
    }
}