package com.manu.manu_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.manu.manu_app.ui.navigation.NavGraph
import com.manu.manu_app.ui.theme.ManuAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManuAppTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}