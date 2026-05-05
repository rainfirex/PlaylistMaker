package com.practicum.playlistmaker.ui.config.compose_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigUi(viewModel: ConfigViewModel){
    val isDarkTheme by viewModel.observeIsDarkTheme().observeAsState(initial = false)

    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkTheme) colorResource(R.color.dark) else colorResource(R.color.white),
        animationSpec = tween(durationMillis = 200),
        label = "AllColor"
    )

    val textColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.black)
    val iconColor = if (isDarkTheme) colorResource(R.color.white) else colorResource(R.color.gray)

    viewModel.loadTheme()

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.setting_title), style = TextStyle(
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
        // Содержимое экрана
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding))
        {
            // Переключатель темы
            SettingSwitchItem(
                text = stringResource(R.string.setting_mnu_theme),
                checked = isDarkTheme,
                onCheckedChange = { viewModel.setTheme(it) },
                isDarkTheme = isDarkTheme,
                textColor = textColor,
            )

            // Пункт меню "Поделиться"
            val shareUrl = stringResource(R.string.url_android_dev)
            SettingMenuItem(
                text = stringResource(R.string.setting_mnu_shared),
                icon = painterResource(R.drawable.ic_share_24),
                onClick = { viewModel.shareApp(shareUrl) },
                textColor = textColor,
                iconColor = iconColor
            )

            // Пункт меню "Поддержка"
            val subject = stringResource(R.string.support_subject)
            val message = stringResource(R.string.support_message)
            SettingMenuItem(
                text = stringResource(R.string.setting_mnu_support),
                icon = painterResource(R.drawable.ic_support_24),
                onClick = { viewModel.openSupport(subject, message) },
                textColor = textColor,
                iconColor = iconColor
            )

            // Пункт меню "Соглашение"
            val agree = stringResource(R.string.url_offer)
            SettingMenuItem(
                text = stringResource(R.string.setting_mnu_agree),
                icon = painterResource(R.drawable.ic_arrow_forward_24),
                onClick = { viewModel.openTerms(agree) },
                textColor = textColor,
                iconColor = iconColor
            )
        }
    }
}

@Composable
private fun SettingSwitchItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, isDarkTheme: Boolean = false, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = textColor
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(40.dp).scale(0.6f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = if (isDarkTheme) colorResource(R.color.blue) else colorResource(R.color.gray),              // thumbTint (checked)
                checkedTrackColor = if (isDarkTheme) colorResource(R.color.blue_light) else colorResource(R.color.gray_light),  // trackTint (checked)
                uncheckedThumbColor = if (isDarkTheme) colorResource(R.color.blue) else colorResource(R.color.gray),            // thumbTint (unchecked)
                uncheckedTrackColor = if (isDarkTheme) colorResource(R.color.blue_light) else colorResource(R.color.gray_light), // trackTint (unchecked)
                uncheckedBorderColor = Color.Transparent,
                disabledUncheckedBorderColor = Color.Transparent,
                disabledCheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
private fun SettingMenuItem(text: String, icon: Painter, onClick: () -> Unit, textColor: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = textColor
        )
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp),
        )
    }
}