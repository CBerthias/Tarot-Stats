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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.navigation.NavigationDestination
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
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
        ) {
            Winrates(modifier = Modifier.padding(16.dp), parties = listeParties)
        }
    }
}