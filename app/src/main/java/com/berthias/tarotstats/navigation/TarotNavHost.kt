package com.berthias.tarotstats.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.berthias.tarotstats.screen.AddJoueurDestination
import com.berthias.tarotstats.screen.AddJoueurScreen
import com.berthias.tarotstats.screen.ListeJoueursDestination
import com.berthias.tarotstats.screen.ListeJoueursScreen

@Composable
fun TarotNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ListeJoueursDestination.route,
        modifier = modifier
    ) {
        composable(route = ListeJoueursDestination.route) {
            ListeJoueursScreen(navigateToAddJoueur = { navController.navigate(AddJoueurDestination.route) })
        }
        composable(route = AddJoueurDestination.route) {
            AddJoueurScreen(onValidate = { navController.navigateUp() })
        }
    }
}