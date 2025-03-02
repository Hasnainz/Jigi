package com.example.jigi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jigi.viewprovider.AppViewModelProvider
import com.example.jigi.R
import com.example.jigi.ui.aboutPage.AboutPage
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsPage
import com.example.jigi.ui.searchPage.SearchPage
import com.example.jigi.ui.searchPage.SearchPageViewModel
import com.example.jigi.ui.dictionaryResultsPage.DictionaryResultsViewModel
import com.example.jigi.ui.historyPage.HistoryPage
import com.example.jigi.ui.settingsPage.SettingsPage
import com.example.jigi.ui.settingsPage.SettingsPageUiState
import com.example.jigi.ui.settingsPage.SettingsViewModel


enum class SearchScreen(@StringRes val title: Int) {
    Search(title = R.string.search_page),
    About(title = R.string.about_page),
    DictionaryResults(title = R.string.dictionary_results_page),
    History(title = R.string.history_page),
    Settings(title = R.string.settings_page),
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun JigiApp(
    searchPageViewModel: SearchPageViewModel = viewModel(),
    dictionaryResultsViewModel: DictionaryResultsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        SearchScreen.valueOf(backStackEntry?.destination?.route ?: SearchScreen.Search.name)

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
                    navigateToSettings = {
                        navController.navigate(SearchScreen.Settings.name)
                    },
                    navigateToAbout = {
                        navController.navigate(SearchScreen.About.name)
                    },
                    navigateToHistory = {
                        navController.navigate(SearchScreen.History.name)
                    }
                )
            }
            composable(route = SearchScreen.DictionaryResults.name) {
                DictionaryResultsPage(
                    searchPageViewModel = searchPageViewModel,
                    settingsViewModel = settingsViewModel,
                    dictionaryResultsViewModel = dictionaryResultsViewModel
                )
            }
            composable(route = SearchScreen.Settings.name) {
                settingsViewModel.updateExistingDictionaries()
                SettingsPage(
                    settingsViewModel = settingsViewModel
                )
            }

            composable(route = SearchScreen.About.name) {
                AboutPage()
            }

            composable(route = SearchScreen.History.name) {
                HistoryPage()
            }
        }
    }
}