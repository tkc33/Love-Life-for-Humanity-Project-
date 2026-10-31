package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.NavTab
import com.example.ui.components.AboutView
import com.example.ui.components.BookmarksView
import com.example.ui.components.CharityDetailBottomSheet
import com.example.ui.components.CharitySearchScreen
import com.example.ui.components.HotlinesView
import com.example.ui.components.ResourceGuidePreviewSheet
import com.example.ui.components.SocialShareBottomSheet
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.util.IntentUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LoveLifeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveLifeApp(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showShareSheet by remember { mutableStateOf(false) }
    var showGuideSheet by remember { mutableStateOf(false) }

    // Back handling
    BackHandler(enabled = showGuideSheet || showShareSheet || uiState.selectedCharity != null || uiState.activeTab != NavTab.SEARCH) {
        if (showGuideSheet) {
            showGuideSheet = false
        } else if (showShareSheet) {
            showShareSheet = false
        } else if (uiState.selectedCharity != null) {
            viewModel.selectCharity(null)
        } else if (uiState.activeTab != NavTab.SEARCH) {
            viewModel.setActiveTab(NavTab.SEARCH)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_launcher_icon_1793454896749),
                                    contentDescription = "Love Life 4 Humanity logo",
                                    modifier = Modifier.size(28.dp).clip(CircleShape)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "The Love Life for Humanity Project",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Print 1-page emergency guide button in header
                    IconButton(
                        onClick = { showGuideSheet = true },
                        modifier = Modifier.testTag("top_bar_print_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print 1-Page Emergency Resource Guide",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Social media share button in header
                    IconButton(
                        onClick = { showShareSheet = true },
                        modifier = Modifier.testTag("top_bar_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share The Love Life for Humanity Project",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Quick 211 direct call action in header
                    FilledTonalButton(
                        onClick = {
                            IntentUtils.dialPhoneNumber(context, "211")
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("top_bar_211_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "2-1-1",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.activeTab == NavTab.SEARCH,
                    onClick = { viewModel.setActiveTab(NavTab.SEARCH) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == NavTab.SEARCH) Icons.Default.Search else Icons.Outlined.Search,
                            contentDescription = "Find Help"
                        )
                    },
                    label = { Text("Find Help") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_search_tab")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == NavTab.BOOKMARKS,
                    onClick = { viewModel.setActiveTab(NavTab.BOOKMARKS) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (uiState.bookmarkedIds.isNotEmpty()) {
                                    Badge {
                                        Text(uiState.bookmarkedIds.size.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.activeTab == NavTab.BOOKMARKS) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Saved Resources"
                            )
                        }
                    },
                    label = { Text("Saved") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_bookmarks_tab")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == NavTab.HOTLINES,
                    onClick = { viewModel.setActiveTab(NavTab.HOTLINES) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == NavTab.HOTLINES) Icons.Default.Emergency else Icons.Outlined.Emergency,
                            contentDescription = "Hotlines"
                        )
                    },
                    label = { Text("Hotlines") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_hotlines_tab")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == NavTab.ABOUT,
                    onClick = { viewModel.setActiveTab(NavTab.ABOUT) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeTab == NavTab.ABOUT) Icons.Default.Info else Icons.Outlined.Info,
                            contentDescription = "About"
                        )
                    },
                    label = { Text("About") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_about_tab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp) // Responsive tablet & large screen adaptation
            ) {
                when (uiState.activeTab) {
                    NavTab.SEARCH -> {
                        CharitySearchScreen(
                            uiState = uiState,
                            onZipChange = viewModel::onZipCodeInputChange,
                            onSubmitSearch = viewModel::submitZipSearch,
                            onSelectRecentZip = viewModel::selectRecentZip,
                            onCategorySelected = viewModel::selectCategory,
                            onKeywordChanged = viewModel::setKeyword,
                            onToggleWalkIn = viewModel::toggleWalkInFilter,
                            onToggleFreeOnly = viewModel::toggleFreeOnlyFilter,
                            onToggleBookmark = viewModel::toggleBookmark,
                            onSelectCharity = viewModel::selectCharity,
                            onOpenShareSheet = { showShareSheet = true },
                            onOpenPrintGuide = { showGuideSheet = true }
                        )
                    }

                    NavTab.BOOKMARKS -> {
                        val bookmarkedList = uiState.charities.filter { uiState.bookmarkedIds.contains(it.id) }
                        BookmarksView(
                            bookmarkedCharities = bookmarkedList,
                            onToggleBookmark = viewModel::toggleBookmark,
                            onSelectCharity = viewModel::selectCharity,
                            onNavigateToSearch = { viewModel.setActiveTab(NavTab.SEARCH) }
                        )
                    }

                    NavTab.HOTLINES -> {
                        HotlinesView()
                    }

                    NavTab.ABOUT -> {
                        AboutView(
                            onNavigateToSearch = { viewModel.setActiveTab(NavTab.SEARCH) }
                        )
                    }
                }
            }

            // Detail Bottom Sheet when charity is selected
            uiState.selectedCharity?.let { charity ->
                CharityDetailBottomSheet(
                    charity = charity,
                    isBookmarked = uiState.bookmarkedIds.contains(charity.id),
                    onToggleBookmark = { viewModel.toggleBookmark(charity.id) },
                    onDismiss = { viewModel.selectCharity(null) }
                )
            }

            // Social Media Share Bottom Sheet
            if (showShareSheet) {
                SocialShareBottomSheet(
                    onDismiss = { showShareSheet = false }
                )
            }

            // 1-Page Printable Resource Guide Preview Sheet
            if (showGuideSheet) {
                ResourceGuidePreviewSheet(
                    zipCode = uiState.activeZip,
                    charities = uiState.charities,
                    onDismiss = { showGuideSheet = false }
                )
            }
        }
    }
}
