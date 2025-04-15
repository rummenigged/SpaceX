package com.octopus.edu.kotlin.spacex.design

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.octopus.edu.kotlin.spacex.R
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceXTopBar(
    @StringRes title: Int,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                style = typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.primary,
            titleContentColor = colorScheme.onPrimary
        )
    )
}