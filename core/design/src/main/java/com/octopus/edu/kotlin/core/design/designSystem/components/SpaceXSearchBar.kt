package com.octopus.edu.kotlin.core.design.designSystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.adgem.cosmicrewards.core.design.R
import com.octopus.edu.kotlin.core.design.designSystem.theme.SpaceXTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SpaceXSearchBar(
    isExpanded: Boolean,
    onSearch: (String) -> Unit,
    onSearchCleared: () -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = stringResource(R.string.search),
) {
    var queryText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { queryText }
            .debounce { 300 }
            .collect { query ->
                if (query.length >= 3) onSearch(query)
            }
    }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = queryText,
                    onQueryChange = { queryText = it },
                    onSearch = {},
                    expanded = true,
                    onExpandedChange = {},
                    placeholder = {
                        Text(text = placeHolder)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search),
                        )
                    },
                    trailingIcon = {
                        if (queryText.isNotBlank()) {
                            IconButton(onClick = {
                                queryText = ""
                                onSearchCleared()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.clear),
                                )
                            }
                        }
                    },
                    interactionSource = null,
                    modifier = if (isExpanded) Modifier.fillMaxWidth() else Modifier,
                )
            },
            expanded = false,
            onExpandedChange = {},
        ) { }
    }
}

@PreviewLightDark
@Composable
fun SpaceXSearchBarPreview() {
    SpaceXTheme {
        SpaceXSearchBar(
            isExpanded = true,
            onSearch = {},
            onSearchCleared = {},
        )
    }
}
