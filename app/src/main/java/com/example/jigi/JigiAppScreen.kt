package com.example.jigi

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsPage
import com.example.jigi.ui.searchPage.SearchPage
import com.example.jigi.ui.searchPage.SearchPageViewModel
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsViewModel
import com.example.jigi.ui.searchPage.SearchOption
import com.example.jigi.ui.theme.onPrimaryContainerDark
import com.example.jigi.ui.theme.onPrimaryContainerLight
import com.example.jigi.ui.theme.onSecondaryContainerDark
import com.example.jigi.ui.theme.onSecondaryContainerLight
import com.example.jigi.ui.theme.primaryContainerDark
import com.example.jigi.ui.theme.primaryContainerLight
import com.example.jigi.ui.theme.secondaryContainerDark
import com.example.jigi.ui.theme.secondaryContainerLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JigiAppBar(
    currentScreen: SearchScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = primaryContainerDark,
            titleContentColor = onPrimaryContainerDark
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = onPrimaryContainerDark,
                    )
                }
            }
        }
    )

}

enum class SearchScreen(@StringRes val title: Int) {
    Search(title = R.string.search_page),
    About(title = R.string.about_page),
    DictionaryResults(title = R.string.dictionary_results_page),
    History(title = R.string.history_page),
    Settings(title = R.string.settings_page),
}

@Composable
fun JigiApp(
    searchPageViewModel: SearchPageViewModel = viewModel(),
    dictionaryResultsViewModel: DictionaryResultsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SearchScreen.valueOf(backStackEntry?.destination?.route ?: SearchScreen.Search.name)

    Scaffold(
        topBar = {
            JigiAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SearchScreen.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SearchScreen.Search.name) {
                SearchPage(
                    searchPageViewModel = searchPageViewModel,
                    navigateToSearch = {
                        navController.navigate(SearchScreen.DictionaryResults.name)
                    },
                )
            }
            composable(route = SearchScreen.DictionaryResults.name) {
                DictionaryResultsPage(
                    searchPageViewModel = searchPageViewModel,
                    dictionaryResultsViewModel = dictionaryResultsViewModel
                )
            }
        }
    }
}