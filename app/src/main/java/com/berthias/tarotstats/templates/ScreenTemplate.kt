package com.berthias.tarotstats.templates

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.berthias.tarotstats.TarotTopAppBar
import com.berthias.tarotstats.navigation.NavigationDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme

object PageDestination : NavigationDestination {
    override val route: String
        get() = "page"
    override val title: String
        get() = "Page"

}

@Composable
fun PageScreen(
    drawerState: DrawerState
) {
    Scaffold(topBar = {
        TarotTopAppBar(
            title = PageDestination.title, drawerState = drawerState
        )
    }) { innerpadding ->
        PageContent(modifier = Modifier.padding(innerpadding))
    }
}

@Composable
fun PageContent(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun PageContentPreview() {
    TarotStatsTheme {
        PageContent()
    }
}