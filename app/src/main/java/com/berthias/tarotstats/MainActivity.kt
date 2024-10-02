package com.berthias.tarotstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.berthias.tarotstats.navigation.TarotNavHost
import com.berthias.tarotstats.ui.theme.TarotStatsTheme

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
    TarotNavHost(modifier = modifier, navController = navController)
}