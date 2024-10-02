package com.berthias.tarotstats.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.berthias.tarotstats.screen.AddJoueurDestination
import com.berthias.tarotstats.screen.AddJoueurScreen
import com.berthias.tarotstats.screen.AddPartieDestination
import com.berthias.tarotstats.screen.AddPartieScreen
import com.berthias.tarotstats.screen.ListeJoueursDestination
import com.berthias.tarotstats.screen.ListeJoueursScreen
import com.berthias.tarotstats.screen.ListePartiesDestination
import com.berthias.tarotstats.screen.ListePartiesScreen

@Composable
fun TarotNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = ListePartiesDestination.route,
        modifier = modifier
    ) {
        composable(route = ListeJoueursDestination.route) {
            ListeJoueursScreen(navigateToAddJoueur = { navController.navigate(AddJoueurDestination.route) })
        }
        composable(route = AddJoueurDestination.route) {
            AddJoueurScreen(onValidate = { navController.navigateUp() })
        }
        composable(route = AddPartieDestination.route) {
            AddPartieScreen(onValidate = { navController.navigateUp() })
        }
        composable(route = ListePartiesDestination.route) {
            ListePartiesScreen(navigateToAddPartie = { navController.navigate(AddPartieDestination.route) })
        }
    }
}