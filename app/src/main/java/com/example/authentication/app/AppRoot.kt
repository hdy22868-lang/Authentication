package com.example.authentication.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.authentication.auth.presentation.navigation.AuthNavGraph
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoot(viewModel: AppViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val start = startDestination ?: return

    val navController = rememberNavController()
    AuthNavGraph(
        navController = navController,
        startDestination = start
    )
}