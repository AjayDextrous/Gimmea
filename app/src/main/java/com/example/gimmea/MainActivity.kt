package com.example.gimmea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.gimmea.ui.screens.CategorySuggestionsScreen
import com.example.gimmea.ui.theme.GimmeaTheme
import com.example.gimmea.ui.theme.modernGray
import com.example.gimmea.ui.theme.modernWhite
import com.example.gimmea.viewmodels.DebugViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    @Serializable
    data object CategorySuggestions
    @Serializable
    data object Settings

    private val debugViewModel: DebugViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GimmeaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
//                    bottomBar = {
//                        BottomAppBar(
//                            containerColor = modernGray,
//                            contentColor = modernWhite,
//                            actions = {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.Center
//                                ){
//                                    IconButton(onClick = {}) {
//                                        Icon(Icons.Filled.Settings, contentDescription = getString(R.string.label_settings))
//                                    }
//                                    IconButton(onClick = {}) {
//                                        Icon(Icons.Outlined.Star, contentDescription = getString(R.string.label_suggestions))
//                                    }
//                                }
//
//                            }
//                        )
//                    }
                ) { innerPadding ->
                    val navController = rememberNavController()
                    val navGraph: NavGraph = remember(navController) {
                        navController.createGraph(startDestination = CategorySuggestions){
                            composable<CategorySuggestions> {
                                CategorySuggestionsScreen(debugViewModel, innerPadding)
                            }
                        }
                    }
                    NavHost(navController, navGraph)
                }
            }
        }
    }
}


