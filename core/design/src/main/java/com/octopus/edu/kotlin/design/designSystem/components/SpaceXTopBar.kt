package com.octopus.edu.kotlin.design.designSystem.components

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceXTopBar(
    @StringRes title: Int,
) {
    TopAppBar(
        title = {
            Text(
                text =
                    stringResource(title),
                style = typography.titleMedium,
            )
        },
        colors =
            topAppBarColors(
                containerColor = colorScheme.primary,
                titleContentColor = colorScheme.onPrimary,
            ),
    )
}
