package com.example.weather.presentation.ui.day

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.presentation.ui.custom.BackgroundImage
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SevenDaysScreen(
    viewModel: SevenDaysViewModel = viewModel(),
    lat: Double = 21.0245,
    long: Double = 105.8412,
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    SevenDaysScreenContent(
        modifier = Modifier.statusBarsPadding(),
        onBackPressed = onBackPressed,
        scaffoldState = scaffoldState
    )
}

@Composable
fun SevenDaysScreenContent(
    modifier: Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBackPressed: (() -> Unit)? = null,
) {
    val drawableId = if (isSystemInDarkTheme()) R.drawable.background_night else R.drawable.background

    Surface(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableId),
            alignment = Alignment.TopCenter
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    SevenDaysTopAppBar { onBackPressed?.invoke() }
                },
                modifier = modifier,
                backgroundColor = Color.Transparent
            ) {
                val contentModifier = Modifier
                    .fillMaxSize()
                    .padding(all = 18.dp)
            }
        }
    }
}

@Composable
private fun SevenDaysTopAppBar(
    elevation: Dp = 0.dp,
    onBackPressed: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 4.dp, top = 12.dp),
                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onPrimary),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed?.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.close),
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        actions = {
            IconButton(onClick = { /** Not implement */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_city),
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = elevation
    )
}
