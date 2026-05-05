package com.practicum.playlistmaker.ui.media.compose_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.compose_ui.TrackItem
import com.practicum.playlistmaker.ui.media.models.FavoriteState
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteUi(isDarkTheme: Boolean, viewModel: FavoriteFragmentViewModel, onNavigateToAudioPlayer: (Track) -> Unit){
    val tracksState by viewModel.observeState().observeAsState(initial = null)

    val textColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)

    viewModel.getTracks()

    when(val state = tracksState){
        is FavoriteState.Tracks -> StateTrackList(isDarkTheme = isDarkTheme, tracks = state.tracks, onNavigateToAudioPlayer = onNavigateToAudioPlayer)
        null -> { EmptyList(textColor) }
    }
}

@Composable
private fun EmptyList(textColor: Color){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment  = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(R.drawable.ic_nothing_found_120),
            contentDescription = stringResource(R.string.image_description),
            modifier = Modifier.padding(top = 106.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(R.string.text_view_no_data_media),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = textColor
        )
    }
}

@Composable
private fun StateTrackList(isDarkTheme: Boolean, tracks: List<Track>, onNavigateToAudioPlayer: (Track) -> Unit){
    LazyColumn (
        modifier = Modifier.fillMaxSize()
    )
    {
        items(tracks) { track ->
            TrackItem(
                isDarkTheme = isDarkTheme,
                track = track,
                onClick = {
                    onNavigateToAudioPlayer(track)
                })
        }
    }
}