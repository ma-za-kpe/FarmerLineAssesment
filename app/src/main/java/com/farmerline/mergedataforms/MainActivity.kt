package com.farmerline.mergedataforms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.farmerline.mergedataforms.ui.screens.FormListScreen
import com.farmerline.mergedataforms.ui.screens.FormScreen
import com.farmerline.mergedataforms.ui.theme.MergeDataSurveysTheme
import com.farmerline.mergedataforms.utils.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MergeDataSurveysTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    networkMonitor.networkStatus.collect { isConnected ->
                        val message = if (isConnected) "Online" else "Offline"
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    // TODO 02: move this into a separate navigation class, ive already created it
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "formList") {
                        composable("formList") {
                            FormListScreen(
                                modifier = Modifier.padding(innerPadding),
                                onQuestionSelected = { formId ->
                                    navController.navigate("form/$formId")
                                }
                            )
                        }
                        composable("form/{formId}") { backStackEntry ->
                            val formId = backStackEntry.arguments?.getString("formId")
                            formId?.let {
                                FormScreen(formId = it, modifier = Modifier.padding(innerPadding))
                            }
                        }
                    }
                }
            }
        }
    }
}