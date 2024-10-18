package com.berthias.tarotstats.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.berthias.tarotstats.R
import com.berthias.tarotstats.data.viewmodel.PartieUI
import com.berthias.tarotstats.model.CouleurEnum
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import java.util.EnumMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    modifier: Modifier = Modifier, valueList: List<String>, label: String, onReturn: (Int) -> Unit
) {
    var selectedValue by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true),
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
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp),
                image = painterResource(R.drawable.trefle),
                CouleurEnum.TREFLE,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.TREFLE, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.TREFLE, 0)
            )
            WinrateRoiCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp),
                image = painterResource(R.drawable.carreau),
                CouleurEnum.CARREAU,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.CARREAU, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.CARREAU, 0)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            WinrateRoiCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp),
                image = painterResource(R.drawable.coeur),
                CouleurEnum.COEUR,
                nbWinRoi = nbWinRoi.getOrDefault(CouleurEnum.COEUR, 0),
                nbPartiesRoi = nbPartiesRoi.getOrDefault(CouleurEnum.COEUR, 0)
            )
            WinrateRoiCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp),
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
    Card(modifier = modifier.aspectRatio(1f)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .weight(3f)
                    .aspectRatio(1f),
                painter = image,
                contentDescription = couleur.stringValue
            )
            val winrate: Float = nbWinRoi.div(nbPartiesRoi.toFloat()) * 100
            val winrateString = if (winrate.isNaN()) "-%" else "%.0f".format(winrate) + "%"
            ResizableText(
                modifier = Modifier.weight(3.5f),
                text = winrateString,
                style = TextStyle(fontSize = 70.sp)
            )
            ResizableText(
                modifier = Modifier.weight(1.5f),
                text = "%s parties".format(nbPartiesRoi),
                style = TextStyle(fontSize = 25.sp)
            )
        }
    }
}

@Composable
fun ResizableText(modifier: Modifier = Modifier, text: String, style: TextStyle) {
    var resizedTextStyle by remember { mutableStateOf(style) }
    val defaultFontSize = 50.sp
    var shouldDraw by remember { mutableStateOf(false) }

    Text(modifier = modifier.drawWithContent {
        if (shouldDraw) {
            drawContent()
        }
    }, text = text, style = resizedTextStyle, softWrap = false, onTextLayout = { result ->
        if (result.didOverflowWidth || result.didOverflowHeight) {
            if (resizedTextStyle.fontSize.isUnspecified) {
                resizedTextStyle = resizedTextStyle.copy(fontSize = defaultFontSize)
            }
            resizedTextStyle = resizedTextStyle.copy(fontSize = resizedTextStyle.fontSize * 0.95f)
        } else {
            shouldDraw = true
        }
    })
}

@Preview(showBackground = true)
@Composable
fun WinratesPreview() {
    TarotStatsTheme {
        Winrates(
            parties = listOf(
                PartieUI(1L, "Tanguy", CouleurEnum.CARREAU, true, "Thibault"),
                PartieUI(2L, "Tanguy", CouleurEnum.PIQUE, true, "Thibault"),
                PartieUI(3L, "Tanguy", CouleurEnum.PIQUE, false, "Thibault")
            )
        )
    }
}