package com.berthias.tarotstats.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    modifier: Modifier = Modifier,
    valueList: List<String>,
    label: String,
    onReturn: (Int) -> Unit
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