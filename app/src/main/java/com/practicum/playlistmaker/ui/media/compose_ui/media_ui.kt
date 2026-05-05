package com.practicum.playlistmaker.ui.media.compose_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import kotlinx.coroutines.launch

@Composable
fun MediaUi(
    favoriteViewModel: FavoriteFragmentViewModel,
    themeViewModel: ThemeViewModel,
    playlistsViewModel: PlaylistsFragmentViewModel,
    onNavigateToAudioPlayer: (Track) -> Unit,
    onNavigateToPlaylist: (Playlist) -> Unit,
    onNavigateToCreatePlaylist: () -> Unit) {

    val tabs = listOf(stringResource(R.string.tab_favorite),stringResource(R.string.tab_playlist))
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Blue,
            contentColor = Color.Red,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
                    color = Color.Yellow  // 👈 меняем цвет индикатора
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().padding(top = 50.dp)
    )
    {page ->
        when (page) {
            0 -> FavoriteUi(
                viewModel = favoriteViewModel,
                viewModelTheme = themeViewModel,
                onNavigateToAudioPlayer = onNavigateToAudioPlayer
            )
            1 -> PlaylistsUi(
                viewModel = playlistsViewModel,
                viewModelTheme = themeViewModel,
                onNavigateToPlaylist = onNavigateToPlaylist,
                onNavigateToCreatePlaylist = onNavigateToCreatePlaylist
            )

        }
    }
}