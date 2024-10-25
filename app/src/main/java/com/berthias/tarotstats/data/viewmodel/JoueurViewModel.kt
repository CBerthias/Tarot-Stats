package com.berthias.tarotstats.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berthias.tarotstats.TarotApplication
import com.berthias.tarotstats.model.Joueur
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class JoueurUI(var nom: String) {
    fun toJoueur(): Joueur = Joueur(nom)

    companion object {
        fun fromJoueur(joueur: Joueur) = JoueurUI(joueur.nom)
    }
}

class JoueurViewModel : ViewModel() {
    private val joueurRepository = TarotApplication.application.appContainer.joueurRepository

    val listJoueurs: StateFlow<List<JoueurUI>> = joueurRepository.getAllJoueurs().map { l ->
        val newListe: ArrayList<JoueurUI> = ArrayList()
        l.forEach { j ->
            newListe.add(JoueurUI.fromJoueur(j))
        }
        newListe
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10_000L),
        initialValue = emptyList()
    )

    fun saveJoueur(joueurUI: JoueurUI) {
        viewModelScope.launch {
            joueurRepository.insert(joueurUI.toJoueur())
        }
    }
}