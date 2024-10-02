package com.berthias.tarotstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.berthias.tarotstats.navigation.TarotNavHost
import com.berthias.tarotstats.screen.HomeDestination
import com.berthias.tarotstats.screen.ListeJoueursDestination
import com.berthias.tarotstats.screen.ListePartiesDestination
import com.berthias.tarotstats.ui.theme.TarotStatsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarotStatsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
                    TarotApp(
                        modifier = Modifier
                            .padding(innerpadding)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TarotApp(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            NavigationDrawerItem(label = { Text(text = "Accueil") }, onClick = {
                navController.navigate(HomeDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
            NavigationDrawerItem(label = { Text(text = "Joueurs") }, onClick = {
                navController.navigate(ListeJoueursDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
            NavigationDrawerItem(label = { Text(text = "Parties") }, onClick = {
                navController.navigate(ListePartiesDestination.route)
                coroutineScope.launch {
                    drawerState.close()
                }
            }, selected = false
            )
        }
    }) {
        TarotNavHost(modifier = modifier, navController = navController)
    }
}