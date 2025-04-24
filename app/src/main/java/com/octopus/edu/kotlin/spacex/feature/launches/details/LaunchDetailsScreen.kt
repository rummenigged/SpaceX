package com.octopus.edu.kotlin.spacex.feature.launches.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.octopus.edu.kotlin.spacex.R
import com.octopus.edu.kotlin.spacex.design.FullScreenCircularProgressIndicator
import com.octopus.edu.kotlin.spacex.design.SpaceXTopBar
import com.octopus.edu.kotlin.spacex.feature.common.LaunchedUiEffectHandler
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LaunchDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: LaunchDetailsViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { SpaceXTopBar(R.string.details_title) },
    ) { padding ->
        LaunchDetailsScreen(
            modifier = modifier.padding(padding),
            uiState = viewState,
        )

        EffectHandler(
            effectFlow = viewModel.effect,
            onEvent = viewModel::processEvent,
        )
    }
}

@Composable
fun EffectHandler(
    effectFlow: StateFlow<UiEffect?>,
    onEvent: (UiEvent) -> Unit,
) {
    val currentOnEvent by rememberUpdatedState(onEvent)

    LaunchedUiEffectHandler(
        effectFlow = effectFlow,
        onEffectConsumed = { currentOnEvent(UiEvent.MarkEffectAsConsumed) },
        onEffect = { effect ->
            when (effect) {
                is UiEffect.ShowError -> TODO()
            }
        },
    )
}

@Composable
fun LaunchDetailsScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        FullScreenCircularProgressIndicator()
    } else {
        Column(modifier = modifier) {
            RocketImage(
                imageUrl =
                    uiState.details
                        ?.rocket
                        ?.images
                        ?.firstOrNull(),
            )

            uiState.details?.rocket?.name?.let { rocketName ->
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Rocket Name: $rocketName",
                )
            }

            uiState.details?.missionName?.let { missionName ->
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Mission Name: $missionName",
                )
            }

            uiState.details?.siteName?.let { siteName ->
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Site Name: $siteName",
                )
            }
        }
    }
}

@Composable
internal fun RocketImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    imageUrl?.let { url ->
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(url)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
            contentScale = ContentScale.Fit,
            modifier =
                modifier
                    .fillMaxWidth(),
            contentDescription = null,
        )
    } ?: Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shapes.small)
                .background(color = colorScheme.onBackground),
    )
}
