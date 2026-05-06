package com.practicum.playlistmaker.ui.common.compose_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackItem(isDarkTheme: Boolean, track: Track, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        val textColorTrackName = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
        val textColorTrackInfo = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.gray)

        GlideImage(
            model = track.artworkUrl100,
            contentDescription = stringResource(R.string.image_description),
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_track_placeholder_45),
            failure = placeholder(R.drawable.ic_track_placeholder_45)
        )

        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(
                text = track.trackName,
                color = textColorTrackName,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily =  FontFamily(Font(R.font.ys_display_regular))
            )
            Row(){
                Text(
                    text = track.artistName,
                    color = textColorTrackInfo,
                    fontSize = 11.sp,
                    fontFamily =  FontFamily(Font(R.font.ys_display_regular))
                )
                Image(
                    painter = painterResource(R.drawable.ic_circle_13),
                    contentDescription = stringResource(R.string.image_description),
                    colorFilter = ColorFilter.tint(textColorTrackInfo),
                    modifier = Modifier
                        .size(13.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = track.timeTrack(),
                    color = textColorTrackInfo,
                    fontSize = 11.sp,
                    fontFamily =  FontFamily(Font(R.font.ys_display_regular))
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_arrow_forward_24),
            contentDescription = stringResource(R.string.image_description),
            colorFilter = ColorFilter.tint(textColorTrackInfo),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }

}