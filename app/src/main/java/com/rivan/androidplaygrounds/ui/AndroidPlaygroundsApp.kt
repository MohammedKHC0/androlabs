package com.rivan.androidplaygrounds.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.rivan.androidplaygrounds.core.designsystem.component.*
import com.rivan.androidplaygrounds.core.designsystem.icon.APIcons
import com.rivan.androidplaygrounds.core.designsystem.icon.Icon.DrawableResourceIcon
import com.rivan.androidplaygrounds.core.designsystem.icon.Icon.ImageVectorIcon
import com.rivan.androidplaygrounds.navigation.APNavHost
import com.rivan.androidplaygrounds.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)

@Composable
fun AndroidPlaygroundsApp(
    windowSizeClass: WindowSizeClass,
    appState: AndroidPlaygroundsAppState = rememberAndroidPlaygroundsAppState(
        windowSizeClass = windowSizeClass
    )
) {
    APBackground {

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    APBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }
            }
        ) { padding ->

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumedWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                if (appState.shouldShowNavRail) {
                    APNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                Column(Modifier.fillMaxSize()) {
                    // Show the top app bar on top level destinations.
                    val destination = appState.currentTopLevelDestination
                    if (destination != null) {
                        APTopAppBar(
                            titleRes = destination.titleTextId,
                            actionIcon = APIcons.Account,
                            actionIconContentDescription = stringResource(
                                // TODO: Add proper string resource here.
                                id = android.R.string.untitled
                            ),
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent
                            ),
                            onActionClick = { /*TODO*/ }
                        )
                    }

                    APNavHost(
                        navController = appState.navController,
                        onBackClick = appState::onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun APBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    APNavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            APNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }

                    when (icon) {
                        is ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

@Composable
private fun APNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    APNavigationRail(modifier = modifier) {
        // Align the items to the center of the navigation rail.
        Spacer(modifier = Modifier.weight(1F))
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            APNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }

                    when (icon) {
                        is ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false