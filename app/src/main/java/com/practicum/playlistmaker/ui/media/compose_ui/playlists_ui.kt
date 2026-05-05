package com.practicum.playlistmaker.ui.media.compose_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.common.models.PlaylistsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsUi(viewModel: PlaylistsFragmentViewModel, viewModelTheme: ThemeViewModel, onNavigateToPlaylist: (Playlist) -> Unit, onNavigateToCreatePlaylist: () -> Unit){
    val isDarkTheme by viewModelTheme.observeIsDarkTheme().observeAsState(initial = false)
    val plsState by viewModel.observeState().observeAsState(initial = null)


    viewModelTheme.loadTheme()
    viewModel.getPlaylists()

    Column()
    {
        BtnCreatePlaylist(isDarkTheme, onNavigateToCreatePlaylist = onNavigateToCreatePlaylist)

        when(val state = plsState){
            is PlaylistsState.Playlists -> ViewState(
                isDarkTheme = isDarkTheme,
                playlists = state.playLists,
                onNavigateToPlaylist = onNavigateToPlaylist
            )
            null -> {
                NoPlaylists(isDarkTheme)
            }
        }
    }
}

@Composable
private fun BtnCreatePlaylist(isDarkTheme: Boolean, onNavigateToCreatePlaylist: () -> Unit){
    val btnContainerColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
    val btnContentColor = if (isDarkTheme) colorResource(R.color.black) else colorResource(R.color.white)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment  = Alignment.CenterHorizontally
    )
    {
        Button(
            onClick = { onNavigateToCreatePlaylist() },
            shape = RoundedCornerShape(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = btnContainerColor,
                contentColor = btnContentColor,
            )
        )
        {
            Text(
                text = stringResource(R.string.btn_new_playlist),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_medium))
            )
        }

    }
}

@Composable
private fun NoPlaylists(isDarkTheme: Boolean){
    val textColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
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
            text = stringResource(R.string.text_view_no_data_playlist),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = textColor
        )
    }

}

@Composable
private fun ViewState(isDarkTheme: Boolean, playlists: List<Playlist>, onNavigateToPlaylist: (Playlist) -> Unit){

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        items(playlists){  playlist ->
            PlaylistItem(
                isDarkTheme = isDarkTheme,
                playlist = playlist,
                onClick = {
                    onNavigateToPlaylist(playlist)
                }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PlaylistItem(isDarkTheme: Boolean, playlist: Playlist, onClick: () -> Unit){
    val textColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)

    Column()
    {
        Column (modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .size(160.dp)
        )
        {
            GlideImage(
                model = playlist.pathImage,
                contentDescription = stringResource(R.string.image_description),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.ic_track_placeholder_45),
                failure = placeholder(R.drawable.ic_track_placeholder_45)
            )
        }

        Text(
            text =  playlist.namePlaylist,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor
        )

        val resources = LocalContext.current.resources
        val trackCountText = resources.getQuantityString(
            R.plurals.view_track_with_d,
            playlist.count,
            playlist.count
        )

        Text(
            text =  trackCountText,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = textColor
        )
    }

}