package com.example.authentication.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.authentication.auth.presentation.navigation.AuthNavGraph
import com.example.authentication.auth.presentation.navigation.AuthRoute
import com.example.authentication.core.data.networking.SessionManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AppRoot(viewModel: AppViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val start = startDestination ?: return

    val navController = rememberNavController()
    val session: SessionManager = koinInject()
    LaunchedEffect(Unit) {
        session.expired.collect {
            navController.navigate(AuthRoute.Welcome) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
    AuthNavGraph(
        navController = navController,
        startDestination = start
    )
}