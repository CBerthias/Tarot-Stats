package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.JoueurUI
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme

object AddJoueurDestination : NavigationDestination {
    override val route: String
        get() = "addJoueur"
    override val title: String
        get() = "Ajout d'un joueur"
}

@Composable
fun AddJoueurScreen(
    drawerState: DrawerState, onValidate: () -> Unit, joueurViewModel: JoueurViewModel = viewModel()
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = AddJoueurDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        AddJoueurForm(modifier = Modifier.padding(innerpadding), onValidate = { joueurUI ->
            joueurViewModel.saveJoueur(joueurUI)
            onValidate()
        })
    }
}

@Composable
fun AddJoueurForm(modifier: Modifier = Modifier, onValidate: (JoueurUI) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    val joueur = JoueurUI(name)
    Column(modifier = modifier) {
        OutlinedTextField(value = name, onValueChange = {
            name = it
        }, label = { Text("Nom du joueur") })
        Button(onClick = {
            if (name.isNotBlank()) {
                onValidate(joueur)
            }
        }) {
            Text("Enregistrer")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddJoueurFormPreview() {
    TarotStatsTheme {
        AddJoueurForm(onValidate = {})
    }
}