package com.practicum.playlistmaker.ui.media.compose_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    val isDarkTheme by themeViewModel.observeIsDarkTheme().observeAsState(initial = false)

    val indicatorColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
    val textColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)

    themeViewModel.loadTheme()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.media_title), style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_medium))
                    ))
                },
                modifier = Modifier.height(56.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.dark),
                    containerColor = Color.Transparent
                )
            )
        }
    )
    { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = textColor,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        height = 2.dp,
                        color = indicatorColor
                    )
                }
            )
            {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title, style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.ys_display_medium))
                        )) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            )
            {page ->
                when (page) {
                    0 -> FavoriteUi(
                        isDarkTheme = isDarkTheme,
                        viewModel = favoriteViewModel,
                        onNavigateToAudioPlayer = onNavigateToAudioPlayer
                    )
                    1 -> PlaylistsUi(
                        isDarkTheme = isDarkTheme,
                        viewModel = playlistsViewModel,
                        onNavigateToPlaylist = onNavigateToPlaylist,
                        onNavigateToCreatePlaylist = onNavigateToCreatePlaylist
                    )

                }
            }
        }
    }
}