package com.berthias.tarotstats.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.berthias.tarotstats.screen.AddJoueurDestination
import com.berthias.tarotstats.screen.AddJoueurScreen
import com.berthias.tarotstats.screen.AddPartieDestination
import com.berthias.tarotstats.screen.AddPartieScreen
import com.berthias.tarotstats.screen.HomeDestination
import com.berthias.tarotstats.screen.HomeScreen
import com.berthias.tarotstats.screen.InfosJoueurDestination
import com.berthias.tarotstats.screen.InfosJoueurScreen
import com.berthias.tarotstats.screen.LeaderboardDestination
import com.berthias.tarotstats.screen.LeaderboardScreen
import com.berthias.tarotstats.screen.ListeJoueursDestination
import com.berthias.tarotstats.screen.ListeJoueursScreen
import com.berthias.tarotstats.screen.ListePartiesDestination
import com.berthias.tarotstats.screen.ListePartiesScreen

@Composable
fun TarotNavHost(
    navController: NavHostController, drawerState: DrawerState, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(drawerState = drawerState)
        }
        composable(route = ListeJoueursDestination.route) {
            ListeJoueursScreen(drawerState = drawerState,
                navigateToAddJoueur = { navController.navigate(AddJoueurDestination.route) },
                navigateToInfosJoueur = { joueurUI ->
                    navController.navigate(InfosJoueurDestination.getRoute(joueurUI.nom))
                },
                navigateToLeaderboard = { navController.navigate(LeaderboardDestination.route) })
        }
        composable(route = AddJoueurDestination.route) {
            AddJoueurScreen(drawerState = drawerState, onValidate = { navController.navigateUp() })
        }
        composable(route = AddPartieDestination.route) {
            AddPartieScreen(drawerState = drawerState, onValidate = { navController.navigateUp() })
        }
        composable(route = ListePartiesDestination.route) {
            ListePartiesScreen(drawerState = drawerState,
                navigateToAddPartie = { navController.navigate(AddPartieDestination.route) })
        }
        composable(route = InfosJoueurDestination.route + "{joueur}") {
            InfosJoueurScreen(
                drawerState = drawerState, joueurUINom = it.arguments?.getString("joueur") ?: ""
            )
        }
        composable(route = LeaderboardDestination.route) {
            LeaderboardScreen(drawerState = drawerState)
        }
    }
}