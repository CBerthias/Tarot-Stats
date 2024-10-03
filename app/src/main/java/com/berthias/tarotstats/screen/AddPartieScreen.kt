package com.berthias.tarotstats.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.util.DropdownBox
import kotlinx.coroutines.launch

object AddPartieDestination : NavigationDestination {
    override val route: String
        get() = "AddPartie"
    override val title: String
        get() = "Ajouter une partie"

}

@Composable
fun AddPartieScreen(drawerState: DrawerState, onValidate: () -> Unit) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = AddPartieDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        AddPartieForm(modifier = Modifier.padding(innerpadding)) { onValidate() }
    }
}

@Composable
fun AddPartieForm(modifier: Modifier = Modifier, onValidate: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val joueurViewModel = viewModel<JoueurViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JoueurViewModel() as T
        }
    })

    val partieViewModel = viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PartieViewModel() as T
        }
    })

    Column(modifier = modifier.fillMaxWidth()) {
        val couleurList: List<CouleurEnum> = CouleurEnum.entries.toList()
        val couleurStringList: List<String> = couleurList.map { it.stringValue }
        var couleurSelected by remember { mutableStateOf(couleurList[0]) }
        DropdownBox(valueList = couleurStringList, label = "Roi appelé") {
            couleurSelected = couleurList[it]
        }

        val joueurList by joueurViewModel.listJoueurs.collectAsState()
        val joueurStringList =
            if (joueurList.isNotEmpty()) joueurList.map { it.nom } else listOf("")
        var joueurSelected by remember { mutableStateOf(joueurList.getOrNull(0)) }
        DropdownBox(valueList = joueurStringList, label = "Joueur") {
            joueurSelected = joueurList.getOrNull(it)
        }

        var gagne by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .height(40.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically),
                text = "Victoire ? "
            )
            Switch(modifier = Modifier.fillMaxHeight(),
                checked = gagne,
                onCheckedChange = { gagne = it })
        }

        val partieUI = PartieUI(
            0, joueurSelected?.nom, couleurSelected, gagne
        )

        Button(modifier = Modifier.align(Alignment.End), onClick = {
            coroutineScope.launch {
                if (partieUI.nomJoueur != null) {
                    partieViewModel.savePartie(partieUI)
                    onValidate()
                } else {
                    joueurSelected = joueurList.getOrNull(0)
                    if (joueurSelected != null) {
                        partieUI.nomJoueur = joueurSelected!!.nom
                        partieViewModel.savePartie(partieUI)
                        onValidate()
                    }
                }
            }
        }) {
            Text(text = "Enregistrer la partie")
        }
    }
}