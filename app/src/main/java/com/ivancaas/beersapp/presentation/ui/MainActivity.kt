package com.ivancaas.beersapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.ivancaas.beersapp.nav.NavigationComponent
import com.ivancaas.beersapp.presentation.ui.theme.BaseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            BaseTheme {
                Scaffold { paddingValues ->

                    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                        NavigationComponent(
                            navHostController = navController,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }
}





