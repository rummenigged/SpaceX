package com.octopus.edu.kotlin.feature.launches

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.octopus.edu.kotlin.core.design.designSystem.theme.SpaceXTheme
import com.octopus.edu.kotlin.core.design.designSystem.theme.Typography
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.mock
import com.octopus.edu.kotlin.core.ui.common.LaunchedUiEffectHandler
import com.octopus.edu.kotlin.core.ui.common.LocalNavigation
import com.octopus.edu.kotlin.core.ui.common.SpaceXDestination.LaunchDetails
import com.octopus.edu.kotlin.core.ui.common.SpaceXNavigation
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

    Scaffold(
        modifier = modifier,
        topBar = {
            SpaceXTopBar(
                title = R.string.launch_list_title,
            )
        },
    ) { padding ->
        LaunchesScreenContent(
            modifier = modifier.padding(padding),
            uiState = viewState,
            onEvent = viewModel::processEvent,
        )

        EffectHandler(
            effectFlow = viewModel.effect,
            navigation = navigation,
            onEvent = viewModel::processEvent,
        )
    }
}

@Composable
fun EffectHandler(
    navigation: SpaceXNavigation,
    effectFlow: StateFlow<LaunchesUiContract.UiEffect?>,
    onEvent: (LaunchesUiContract.UiEvent) -> Unit,
) {
    val currentOnEvent by rememberUpdatedState(onEvent)
    val context = LocalContext.current
    LaunchedUiEffectHandler(
        effectFlow = effectFlow,
        onEffectConsumed = { currentOnEvent(LaunchesUiContract.UiEvent.MarkEffectAsConsumed) },
        onEffect = { effect ->
            when (effect) {
                is LaunchesUiContract.UiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
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
internal fun LaunchesScreenContent(
    uiState: LaunchesUiContract.UiState,
    onEvent: (LaunchesUiContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        FullScreenCircularProgressIndicator()
    } else {
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
                        onEvent(LaunchesUiContract.UiEvent.OnLaunchClicked(flightNumber))
                    },
                )
            }
        }
    }
}

@Composable
internal fun LaunchItem(
    item: Launch,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Row(modifier = modifier.clickable { onItemClicked(item.flightNumber) }) {
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
            LaunchesScreenContent(
                uiState =
                    LaunchesUiContract.UiState(
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
