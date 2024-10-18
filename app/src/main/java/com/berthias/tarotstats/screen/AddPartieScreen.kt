package com.berthias.tarotstats.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.R
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.JoueurUI
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
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
        val joueurViewModel =
            viewModel<JoueurViewModel>(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return JoueurViewModel() as T
                }
            })

        val partieViewModel =
            viewModel<PartieViewModel>(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PartieViewModel() as T
                }
            })

        val coroutineScope = rememberCoroutineScope()
        val joueurList by joueurViewModel.listJoueurs.collectAsState()

        AddPartieForm(modifier = Modifier.padding(innerpadding),
            joueurList = joueurList,
            onSave = { partieUI ->
                coroutineScope.launch { partieViewModel.savePartie(partieUI) }
            }) { onValidate() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPartieForm(
    modifier: Modifier = Modifier,
    joueurList: List<JoueurUI>,
    onSave: (PartieUI) -> Unit,
    onValidate: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val couleurList: List<CouleurEnum> = CouleurEnum.entries.toList()
        var couleurSelected by remember { mutableStateOf(couleurList[0]) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorButtonSelector(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                image = painterResource(R.drawable.trefle),
                couleur = CouleurEnum.TREFLE,
                selected = couleurSelected == CouleurEnum.TREFLE
            ) { couleurSelected = CouleurEnum.TREFLE }
            ColorButtonSelector(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                image = painterResource(R.drawable.coeur),
                couleur = CouleurEnum.COEUR,
                selected = couleurSelected == CouleurEnum.COEUR
            ) { couleurSelected = CouleurEnum.COEUR }
            ColorButtonSelector(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                image = painterResource(R.drawable.pique),
                couleur = CouleurEnum.PIQUE,
                selected = couleurSelected == CouleurEnum.PIQUE
            ) { couleurSelected = CouleurEnum.PIQUE }
            ColorButtonSelector(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                image = painterResource(R.drawable.carreau),
                couleur = CouleurEnum.CARREAU,
                selected = couleurSelected == CouleurEnum.CARREAU
            ) { couleurSelected = CouleurEnum.CARREAU }
        }

        val joueurStringList =
            if (joueurList.isNotEmpty()) joueurList.map { it.nom } else listOf("")

        var joueurSelected by remember { mutableStateOf(joueurList.getOrNull(0)) }
        DropdownBox(valueList = joueurStringList, label = "Joueur") {
            joueurSelected = joueurList.getOrNull(it)
        }

        var coequipierSelected by remember { mutableStateOf(joueurList.getOrNull(0)) }
        DropdownBox(valueList = joueurStringList, label = "Avec") {
            coequipierSelected = joueurList.getOrNull(it)
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
            0, joueurSelected?.nom, couleurSelected, gagne, coequipierSelected?.nom
        )

        Button(modifier = Modifier.align(Alignment.End), onClick = {

            if (partieUI.nomJoueur != null) {
                onSave(partieUI)
                onValidate()
            } else {
                joueurSelected = joueurList.getOrNull(0)
                if (joueurSelected != null) {
                    partieUI.nomJoueur = joueurSelected!!.nom
                    onSave(partieUI)
                    onValidate()
                }
            }
        }) {
            Text(text = "Enregistrer la partie")
        }
    }
}

@Composable
fun ColorButtonSelector(
    modifier: Modifier = Modifier,
    image: Painter,
    couleur: CouleurEnum,
    selected: Boolean,
    onClick: () -> Unit
) {
    var couleurSelected = MaterialTheme.colorScheme.onPrimaryContainer
    var couleurNotSelected = MaterialTheme.colorScheme.onBackground
    if (couleur == CouleurEnum.COEUR || couleur == CouleurEnum.CARREAU) {
        couleurNotSelected = Color.Red
        couleurSelected = Color.Red
    }
    val backgroundSelected = MaterialTheme.colorScheme.primaryContainer
    val backgroundNotSelected = MaterialTheme.colorScheme.background

    OutlinedIconButton(
        enabled = true,
        modifier = modifier.aspectRatio(1f),
        onClick = onClick,
        colors = IconButtonColors(
            containerColor = if (selected) backgroundSelected else backgroundNotSelected,
            contentColor = if (selected) couleurSelected else couleurNotSelected,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(0.dp, MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(20)
    ) {
        Icon(
            painter = image,
            contentDescription = couleur.stringValue,
            tint = if (selected) couleurSelected else couleurNotSelected
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddPartieFormPreview() {
    TarotStatsTheme {
        AddPartieForm(joueurList = listOf(JoueurUI("Corentin"), JoueurUI("Josh")),
            onSave = {},
            onValidate = {})
    }
}