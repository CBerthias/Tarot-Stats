package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.navigation.NavigationDestination

object ListePartiesDestination : NavigationDestination {
    override val route: String
        get() = "ListeParties"
    override val title: String
        get() = "Liste des parties"
}

@Composable
fun ListePartiesScreen(modifier: Modifier = Modifier, navigateToAddPartie: () -> Unit) {
    Box(modifier = modifier) {
        ListeParties()
        FloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp),
            onClick = {
                navigateToAddPartie()
            }) {
            Icon(Icons.Filled.Add, "add button")
        }
    }
}

@Composable
fun ListeParties(modifier: Modifier = Modifier) {
    val partieViewModel = viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PartieViewModel() as T
        }
    })

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.height(30.dp)) {
            Text(
                text = "Joueur",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            Text(
                text = "Roi",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            Text(
                text = "Victoire",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            )
        }
        val scrollState = rememberScrollState();
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(scrollState)
        ) {
            val listeParties by partieViewModel.listParties.collectAsState()
            listeParties.forEach { partie ->
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                Row(modifier = Modifier.height(30.dp)) {
                    Text(
                        partie.nomJoueur,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Text(
                        partie.couleur.stringValue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Icon(
                        if (partie.gagne) Icons.Filled.Star else Icons.Filled.Close,
                        "victoire",
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}