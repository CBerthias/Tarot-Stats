package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import com.berthias.tarotstats.util.Winrates

object HomeDestination : NavigationDestination {
    override val route: String
        get() = "Home"
    override val title: String
        get() = "Stats"
}

@Composable
fun HomeScreen(drawerState: DrawerState) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = HomeDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        val partieViewModel =
            viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PartieViewModel() as T
                }
            })
        val listeParties by partieViewModel.listParties.collectAsState()
        HomePageContent(modifier = Modifier.padding(innerpadding), listeParties = listeParties)
    }
}

@Composable
fun HomePageContent(modifier: Modifier = Modifier, listeParties: List<PartieUI>) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Winrates(modifier = Modifier.padding(16.dp), parties = listeParties)
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePagePreview() {
    TarotStatsTheme {
        HomePageContent(
            listeParties = listOf(
                PartieUI(1L, "Corentin", CouleurEnum.TREFLE, true, "Josh"),
                PartieUI(2L, "Corentin", CouleurEnum.TREFLE, true, "Thibault"),
                PartieUI(3L, "Corentin", CouleurEnum.CARREAU, true, "Tanguy")
            )
        )
    }
}