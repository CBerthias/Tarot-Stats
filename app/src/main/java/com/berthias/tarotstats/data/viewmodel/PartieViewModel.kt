package com.berthias.tarotstats.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berthias.tarotstats.TarotApplication
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.model.Joueur
import com.berthias.tarotstats.model.Partie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

data class PartieUI(
    val nomJoueur: String?, val couleur: CouleurEnum?, val gagne: Boolean
) {
    fun toPartie(): Partie? {
        return if (nomJoueur != null && couleur!= null)
            Partie(0, nomJoueur, couleur, gagne)
        else
            null
    }

    companion object {
        fun fromPartie(partie: Partie): PartieUI {
            return PartieUI(partie.nomJoueur, partie.couleur, partie.gagne)
        }
    }
}

class PartieViewModel: ViewModel() {
    private val partieRepository = TarotApplication.application.appContainer.partieRepository

    val listParties: StateFlow<List<Partie>> = partieRepository.getAllParties().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10_000L),
        initialValue = emptyList()
    )

    suspend fun savePartie(partieUI: PartieUI) {
        val partie: Partie? = partieUI.toPartie()
        if (partie != null)
            partieRepository.insert(partie)
    }
}