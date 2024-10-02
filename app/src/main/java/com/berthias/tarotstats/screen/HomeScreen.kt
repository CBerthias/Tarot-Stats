package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.model.Partie
import com.berthias.tarotstats.navigation.NavigationDestination
import java.util.EnumMap

object HomeDestination : NavigationDestination {
    override val route: String
        get() = "Home"
    override val title: String
        get() = "Accueil"
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val partieViewModel = viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PartieViewModel() as T
        }
    })
    val listeParties by partieViewModel.listParties.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {
        PickRateRoi(modifier = Modifier.padding(16.dp), listeParties = listeParties)
        HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        WinRateByRoi(modifier = Modifier.padding(16.dp), listeParties = listeParties)
    }

}

@Composable
fun PickRateRoi(modifier: Modifier = Modifier, listeParties: List<Partie>) {
    val picksByRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)

    listeParties.forEach { partie ->
        picksByRoi[partie.couleur] = picksByRoi.getOrDefault(partie.couleur, 0) + 1
    }

    Column(modifier = modifier) {
        Text(text = "Nombre de parties par roi", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        for (couleur in CouleurEnum.entries) {
            Text(text = "${couleur.stringValue}: ${picksByRoi[couleur] ?: 0}")
        }
    }
}

@Composable
fun WinRateByRoi(modifier: Modifier = Modifier, listeParties: List<Partie>) {
    val winByRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    val picksByRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    listeParties.forEach { partie ->
        picksByRoi[partie.couleur] = picksByRoi.getOrDefault(partie.couleur, 0) + 1
        if (partie.gagne) {
            winByRoi[partie.couleur] = winByRoi.getOrDefault(partie.couleur, 0) + 1
        }
    }

    Column(modifier = modifier) {
        Text(text = "Winrate par roi", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        for (couleur in CouleurEnum.entries) {
            val winRate: Float =
                (winByRoi[couleur]?.div(picksByRoi[couleur]?.toFloat() ?: 0F) ?: 0F) * 100F
            Text(text = "${couleur.stringValue}: ${"%.2f".format(winRate)}%")
        }
    }
}