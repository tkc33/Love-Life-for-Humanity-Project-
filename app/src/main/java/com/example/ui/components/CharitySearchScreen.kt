package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.Charity
import com.example.data.model.ResourceCategory
import com.example.ui.UiState

@Composable
fun CharitySearchScreen(
    uiState: UiState,
    onZipChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
    onSelectRecentZip: (String) -> Unit,
    onCategorySelected: (ResourceCategory) -> Unit,
    onKeywordChanged: (String) -> Unit,
    onToggleWalkIn: () -> Unit,
    onToggleFreeOnly: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    onSelectCharity: (Charity) -> Unit,
    onOpenShareSheet: () -> Unit = {},
    onOpenPrintGuide: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("charity_search_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)
        ) {
            // ZIP Code Input & Sample Chips Header
            item {
                ZipCodeHeader(
                    zipInput = uiState.zipCodeInput,
                    activeZip = uiState.activeZip,
                    stateName = uiState.activeStateName,
                    resultCount = uiState.filteredCharities.size,
                    onZipChange = onZipChange,
                    onSubmitSearch = onSubmitSearch,
                    onSelectRecentZip = onSelectRecentZip
                )
            }

            // 1-Page Printable Emergency Guide Action Banner
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("printable_guide_banner_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "1-Page Printable Emergency Guide",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Print or export an 8.5\"x11\" summary with phone numbers & intake checklist for ZIP ${uiState.activeZip}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onOpenPrintGuide,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("open_print_guide_button")
                        ) {
                            Text(
                                text = "Print",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Community Social Share Banner
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("community_share_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Share with Neighbors & Community",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Share via WhatsApp, Facebook, X, or SMS to help families find resources.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onOpenShareSheet,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.testTag("open_share_sheet_button")
                        ) {
                            Text(
                                text = "Share",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }

            // Keyword Search Bar & Category Filters
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchKeyword,
                        onValueChange = onKeywordChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("keyword_search_input"),
                        placeholder = { Text("Filter by need (e.g. food, diapers, rent, dental)") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchKeyword.isNotEmpty()) {
                                IconButton(
                                    onClick = { onKeywordChanged("") },
                                    modifier = Modifier.testTag("clear_keyword_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search keyword"
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Secondary Toggles: Walk-ins & Free Only
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.filterWalkInOnly,
                            onClick = onToggleWalkIn,
                            label = { Text("Walk-in Only") },
                            modifier = Modifier.testTag("filter_walk_in_chip")
                        )

                        FilterChip(
                            selected = uiState.filterFreeOnly,
                            onClick = onToggleFreeOnly,
                            label = { Text("100% Free Only") },
                            modifier = Modifier.testTag("filter_free_chip")
                        )
                    }
                }
            }

            // Resource Categories Filter
            item {
                CategoryFilterChips(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            // Results List
            if (uiState.filteredCharities.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Charities Found for Current Filters",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try clearing the keyword search, selecting 'All Needs', or entering another ZIP code.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    onKeywordChanged("")
                                    onCategorySelected(ResourceCategory.ALL)
                                    if (uiState.filterWalkInOnly) onToggleWalkIn()
                                    if (uiState.filterFreeOnly) onToggleFreeOnly()
                                },
                                modifier = Modifier.testTag("reset_filters_button")
                            ) {
                                Text("Reset All Filters")
                            }
                        }
                    }
                }
            } else {
                items(uiState.filteredCharities, key = { it.id }) { charity ->
                    CharityCard(
                        charity = charity,
                        isBookmarked = uiState.bookmarkedIds.contains(charity.id),
                        onToggleBookmark = { onToggleBookmark(charity.id) },
                        onSelectCharity = { onSelectCharity(charity) }
                    )
                }
            }
        }
    }
}
