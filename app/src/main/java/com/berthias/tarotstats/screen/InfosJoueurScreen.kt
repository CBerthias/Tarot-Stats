package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import java.util.EnumMap

object InfosJoueurDestination : NavigationDestination {
    override var route: String = "infosJoueur/"
    fun getRoute(joueur: String) = route + joueur
    override var title: String = "Infos de "
    fun getTitle(joueur: String) = title + joueur

}

@Composable
fun InfosJoueurScreen(drawerState: DrawerState, joueurUINom: String) {

    val partieViewModel = viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PartieViewModel() as T
        }
    })

    val partiesUI: List<PartieUI> by partieViewModel.listParties.collectAsState()
    val partiesForJoueur = partieViewModel.getPartiesForJoueur(joueurUINom, partiesUI)

    Scaffold(topBar = {
        TarotTopAppBar(
            title = InfosJoueurDestination.getTitle(joueurUINom), drawerState = drawerState
        )
    }) { innerpadding ->
        StatsJoueur(
            modifier = Modifier
                .padding(innerpadding)
                .padding(8.dp),
            parties = partiesForJoueur
        )
    }
}

@Composable
fun StatsJoueur(modifier: Modifier = Modifier, parties: List<PartieUI>) {
    var gagnees = 0
    val nbPartiesRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    val nbWinRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    for (partie in parties) {
        if (partie.gagne) {
            gagnees++
            nbWinRoi[partie.couleur] = nbWinRoi.getOrDefault(partie.couleur, 0) + 1
        }
        nbPartiesRoi[partie.couleur] = nbPartiesRoi.getOrDefault(partie.couleur, 0) + 1
    }
    Column(modifier = modifier) {
        Text(
            text = "Winrate global: %.0f".format(gagnees.div(parties.size.toFloat()) * 100) + "%",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier
                .height(30.dp)
                .wrapContentHeight(Alignment.CenterVertically)
        )
        HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        for (couleur in CouleurEnum.entries) {
            val winrate: Float = nbWinRoi.getOrDefault(couleur, 0)
                .div(nbPartiesRoi.getOrDefault(couleur, 0).toFloat()) * 100
            Text(text = "Winrate Roi de %s appelé: %.0f".format(couleur.stringValue, winrate) + "%")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun StatsJoueurPreview() {
    TarotStatsTheme {
        StatsJoueur(
            parties = listOf(
                PartieUI(1L, "Corentin", CouleurEnum.TREFLE, true),
                PartieUI(2L, "Corentin", CouleurEnum.TREFLE, false),
                PartieUI(3L, "Corentin", CouleurEnum.CARREAU, true),
            )
        )
    }
}