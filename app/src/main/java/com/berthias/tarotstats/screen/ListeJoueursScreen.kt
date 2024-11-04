package com.berthias.tarotstats.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.JoueurUI
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme

object ListeJoueursDestination : NavigationDestination {
    override val route: String
        get() = "listeJoueurs"
    override val title: String
        get() = "Liste des joueurs"

}

@Composable
fun ListeJoueursScreen(
    drawerState: DrawerState,
    navigateToAddJoueur: () -> Unit,
    navigateToInfosJoueur: (JoueurUI) -> Unit,
    navigateToLeaderboard: () -> Unit,
    joueurViewModel: JoueurViewModel = viewModel()
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = ListeJoueursDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        val listeJoueurs by joueurViewModel.listJoueurs.collectAsState()

        ListeJoueursScreenContent(
            modifier = Modifier.padding(innerpadding),
            listeJoueurs,
            navigateToInfosJoueur,
            navigateToAddJoueur,
            navigateToLeaderboard
        )
    }
}

@Composable
fun ListeJoueursScreenContent(
    modifier: Modifier = Modifier,
    listeJoueurs: List<JoueurUI>,
    navigateToInfosJoueur: (JoueurUI) -> Unit,
    navigateToAddJoueur: () -> Unit,
    navigateToLeaderboard: () -> Unit
) {
    Box(modifier = modifier) {
        Column {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
                shape = AbsoluteCutCornerShape(0),
                onClick = {
                    navigateToLeaderboard()
                }) {
                Text(text = "Classement", fontSize = 20.sp)
            }
            ListeJoueurs(
                modifier = Modifier.fillMaxSize(),
                listeJoueurs = listeJoueurs,
                navigateToInfosJoueur = navigateToInfosJoueur
            )
        }
        FloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp),
            onClick = {
                navigateToAddJoueur()
            }) {
            Icon(Icons.Filled.Add, "add button")
        }
    }
}

@Composable
fun ListeJoueurs(
    modifier: Modifier = Modifier,
    listeJoueurs: List<JoueurUI>,
    navigateToInfosJoueur: (JoueurUI) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        listeJoueurs.sortedWith(compareBy(JoueurUI::nom)).forEach { joueur ->
            RowJoueur(joueurUI = joueur, navigateToInfosJoueur = navigateToInfosJoueur)
            HorizontalDivider()
        }
    }
}

@Composable
fun RowJoueur(
    modifier: Modifier = Modifier, joueurUI: JoueurUI, navigateToInfosJoueur: (JoueurUI) -> Unit
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable { navigateToInfosJoueur(joueurUI) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(start = 16.dp), text = joueurUI.nom, fontSize = 23.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ListeJoueursScreenContentPreview() {
    TarotStatsTheme {
        ListeJoueursScreenContent(listeJoueurs = listOf(
            JoueurUI("Corentin"), JoueurUI("Julien"), JoueurUI("Josh"), JoueurUI("Benoit")
        ), navigateToInfosJoueur = {}, navigateToAddJoueur = {}, navigateToLeaderboard = {})
    }
}