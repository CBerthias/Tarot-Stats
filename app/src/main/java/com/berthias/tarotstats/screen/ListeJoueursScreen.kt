package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.model.Joueur
import com.berthias.tarotstats.navigation.NavigationDestination

object ListeJoueursDestination : NavigationDestination {
    override val route: String
        get() = "listeJoueurs"
    override val title: String
        get() = "Liste des joueurs"

}

@Composable
fun ListeJoueursScreen(modifier: Modifier = Modifier, navigateToAddJoueur: () -> Unit) {
    Box(modifier = modifier) {
        ListeJoueurs(modifier = Modifier.fillMaxSize())
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

@Composable
fun ListeJoueurs(modifier: Modifier = Modifier) {
    val joueurViewModel = viewModel<JoueurViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JoueurViewModel() as T
        }
    })
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState)) {
        val listJoueurs by joueurViewModel.listJoueurs.collectAsState()
        for (joueur: Joueur in listJoueurs) {
            Row {
                Text(joueur.nom)
            }
        }
    }
}