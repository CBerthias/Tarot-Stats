package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ListePartiesDestination : NavigationDestination {
    override val route: String
        get() = "ListeParties"
    override val title: String
        get() = "Liste des parties"
}

@Composable
fun ListePartiesScreen(drawerState: DrawerState, navigateToAddPartie: () -> Unit) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = ListePartiesDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding)) {
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
}

@Composable
fun ListeParties(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
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
            Spacer(modifier = Modifier.width(32.dp))
        }
        val scrollState = rememberScrollState();
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(scrollState)
        ) {
            val listePartiesUI by partieViewModel.listParties.collectAsState()
            listePartiesUI.forEach { partieUI ->
                var expanded by remember { mutableStateOf(false) }
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 32.dp))
                Row(
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        partieUI.nomJoueur!!,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Text(
                        partieUI.couleur.stringValue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    VerticalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Icon(
                        if (partieUI.gagne) Icons.Filled.Star else Icons.Filled.Close,
                        "victoire",
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    Box(modifier = Modifier.width(32.dp)) {
                        IconButton(onClick = {
                            expanded = true
                        }) {
                            Icon(Icons.Filled.MoreVert, "more options")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text(text = "Supprimer") }, onClick = {
                                coroutineScope.launch {
                                    partieViewModel.deletePartie(partieUI)
                                }
                                expanded = false
                            })
                        }
                    }
                }
            }
        }
    }
}