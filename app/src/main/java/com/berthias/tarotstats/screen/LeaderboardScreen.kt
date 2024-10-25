package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme

object LeaderboardDestination : NavigationDestination {
    override val route: String
        get() = "leaderboard"
    override val title: String
        get() = "Classement"

}

@Composable
fun LeaderboardScreen(
    drawerState: DrawerState, partieViewModel: PartieViewModel = viewModel()
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = LeaderboardDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        val listePartiesUI by partieViewModel.listParties.collectAsState()

        LeaderboardScreenContent(
            modifier = Modifier.padding(innerpadding), listePartiesUI = listePartiesUI
        )
    }
}

@Composable
fun LeaderboardScreenContent(modifier: Modifier = Modifier, listePartiesUI: List<PartieUI>) {
    val nbPartiesJoueur: MutableMap<String, Int> = HashMap()
    val nbWinJoueur: MutableMap<String, Int> = HashMap()
    listePartiesUI.forEach { partieUI ->
        nbPartiesJoueur[partieUI.nomJoueur] =
            nbPartiesJoueur.getOrDefault(partieUI.nomJoueur, 0) + 1
        if (partieUI.gagne) {
            nbWinJoueur[partieUI.nomJoueur] = nbWinJoueur.getOrDefault(partieUI.nomJoueur, 0) + 1
        }
    }
    WinrateLeaderboard(
        modifier = modifier, nbWinJoueur = nbWinJoueur, nbPartiesJoueur = nbPartiesJoueur
    )

}

@Composable
fun WinrateLeaderboard(
    modifier: Modifier = Modifier, nbWinJoueur: Map<String, Int>, nbPartiesJoueur: Map<String, Int>
) {
    val winrateJoueur: MutableMap<String, Float> = HashMap()
    nbPartiesJoueur.forEach { (nomJoueur, nbParties) ->
        winrateJoueur[nomJoueur] =
            (nbWinJoueur.getOrDefault(nomJoueur, 0) / nbParties.toFloat()) * 100
    }
    Column(modifier = modifier) {
        val sortedWinrateJoueur = winrateJoueur.toSortedMap(compareByDescending<String> {
            winrateJoueur[it]
        }.thenComparing(compareByDescending { nbPartiesJoueur[it] }).thenComparing(String::toString)
        )
        for (joueurWinrate in sortedWinrateJoueur) {
            WinrateRow(
                modifier = Modifier.fillMaxWidth(),
                nomJoueur = joueurWinrate.key,
                winrate = joueurWinrate.value,
                nbParties = nbPartiesJoueur.getOrDefault(joueurWinrate.key, 0),
                nbWin = nbWinJoueur.getOrDefault(joueurWinrate.key, 0)
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun WinrateRow(
    modifier: Modifier = Modifier, nomJoueur: String, winrate: Float, nbParties: Int, nbWin: Int
) {
    Column(modifier = modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = nomJoueur,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = buildAnnotatedString {
            append("winrate: ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)) {
                append("%.0f".format(winrate) + "%")
            }
            append(" (%s/%s)".format(nbWin, nbParties))
        })
    }
}

@Preview(showSystemUi = true)
@Composable
fun LeaderboardScreenContentPreview() {
    TarotStatsTheme {
        LeaderboardScreenContent(
            listePartiesUI = listOf(
                PartieUI(1L, "Corentin", CouleurEnum.TREFLE, true, "Tanguy"),
                PartieUI(2L, "Corentin", CouleurEnum.CARREAU, false, "Tanguy"),
                PartieUI(3L, "Tanguy", CouleurEnum.PIQUE, true, "Corentin"),
                PartieUI(4L, "Tanguy", CouleurEnum.PIQUE, false, "Corentin"),
                PartieUI(5L, "Josh", CouleurEnum.PIQUE, false, "Corentin"),
                PartieUI(6L, "Josh", CouleurEnum.PIQUE, true, "Corentin"),
                PartieUI(7L, "Josh", CouleurEnum.PIQUE, false, "Corentin"),
                PartieUI(8L, "Josh", CouleurEnum.PIQUE, true, "Corentin"),
            )
        )
    }
}