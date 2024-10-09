package com.berthias.tarotstats.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    navigateToInfosJoueur: (JoueurUI) -> Unit
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = ListeJoueursDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding)) {
            ListeJoueurs(
                modifier = Modifier.fillMaxSize(), navigateToInfosJoueur = navigateToInfosJoueur
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    navigateToAddJoueur()
                }) {
                Icon(Icons.Filled.Add, "add button")
            }
        }
    }
}

@Composable
fun ListeJoueurs(modifier: Modifier = Modifier, navigateToInfosJoueur: (JoueurUI) -> Unit) {
    val joueurViewModel = viewModel<JoueurViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JoueurViewModel() as T
        }
    })
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState)) {
        val listJoueurs by joueurViewModel.listJoueurs.collectAsState()
        for (joueur: JoueurUI in listJoueurs) {
            RowJoueur(joueurUI = joueur, navigateToInfosJoueur = navigateToInfosJoueur)
        }
    }
}

@Composable
fun RowJoueur(
    modifier: Modifier = Modifier, joueurUI: JoueurUI, navigateToInfosJoueur: (JoueurUI) -> Unit
) {
    Row(modifier = modifier
        .height(40.dp)
        .fillMaxWidth()
        .wrapContentHeight(Alignment.CenterVertically)
        .clickable { navigateToInfosJoueur(joueurUI) }) {
        Text(modifier = Modifier.padding(start = 16.dp), text = joueurUI.nom, fontSize = 20.sp)
    }
}

@Preview
@Composable
fun RowJoueurPreview() {
    TarotStatsTheme {
        RowJoueur(modifier = Modifier, joueurUI = JoueurUI("Corentin"), navigateToInfosJoueur = {})
    }
}