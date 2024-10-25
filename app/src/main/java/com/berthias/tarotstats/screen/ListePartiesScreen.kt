package com.berthias.tarotstats.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.berthias.tarotstats.R
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.data.viewmodel.PartieViewModel
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import com.berthias.tarotstats.util.ResizableText

object ListePartiesDestination : NavigationDestination {
    override val route: String
        get() = "ListeParties"
    override val title: String
        get() = "Liste des parties"
}

@Composable
fun ListePartiesScreen(
    drawerState: DrawerState,
    navigateToAddPartie: () -> Unit,
    partieViewModel: PartieViewModel = viewModel()
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = ListePartiesDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        val listePartiesUI by partieViewModel.listParties.collectAsState()

        Box(
            modifier = Modifier
                .padding(innerpadding)
                .padding(4.dp)
        ) {
            ListePartiesContent(listePartiesUI = listePartiesUI, onDelete = { partieUI ->
                partieViewModel.deletePartie(partieUI)
            })
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onClick = {
                    navigateToAddPartie()
                },
                shape = RoundedCornerShape(100)
            ) {
                Icon(Icons.Filled.Add, "add button")
            }
        }
    }
}

@Composable
fun ListePartiesContent(
    modifier: Modifier = Modifier, listePartiesUI: List<PartieUI>, onDelete: (PartieUI) -> Unit
) {

    var joueurOrder by remember { mutableStateOf(OrderTri.NONE) }
    var roiOrder by remember { mutableStateOf(OrderTri.NONE) }
    var victoireOrder by remember { mutableStateOf(OrderTri.NONE) }

    var sortedListParties = listePartiesUI.subList(0, listePartiesUI.size).reversed()
    sortedListParties = getSortedListPartie(joueurOrder, roiOrder, victoireOrder, sortedListParties)

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.height(50.dp)) {
            var resizedTextStyle: TextStyle by remember {
                mutableStateOf(
                    TextStyle(
                        fontSize = 23.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )
            }
            var shouldDraw: Boolean by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .weight(1F)
                    .clickable {
                        joueurOrder = rotateOrder(joueurOrder)
                        roiOrder = OrderTri.NONE
                        victoireOrder = OrderTri.NONE
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText(modifier = Modifier.weight(1F),
                    text = "Joueur",
                    shouldDraw = shouldDraw,
                    resizedTextStyle = resizedTextStyle,
                    onModifyTextStyle = { resizedTextStyle = it },
                    onModifyShouldDraw = { shouldDraw = it })
                Icon(
                    modifier = Modifier.alpha(if (joueurOrder == OrderTri.NONE) 0f else 1f),
                    imageVector = getIconOrder(joueurOrder),
                    contentDescription = "order $joueurOrder"
                )
            }
            Row(
                modifier = Modifier
                    .weight(1F)
                    .clickable {
                        joueurOrder = OrderTri.NONE
                        roiOrder = rotateOrder(roiOrder)
                        victoireOrder = OrderTri.NONE
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText(modifier = Modifier.weight(1F),
                    text = "Roi",
                    shouldDraw = shouldDraw,
                    resizedTextStyle = resizedTextStyle,
                    onModifyTextStyle = { resizedTextStyle = it },
                    onModifyShouldDraw = { shouldDraw = it })
                Icon(
                    modifier = Modifier.alpha(if (roiOrder == OrderTri.NONE) 0f else 1f),
                    imageVector = getIconOrder(roiOrder),
                    contentDescription = "order $roiOrder"
                )
            }
            Row(
                modifier = Modifier
                    .weight(1F)
                    .clickable {
                        joueurOrder = OrderTri.NONE
                        roiOrder = OrderTri.NONE
                        victoireOrder = rotateOrder(victoireOrder)
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText(modifier = Modifier.weight(1F),
                    text = "Victoire",
                    shouldDraw = shouldDraw,
                    resizedTextStyle = resizedTextStyle,
                    onModifyTextStyle = { resizedTextStyle = it },
                    onModifyShouldDraw = { shouldDraw = it })
                Icon(
                    modifier = Modifier.alpha(if (victoireOrder == OrderTri.NONE) 0f else 1f),
                    imageVector = getIconOrder(victoireOrder),
                    contentDescription = "order $victoireOrder"
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
        }
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(scrollState)
        ) {
            sortedListParties.forEach { partieUI ->
                RowPartie(partieUI = partieUI, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    shouldDraw: Boolean,
    resizedTextStyle: TextStyle,
    onModifyTextStyle: (TextStyle) -> Unit,
    onModifyShouldDraw: (Boolean) -> Unit
) {
    Text(modifier = modifier
        .fillMaxHeight()
        .wrapContentHeight(Alignment.CenterVertically)
        .drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        }, text = text, style = resizedTextStyle, softWrap = false, onTextLayout = { result ->
        if (result.didOverflowWidth || result.didOverflowHeight) {
            onModifyTextStyle(resizedTextStyle.copy(fontSize = resizedTextStyle.fontSize * 0.95f))
            onModifyShouldDraw(false)
        } else {
            onModifyShouldDraw(true)
        }
    })
}

@Composable
fun RowPartie(modifier: Modifier = Modifier, partieUI: PartieUI, onDelete: (PartieUI) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    HorizontalDivider(modifier = modifier)
    Row(
        modifier = Modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val painter = when (partieUI.couleur) {
            CouleurEnum.CARREAU -> painterResource(R.drawable.carreau)
            CouleurEnum.COEUR -> painterResource(R.drawable.coeur)
            CouleurEnum.PIQUE -> painterResource(R.drawable.pique)
            CouleurEnum.TREFLE -> painterResource(R.drawable.trefle)
        }
        val tint = when (partieUI.couleur) {
            CouleurEnum.CARREAU -> Color.Red
            CouleurEnum.COEUR -> Color.Red
            CouleurEnum.PIQUE -> MaterialTheme.colorScheme.onPrimaryContainer
            CouleurEnum.TREFLE -> MaterialTheme.colorScheme.onPrimaryContainer
        }
        ResizableText(
            text = partieUI.nomJoueur,
            style = TextStyle(
                fontSize = 20.sp, textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)
        )
        Icon(
            painter = painter,
            contentDescription = "victoire",
            tint = tint,
            modifier = Modifier
                .weight(1F)
                .padding(4.dp)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)
        )
        Icon(
            if (partieUI.gagne) Icons.Filled.Star else Icons.Filled.Close,
            "victoire",
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)
        )
        Box(modifier = Modifier.width(32.dp)) {
            IconButton(modifier = Modifier.aspectRatio(1f), onClick = {
                expanded = true
            }) {
                Icon(Icons.Filled.MoreVert, "more options")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text(text = "Supprimer") }, onClick = {
                    onDelete(partieUI)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListePartiesContentPreview() {
    TarotStatsTheme {
        ListePartiesContent(
            listePartiesUI = listOf(
                PartieUI(1L, "Corentin", CouleurEnum.TREFLE, true, "Josh"),
                PartieUI(2L, "Corentin", CouleurEnum.TREFLE, true, "Thibault"),
                PartieUI(3L, "Corentin", CouleurEnum.CARREAU, true, "Tanguy")
            )
        ) { }
    }
}

private fun rotateOrder(order: OrderTri): OrderTri {
    return when (order) {
        OrderTri.NONE -> OrderTri.ASC
        OrderTri.ASC -> OrderTri.DESC
        OrderTri.DESC -> OrderTri.NONE
    }
}

private fun getIconOrder(order: OrderTri): ImageVector {
    return when (order) {
        OrderTri.NONE -> Icons.Filled.ArrowDropDown
        OrderTri.ASC -> Icons.Filled.KeyboardArrowUp
        OrderTri.DESC -> Icons.Filled.KeyboardArrowDown
    }
}

private fun getSortedListPartie(
    joueurOrder: OrderTri, roiOrder: OrderTri, victoireOrder: OrderTri, listParties: List<PartieUI>
): List<PartieUI> {
    var sortedListParties: List<PartieUI> = listParties.subList(0, listParties.size)
    if (joueurOrder != OrderTri.NONE) {
        sortedListParties = sortedListParties.sortedBy { it.nomJoueur }
        if (joueurOrder == OrderTri.DESC) {
            sortedListParties = sortedListParties.reversed()
        }
    }
    if (roiOrder != OrderTri.NONE) {
        sortedListParties = sortedListParties.sortedBy { it.couleur }
        if (roiOrder == OrderTri.DESC) {
            sortedListParties = sortedListParties.reversed()
        }
    }
    if (victoireOrder != OrderTri.NONE) {
        sortedListParties = sortedListParties.sortedBy { it.gagne }
        if (victoireOrder == OrderTri.DESC) {
            sortedListParties = sortedListParties.reversed()
        }
    }
    return sortedListParties
}

enum class OrderTri {
    NONE, ASC, DESC
}