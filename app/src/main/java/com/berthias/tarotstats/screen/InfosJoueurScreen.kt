package com.berthias.tarotstats.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.berthias.tarotstats.util.ResizableText
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
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResizableText(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.Bottom),
                    text = "Parties jouées",
                    style = TextStyle(
                        textAlign = TextAlign.Center, fontSize = 30.sp
                    )
                )
                ResizableText(
                    modifier = Modifier
                        .weight(1.5f)
                        .align(Alignment.CenterHorizontally),
                    text = nbParties.toString(),
                    style = TextStyle(
                        textAlign = TextAlign.Center, fontSize = 50.sp
                    )
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val trophee = painterResource(R.drawable.trophee)
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    painter = trophee,
                    contentDescription = "Victoires"
                )
                ResizableText(
                    modifier = Modifier.weight(1.5f),
                    text = nbWin.toString(),
                    style = TextStyle(fontSize = 50.sp)
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    val trophee = painterResource(R.drawable.trophee)
                    Image(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        painter = trophee,
                        contentDescription = "Victoires"
                    )
                    ResizableText(
                        text = "%",
                        style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.SemiBold)
                    )
                }
                val winrate: Float = nbWin.div(nbParties.toFloat()) * 100
                val winrateString = if (winrate.isNaN()) "-%" else "%.0f".format(winrate) + "%"
                ResizableText(
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(4.dp),
                    text = winrateString,
                    style = TextStyle(fontSize = 50.sp)
                )
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
                PartieUI(1L, "Corentin", CouleurEnum.TREFLE, true, "Josh"),
                PartieUI(2L, "Corentin", CouleurEnum.TREFLE, true, "Thibault"),
                PartieUI(3L, "Corentin", CouleurEnum.CARREAU, true, "Tanguy")
            )
        )
    }
}