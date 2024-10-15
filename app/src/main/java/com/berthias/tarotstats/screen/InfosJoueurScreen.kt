package com.berthias.tarotstats.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.R
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import com.berthias.tarotstats.util.Winrates

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
                .padding(8.dp), parties = partiesForJoueur
        )
    }
}

@Composable
fun StatsJoueur(modifier: Modifier = Modifier, parties: List<PartieUI>) {
    Column(modifier = modifier) {
        RawValues(parties = parties)
        Winrates(parties = parties)
    }
}

@Composable
fun RawValues(modifier: Modifier = Modifier, parties: List<PartieUI>) {
    val nbParties = parties.size
    val nbWin = parties.stream().filter { partie ->
        partie.gagne
    }.count()
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Parties jouées",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = nbParties.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val trophee = painterResource(R.drawable.trophee)
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = trophee,
                    contentDescription = "Victoires"
                )
                Text(text = nbWin.toString(), fontSize = 40.sp)
            }
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    val trophee = painterResource(R.drawable.trophee)
                    Image(
                        modifier = Modifier.size(25.dp),
                        painter = trophee,
                        contentDescription = "Victoires"
                    )
                    Text(text = "%", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
                }
                var winrate: Float = nbWin.div(nbParties.toFloat()) * 100
                if (winrate.isNaN()) winrate = 0F
                Text(text = "%.0f".format(winrate) + "%", fontSize = 35.sp)
            }
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
                PartieUI(2L, "Corentin", CouleurEnum.TREFLE, true),
                PartieUI(3L, "Corentin", CouleurEnum.CARREAU, true),
            )
        )
    }
}