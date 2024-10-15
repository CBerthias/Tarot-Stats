package com.berthias.tarotstats.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berthias.tarotstats.R
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.model.CouleurEnum
import java.util.EnumMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    modifier: Modifier = Modifier, valueList: List<String>, label: String, onReturn: (Int) -> Unit
) {
    var selectedValue by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                value = valueList[selectedValue],
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                })
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (index in valueList.indices) {
                    DropdownMenuItem(text = {
                        Text(
                            text = valueList[index], textAlign = TextAlign.End
                        )
                    }, onClick = {
                        selectedValue = index
                        onReturn(selectedValue)
                        expanded = false
                    })
                    if (index != valueList.size - 1) {
                        HorizontalDivider(
                            modifier.padding(
                                start = 8.dp, end = 8.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Winrates(modifier: Modifier = Modifier, parties: List<PartieUI>) {
    var gagnees = 0
    val nbPartiesRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    val nbWinRoi: EnumMap<CouleurEnum, Int> = EnumMap(CouleurEnum::class.java)
    for (partie in parties) {
        if (partie.gagne) {
            gagnees++
            nbWinRoi[partie.couleur] = nbWinRoi.getOrDefault(partie.couleur, 0) + 1
        }
        nbPartiesRoi[partie.couleur] = nbPartiesRoi.getOrDefault(partie.couleur, 0) + 1
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Winrate par roi appelé",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WinrateRoiCard(
                modifier = Modifier.size(100.dp),
                image = painterResource(R.drawable.trefle),
                CouleurEnum.TREFLE,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.TREFLE, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.TREFLE, 0)
            )
            WinrateRoiCard(
                modifier = Modifier.size(100.dp),
                image = painterResource(R.drawable.carreau),
                CouleurEnum.CARREAU,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.CARREAU, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.CARREAU, 0)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            WinrateRoiCard(
                modifier = Modifier.size(100.dp),
                image = painterResource(R.drawable.coeur),
                CouleurEnum.COEUR,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.COEUR, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.COEUR, 0)
            )
            WinrateRoiCard(
                modifier = Modifier.size(100.dp),
                image = painterResource(R.drawable.pique),
                CouleurEnum.PIQUE,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.PIQUE, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.PIQUE, 0)
            )
        }
    }
}

@Composable
fun WinrateRoiCard(
    modifier: Modifier = Modifier,
    image: Painter,
    couleur: CouleurEnum,
    nbWinRoi: Int,
    nbPartiesRoi: Int
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(35.dp),
                painter = image,
                contentDescription = couleur.stringValue
            )
            var winrate: Float = nbWinRoi.div(nbPartiesRoi.toFloat()) * 100
            if (winrate.isNaN()) winrate = 0F
            Text(text = "%.0f".format(winrate) + "%", fontSize = 30.sp)
            Text(text = "%s parties".format(nbPartiesRoi), fontSize = 15.sp)
        }
    }
}