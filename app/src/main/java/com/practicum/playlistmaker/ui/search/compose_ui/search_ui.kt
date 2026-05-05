package com.practicum.playlistmaker.ui.search.compose_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.models.SearchState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.common.compose_ui.TrackItem
import com.practicum.playlistmaker.ui.utils.rememberKeyboardVisibility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUi(viewModel: SearchViewModel, viewModelTheme: ThemeViewModel, onNavigateToAudioPlayer: (Track) -> Unit){

    val text by viewModel.observeStateText().observeAsState(initial = "")
    val tracksState by viewModel.observeState().observeAsState(initial = null)
    val toggleKeyboard = rememberKeyboardVisibility()
    val isDarkTheme by viewModelTheme.observeIsDarkTheme().observeAsState(initial = false)

    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkTheme) colorResource(R.color.dark) else colorResource(R.color.white),
        animationSpec = tween(durationMillis = 200),
        label = "AllColor"
    )

    val iconColor = if (isDarkTheme) colorResource(R.color.black) else colorResource(R.color.gray)

    viewModelTheme.loadTheme()

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.search_title), style = TextStyle(
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
    ){ paddingValues ->

        Column(modifier = Modifier.fillMaxSize().padding(paddingValues))
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ){
                TextField(
                    value = text,
                    onValueChange = {
                        viewModel.searchTracksDebounce(it)
                        if (text.isEmpty()){
                            viewModel.loadHistory()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                    // Если делать по макету, то текст схлопываеться.
                    // .padding(horizontal = 16.dp, vertical = 8.dp)
                    // .height(36.dp)
                    ,
                    placeholder = { Text(text = stringResource(R.string.hint_edit_search),style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.ys_display_regular)) )) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.hint_edit_search),
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.image_description),
                                tint = iconColor,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable{
                                        viewModel.clearText()
                                        viewModel.loadHistory()
                                        toggleKeyboard(false)
                                    }
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.None,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    ),
                )
            }

            when (val state = tracksState){
                is SearchState.Loading -> {
                    StateProgress()
                }
                is SearchState.SearchResult -> {
                    StateTrackList(
                        isDarkTheme = isDarkTheme,
                        tracks = state.tracks,
                        onSaveHistory = {
                            viewModel.addHistory(it, 0)
                        },
                        onNavigateToAudioPlayer = onNavigateToAudioPlayer)
                }
                is SearchState.Error -> {
                    StateError(isDarkTheme = isDarkTheme, stringId = state.stringId, drawable = state.drawableId, onClickUpdate =  {
                         viewModel.searchTracksDebounce(text)
                     })
                }
                is SearchState.HistoryResult -> {
                    StateLayoutHistory(
                        isDarkTheme = isDarkTheme,
                        tracks = state.tracks,
                        onClickClearHistory = { viewModel.clearHistory() },
                        onNavigateToAudioPlayer = onNavigateToAudioPlayer
                    )
                }
                null -> {  }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun StateError(isDarkTheme: Boolean, stringId: Int, drawable: Int, onClickUpdate: () -> Unit){
    val message = stringResource(stringId)
    val isBtnUpdate = (stringId == R.string.no_internet)
    val textTrackNameColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
    val btnContainerColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
    val btnContentColor = if (isDarkTheme) colorResource(R.color.black) else colorResource(R.color.white)

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 102.dp),
        horizontalAlignment  = Alignment.CenterHorizontally
    )
    {

        Image(
            painter = painterResource(drawable),
            contentDescription = stringResource(R.string.image_description),
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp).padding(horizontal = 24.dp),
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = textTrackNameColor
        )

        if(isBtnUpdate){
            Button(
                onClick = { onClickUpdate() },
                modifier = Modifier.padding(top = 24.dp),
                shape = RoundedCornerShape(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnContainerColor,
                    contentColor = btnContentColor,
                )
            )
            {
                Text(
                    text = stringResource(R.string.btn_update),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                )
            }
        }

    }
}

@Composable
private fun StateProgress(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp,)
    )
    {
        CircularProgressIndicator(
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.Center),
            color = colorResource(R.color.blue)
        )
    }
}

@Composable
private fun StateTrackList(isDarkTheme: Boolean, tracks: List<Track>, onSaveHistory: (Track) -> Unit, onNavigateToAudioPlayer: (Track) -> Unit){
    LazyColumn (
        modifier = Modifier.fillMaxSize().padding(top = 16.dp)
    )
    {
        items(tracks) { track ->
            TrackItem(
                isDarkTheme = isDarkTheme,
                track = track,
                onClick = {
                    onSaveHistory(track)
                    onNavigateToAudioPlayer(track)
                })
        }
    }
}

@Composable
private fun StateLayoutHistory(isDarkTheme: Boolean, tracks: List<Track>, onClickClearHistory: () -> Unit, onNavigateToAudioPlayer: (Track) -> Unit){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text  = stringResource(R.string.text_view_your_search),
            modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontSize = 19.sp,
                color = colorResource(R.color.dark)
            )
        )
    }

    HistoryList(isDarkTheme,tracks, onClickClearHistory, onNavigateToAudioPlayer)
}

@Composable
private fun HistoryList(isDarkTheme: Boolean, historyItems: List<Track>, onClickClearHistory: () -> Unit, onNavigateToAudioPlayer: (Track) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            items(historyItems) { track ->
                TrackItem(
                    isDarkTheme = isDarkTheme,
                    track = track,
                    onClick = { onNavigateToAudioPlayer(track) })
            }
        }

        if(historyItems.isNotEmpty()){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Button(
                    onClick = onClickClearHistory,
                    shape = RoundedCornerShape(54.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.dark),
                        contentColor = colorResource(R.color.white)
                    )
                )
                {
                    Text(
                        text = stringResource(R.string.btn_clear_search_history),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}