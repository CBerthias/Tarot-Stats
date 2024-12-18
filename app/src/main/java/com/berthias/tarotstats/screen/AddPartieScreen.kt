package com.berthias.tarotstats.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.JoueurUI
import com.berthias.tarotstats.data.viewmodel.JoueurViewModel
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import com.berthias.tarotstats.util.DropdownBox
import com.berthias.tarotstats.util.painterByColor

object AddPartieDestination : NavigationDestination {
    override val route: String
        get() = "AddPartie"
    override val title: String
        get() = "Ajouter une partie"

}

@Composable
fun AddPartieScreen(
    drawerState: DrawerState,
    onValidate: () -> Unit,
    joueurViewModel: JoueurViewModel = viewModel(),
    partieViewModel: PartieViewModel = viewModel()
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = AddPartieDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        val joueurList by joueurViewModel.listJoueurs.collectAsState()

        AddPartieForm(modifier = Modifier.padding(innerpadding),
            joueurList = joueurList,
            onSave = { partieUI ->
                partieViewModel.savePartie(partieUI)
            }) { onValidate() }
    }
}

@Composable
fun AddPartieForm(
    modifier: Modifier = Modifier,
    joueurList: List<JoueurUI>,
    onSave: (PartieUI) -> Unit,
    onValidate: () -> Unit
) {
    val sortedJoueurUIListe = joueurList.sortedWith(compareBy(JoueurUI::nom))
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val couleurList: List<CouleurEnum> = CouleurEnum.entries.toList()
        var couleurSelected by remember { mutableStateOf(couleurList[0]) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 30.dp, max = 100.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CouleurEnum.entries.forEach { couleur ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    val painter = painterByColor(couleur)
                    ColorButtonSelector(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.Center),
                        image = painter,
                        couleur = couleur,
                        selected = couleurSelected == couleur
                    ) { couleurSelected = couleur }
                }
            }
        }

        val joueurStringList =
            if (sortedJoueurUIListe.isNotEmpty()) sortedJoueurUIListe.map { it.nom } else listOf("")

        var joueurSelected by remember { mutableStateOf(sortedJoueurUIListe.getOrNull(0)) }
        DropdownBox(valueList = joueurStringList, label = "Joueur") {
            joueurSelected = sortedJoueurUIListe.getOrNull(it)
        }

        var coequipierSelected by remember { mutableStateOf(sortedJoueurUIListe.getOrNull(0)) }
        DropdownBox(valueList = joueurStringList, label = "Avec") {
            coequipierSelected = sortedJoueurUIListe.getOrNull(it)
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
            0, joueurSelected?.nom ?: "", couleurSelected, gagne, coequipierSelected?.nom ?: ""
        )

        Button(modifier = Modifier.align(Alignment.End), onClick = {
            if (joueurSelected == null) {
                joueurSelected = sortedJoueurUIListe.getOrNull(0)
                partieUI.nomJoueur = joueurSelected?.nom ?: ""
            }
            if (coequipierSelected == null) {
                coequipierSelected = sortedJoueurUIListe.getOrNull(0)
                partieUI.coequipier = coequipierSelected?.nom ?: ""
            }
            if (partieUI.nomJoueur.isNotBlank() && partieUI.coequipier.isNotBlank()) {
                onSave(partieUI)
                onValidate()
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

@Preview(
    showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420,orientation=portrait"
)
@Composable
fun AddPartieFormPreview() {
    TarotStatsTheme {
        AddPartieForm(joueurList = listOf(
            JoueurUI("Corentin"), JoueurUI("Josh"), JoueurUI("Benoit"), JoueurUI("Kevin")
        ), onSave = {}, onValidate = {})
    }
}