package com.example.weather.presentation.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.weather.R
import com.example.weather.presentation.base.ExceptionHandleView
import com.example.weather.presentation.model.CurrentWeatherViewDataModel
import com.example.weather.presentation.model.HourlyWeatherViewDataModel
import com.example.weather.presentation.model.factory.createCurrentWeather
import com.example.weather.presentation.model.factory.createHourlyWeathers
import com.example.weather.presentation.ui.custom.BackgroundImage
import com.example.weather.presentation.ui.theme.WeatherTheme
import com.example.weather.presentation.ui.theme.White60
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navigateToNextSevenDay: (Float, Float) -> Unit
) {
    val viewState by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val requestFocus = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    HomeScreenContent(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = scaffoldState,
        homeViewState = viewState,
        closeSearchView = {
            viewModel.enableSearchView(false)
        },
        onSearchChange = {
            viewModel.onSearchInputChanged(it)
        },
        openSearchView = {
            viewModel.enableSearchView(true)
        },
        focusRequest = requestFocus,
        keyboardController = keyboardController,
        actionSearch = {
            viewModel.getWeather(viewState.searchInput)
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    homeViewState: HomeViewState = HomeViewState(),
    onSearchChange: ((String) -> Unit)? = null,
    closeSearchView: (() -> Unit)? = null,
    openSearchView: (() -> Unit)? = null,
    focusRequest: FocusRequester = remember { FocusRequester() },
    keyboardController: SoftwareKeyboardController? = null,
    actionSearch: (() -> Unit)? = null
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
                    HomeTopAppBar(
                        searchQuery = homeViewState.searchInput,
                        onSearchChange = onSearchChange,
                        showSearchView = homeViewState.searchEnabled,
                        closeSearchView = closeSearchView,
                        openSearchView = openSearchView,
                        focusRequest = focusRequest,
                        keyboardController = keyboardController,
                        actionSearch = actionSearch
                    )
                },
                modifier = modifier,
                backgroundColor = Color.Transparent
            ) {
                val contentModifier = Modifier
                    .fillMaxSize()
                    .padding(all = 18.dp)

                ExceptionHandleView(
                    state = homeViewState,
                    snackBarHostState = scaffoldState.snackbarHostState
                ) {
                    if (homeViewState.currentWeather != null) {
                        CurrentWeatherContent(modifier = contentModifier, homeViewState.currentWeather, homeViewState.hourlyWeathers)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherContent(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeatherViewDataModel,
    hourlyWeathers: List<HourlyWeatherViewDataModel> = emptyList()
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(78.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentWeather.currentTime,
                    modifier = Modifier,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 12.sp
                    )
                )

                Column(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.BottomStart)
                ) {
                    Text(
                        text = currentWeather.city,
                        style = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 14.sp
                        )
                    )

                    Text(
                        text = currentWeather.country,
                        style = MaterialTheme.typography.h5.copy(
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 24.sp
                        )
                    )
                }
            }

            Row {
                Text(
                    text = currentWeather.currentTemp,
                    modifier = Modifier.fillMaxHeight().padding(top = 2.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4.copy(
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 62.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = stringResource(R.string.zero),
                    style = MaterialTheme.typography.h5.copy(
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 25.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CurrentWeatherInfo(
            modifier = Modifier.height(200.dp)
                .fillMaxWidth(),
            currentWeather = currentWeather
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(124.dp)
        ) {
            LazyRow {
                items(hourlyWeathers) { hourly ->
                    HourlyWeatherItem(hourly = hourly)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CurrentWeatherInfo(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeatherViewDataModel
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bg_current),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.2f
        )

        Image(
            painter = rememberImagePainter(
                data = currentWeather.currentIcon,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_cloud)
                    error(R.drawable.ic_cloud)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .size(84.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.TopStart)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(R.string.humidity),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = currentWeather.humidity,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxHeight(fraction = 0.2f)
                .width(1.dp)
                .align(Alignment.TopCenter)
                .background(White60)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.TopEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.wind),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = currentWeather.wind,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxHeight(fraction = 0.2f)
                .width(1.dp)
                .align(Alignment.BottomCenter)
                .background(White60)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.visibility),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = currentWeather.visibility,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(start = 32.dp)
                .fillMaxWidth(fraction = 0.28f)
                .height(1.dp)
                .align(Alignment.CenterStart)
                .background(White60)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.real_feel),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = currentWeather.realFeel,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(end = 32.dp)
                .fillMaxWidth(fraction = 0.28f)
                .height(1.dp)
                .align(Alignment.CenterEnd)
                .background(White60)
        )
    }
}

/**
 * TopAppBar for the Home screen
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun HomeTopAppBar(
    elevation: Dp = 0.dp,
    showSearchView: Boolean = false,
    searchQuery: String = "",
    onSearchChange: ((String) -> Unit)? = null,
    closeSearchView: (() -> Unit)? = null,
    openSearchView: (() -> Unit)? = null,
    actionSearch: (() -> Unit)? = null,
    focusRequest: FocusRequester = remember { FocusRequester() },
    keyboardController: SoftwareKeyboardController? = null
) {
    if (showSearchView) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { closeSearchView?.invoke() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colors.primary
                    )
                }

                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequest)
                        .weight(1f)
                        .background(color = Color.Transparent),
                    value = searchQuery,
                    shape = RoundedCornerShape(size = 0.dp),
                    onValueChange = { value ->
                        onSearchChange?.invoke(value)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_city)
                        )
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = { onSearchChange?.invoke("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(R.string.remove),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    } else null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            actionSearch?.invoke()
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        LaunchedEffect(keyboardController) {
            focusRequest.requestFocus()
        }
    } else {
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
                IconButton(onClick = { closeSearchView?.invoke() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu_drawer),
                        contentDescription = stringResource(R.string.menu),
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            actions = {
                IconButton(onClick = { openSearchView?.invoke() }) {
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun HomeTopAppBarPreview() {
    WeatherTheme {
        Surface {
            HomeTopAppBar()
        }
    }
}

@Preview
@Composable
fun CurrentWeatherInfoPreview() {
    WeatherTheme {
        Surface {
            CurrentWeatherInfo(
                modifier = Modifier.height(200.dp),
                currentWeather = createCurrentWeather()
            )
        }
    }
}

@Preview("Default colors")
@Composable
fun HomeScreenContentPreview() {
    WeatherTheme {
        Surface {
            HomeScreenContent(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                homeViewState = HomeViewState(
                    currentWeather = createCurrentWeather(),
                    hourlyWeathers = createHourlyWeathers()
                )
            )
        }
    }
}

@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenContentDarkPreview() {
    WeatherTheme {
        Surface {
            HomeScreenContent(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                homeViewState = HomeViewState(
                    currentWeather = createCurrentWeather(),
                    hourlyWeathers = createHourlyWeathers()
                )
            )
        }
    }
}
