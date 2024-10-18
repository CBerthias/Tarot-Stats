package com.berthias.tarotstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.berthias.tarotstats.navigation.TarotNavHost
import com.berthias.tarotstats.screen.HomeDestination
import com.berthias.tarotstats.screen.ListeJoueursDestination
import com.berthias.tarotstats.screen.ListePartiesDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import com.berthias.tarotstats.util.ResizableText
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarotStatsTheme {
                TarotApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarotTopAppBar(modifier: Modifier = Modifier, title: String, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(modifier = modifier, title = { Text(title) }, navigationIcon = {
        IconButton(onClick = {
            coroutineScope.launch {
                drawerState.open()
            }
        }) {
            Icon(Icons.Filled.Menu, "toggle navigation drawer")
        }
    })
}

@Composable
fun TarotApp(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.7F)) {
            NavigationDrawerItem(modifier = Modifier.padding(8.dp), label = {
                ResizableText(
                    text = "Stats", style = TextStyle(fontSize = 30.sp)
                )
            }, onClick = {
                navController.navigate(HomeDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
            NavigationDrawerItem(modifier = Modifier.padding(8.dp), label = {
                ResizableText(
                    text = "Joueurs", style = TextStyle(fontSize = 30.sp)
                )
            }, onClick = {
                navController.navigate(ListeJoueursDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
            NavigationDrawerItem(modifier = Modifier.padding(8.dp), label = {
                ResizableText(
                    text = "Parties", style = TextStyle(fontSize = 30.sp)
                )
            }, onClick = {
                navController.navigate(ListePartiesDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
        }
    }) {
        TarotNavHost(modifier = modifier, navController = navController, drawerState = drawerState)
    }
}