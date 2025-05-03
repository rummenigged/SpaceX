package com.octopus.edu.kotlin.feature.launches

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.octopus.edu.kotlin.core.design.designSystem.components.FullScreenCircularProgressIndicator
import com.octopus.edu.kotlin.core.design.designSystem.components.SpaceXTopBar
import com.octopus.edu.kotlin.core.design.designSystem.components.TabContainer
import com.octopus.edu.kotlin.core.design.designSystem.theme.SpaceXTheme
import com.octopus.edu.kotlin.core.design.designSystem.theme.Typography
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.mock
import com.octopus.edu.kotlin.core.ui.common.LaunchedUiEffectHandler
import com.octopus.edu.kotlin.core.ui.common.LocalNavigation
import com.octopus.edu.kotlin.core.ui.common.SpaceXDestination.LaunchDetails
import com.octopus.edu.kotlin.core.ui.common.SpaceXNavigation
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEvent
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiState
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.getStatusValue
import kotlinx.coroutines.flow.StateFlow
import okhttp3.internal.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LaunchesScreen(
    modifier: Modifier = Modifier,
    navigation: SpaceXNavigation = LocalNavigation.current,
    viewModel: LaunchesViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            SpaceXTopBar(
                title = R.string.launch_list_title,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { padding ->
        LaunchesContent(
            modifier = modifier.padding(padding),
            uiState = viewState,
            onEvent = viewModel::processEvent,
        )

        EffectHandler(
            effectFlow = viewModel.effect,
            snackBarHostState = snackBarHostState,
            navigation = navigation,
            onEvent = viewModel::processEvent,
        )
    }
}

@Composable
fun EffectHandler(
    navigation: SpaceXNavigation,
    effectFlow: StateFlow<LaunchesUiContract.UiEffect?>,
    onEvent: (UiEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    val currentOnEvent by rememberUpdatedState(onEvent)
    val context = LocalContext.current
    LaunchedUiEffectHandler(
        effectFlow = effectFlow,
        onEffectConsumed = { currentOnEvent(UiEvent.MarkEffectAsConsumed) },
        onEffect = { effect ->
            when (effect) {
                is LaunchesUiContract.UiEffect.ShowError -> {
                    val result =
                        snackBarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = context.getString(R.string.retry),
                        )

                    if (result == SnackbarResult.ActionPerformed) {
                        currentOnEvent(UiEvent.ReloadLaunches)
                    }
                }

                is LaunchesUiContract.UiEffect.NavigateToLaunchDetails ->
                    navigation.navigate(
                        LaunchDetails(
                            effect.flightNumber,
                        ),
                    )
            }
        },
    )
}

@Composable
internal fun LaunchesContent(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val selectedIndex = uiState.tabs.indexOfFirst { it == uiState.tabSelected }
        val pagerState = rememberPagerState(initialPage = selectedIndex) { uiState.tabs.size }

        TabContainer(
            tabTitles = uiState.tabTitles,
            state = pagerState,
        ) { tabIndex ->

            LaunchedEffect(tabIndex) {
                onEvent(UiEvent.OnTabSelected(uiState.getTab(tabIndex)))
            }

            if (uiState.isLoading) {
                FullScreenCircularProgressIndicator()
            } else {
                LaunchList(
                    uiState,
                    onEvent,
                )
            }
        }
    }
}

@Composable
fun LaunchList(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(colorScheme.background),
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(all = 16.dp),
        verticalArrangement =
            androidx.compose.foundation.layout.Arrangement
                .spacedBy(8.dp),
    ) {
        items(uiState.launches) { item ->
            LaunchItem(
                item = item,
                onItemClicked = { flightNumber ->
                    onEvent(UiEvent.OnLaunchClicked(flightNumber))
                },
            )
        }
    }
}

@Composable
internal fun LaunchItem(
    item: Launch,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.clickable {
                onItemClicked(item.flightNumber)
            },
    ) {
        LaunchPatch(patch = item.patch)

        Spacer(
            modifier =
                Modifier.width(
                    8.dp,
                ),
        )

        Column(
            modifier =
                Modifier.weight(
                    1f,
                ),
        ) {
            Text(
                text = "${item.missionName} - ${item.rocketName}",
                style = Typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp,
                    ),
            )

            Text(
                text = item.date,
                style = Typography.bodySmall,
            )
        }

        Spacer(
            modifier =
                Modifier.width(
                    4.dp,
                ),
        )
        Text(
            text = item.launchStatus.getStatusValue(),
            style = Typography.labelSmall,
        )
    }
}

@Composable
fun LaunchPatch(
    modifier: Modifier = Modifier,
    patch: String?,
) {
    if (patch.isNullOrEmpty()) {
        Box(
            modifier =
                modifier
                    .size(width = 62.dp, height = 62.dp)
                    .clip(shapes.small)
                    .background(color = colorScheme.onBackground),
        )
    } else {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(patch)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
            contentScale = ContentScale.Companion.Fit,
            modifier =
                modifier
                    .size(62.dp)
                    .border(
                        width = 2.dp,
                        color = colorScheme.onSurfaceVariant,
                        shape =
                            androidx.compose.foundation.shape
                                .RoundedCornerShape(8.dp),
                    ).clip(
                        androidx.compose.foundation.shape
                            .RoundedCornerShape(8.dp),
                    ).background(colorScheme.onSurface),
            contentDescription = null,
        )
    }
}

@PreviewLightDark
@Composable
private fun LeagueItemPreview() {
    LaunchItem(
        item =
            Launch.Companion.mock(),
        onItemClicked = {},
    )
}

@PreviewLightDark
@Composable
private fun LeaguesScreenPreview() {
    SpaceXTheme {
        Scaffold { padding ->
            LaunchesContent(
                uiState =
                    UiState(
                        isLoading = false,
                        launches =
                            listOf(
                                Launch.Companion.mock(),
                            ).toImmutableList(),
                    ),
                onEvent = {},
                modifier =
                    Modifier.padding(padding),
            )
        }
    }
}
