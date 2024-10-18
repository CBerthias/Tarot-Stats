package com.berthias.tarotstats.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berthias.tarotstats.TarotApplication
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.model.Partie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.stream.Collectors

data class PartieUI(
    var id: Long,
    var nomJoueur: String?,
    var couleur: CouleurEnum,
    var gagne: Boolean,
    var coequipier: String?
) {
    fun toPartie(): Partie? {
        return if (nomJoueur != null) Partie(
            id, nomJoueur!!, couleur, gagne, coequipier ?: nomJoueur!!
        )
        else null
    }

    companion object {
        fun fromPartie(partie: Partie): PartieUI {
            return PartieUI(
                partie.id, partie.nomJoueur, partie.couleur, partie.gagne, partie.coequipier
            )
        }
    }
}

class PartieViewModel : ViewModel() {
    private val partieRepository = TarotApplication.application.appContainer.partieRepository

    val listParties: StateFlow<List<PartieUI>> = partieRepository.getAllParties().map { l ->
        val newList: ArrayList<PartieUI> = ArrayList()
        l.forEach { p ->
            newList.add(PartieUI.fromPartie(p))
        }
        newList
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10_000L),
        initialValue = emptyList()
    )

    suspend fun savePartie(partieUI: PartieUI) {
        val partie: Partie? = partieUI.toPartie()
        if (partie != null) partieRepository.insert(partie)
    }

    suspend fun deletePartie(partieUI: PartieUI) {
        val partie: Partie? = partieUI.toPartie()
        if (partie != null) partieRepository.delete(partie)
    }

    fun getPartiesForJoueur(joueurUINom: String, parties: List<PartieUI>): List<PartieUI> {
        return parties.stream().filter { partie ->
            partie.nomJoueur == joueurUINom
        }.collect(Collectors.toList())
    }
}