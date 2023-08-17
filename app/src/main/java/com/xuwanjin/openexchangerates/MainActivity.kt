package com.xuwanjin.openexchangerates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.xuwanjin.currencyconvert.NavGraphs
import com.xuwanjin.currencyconvert.destinations.CurrencyConvertScreenDestination
import com.xuwanjin.openexchangerates.ui.theme.OpenExchangeRatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            OpenExchangeRatesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val engine = rememberNavHostEngine()
                    val navController = engine.rememberNavController()

                    val startRoute = CurrencyConvertScreenDestination
                    /**
                     *  use the compose-destinations for navigation.
                     *  here is the repo https://github.com/raamcosta/compose-destinations
                     */
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController,
                        startRoute = startRoute,
                    )
                }
            }
        }
    }
}
