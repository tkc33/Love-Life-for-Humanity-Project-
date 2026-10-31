package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Charity
import com.example.data.model.ResourceCategory
import com.example.data.repository.CharityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class NavTab {
    SEARCH,
    BOOKMARKS,
    HOTLINES,
    ABOUT
}

data class UiState(
    val zipCodeInput: String = "90001",
    val activeZip: String = "90001",
    val activeStateName: String? = "California",
    val selectedCategory: ResourceCategory = ResourceCategory.ALL,
    val searchKeyword: String = "",
    val filterWalkInOnly: Boolean = false,
    val filterFreeOnly: Boolean = false,
    val charities: List<Charity> = emptyList(),
    val filteredCharities: List<Charity> = emptyList(),
    val bookmarkedIds: Set<String> = emptySet(),
    val selectedCharity: Charity? = null,
    val activeTab: NavTab = NavTab.SEARCH,
    val recentZips: List<String> = listOf("90001", "60601", "10001", "77001", "30301", "33101", "80202", "98101"),
    val isSearching: Boolean = false
)

class MainViewModel(
    private val repository: CharityRepository = CharityRepository()
) : ViewModel() {

    private val _zipInput = MutableStateFlow("90001")
    private val _activeZip = MutableStateFlow("90001")
    private val _selectedCategory = MutableStateFlow(ResourceCategory.ALL)
    private val _searchKeyword = MutableStateFlow("")
    private val _filterWalkInOnly = MutableStateFlow(false)
    private val _filterFreeOnly = MutableStateFlow(false)
    private val _selectedCharity = MutableStateFlow<Charity?>(null)
    private val _activeTab = MutableStateFlow(NavTab.SEARCH)
    private val _recentZips = MutableStateFlow(listOf("90001", "60601", "10001", "77001", "30301", "33101", "80202", "98101"))

    // Raw charities matching the active ZIP
    private val _activeCharities = MutableStateFlow<List<Charity>>(emptyList())

    val uiState: StateFlow<UiState> = combine(
        _zipInput,
        _activeZip,
        _selectedCategory,
        _searchKeyword,
        _filterWalkInOnly,
        _filterFreeOnly,
        _activeCharities,
        repository.bookmarkedCharityIds,
        _selectedCharity,
        _activeTab,
        _recentZips
    ) { params ->
        val zipInput = params[0] as String
        val activeZip = params[1] as String
        val category = params[2] as ResourceCategory
        val keyword = params[3] as String
        val walkIn = params[4] as Boolean
        val freeOnly = params[5] as Boolean
        val charities = params[6] as List<Charity>
        val bookmarked = params[7] as Set<String>
        val selected = params[8] as Charity?
        val tab = params[9] as NavTab
        val recents = params[10] as List<String>

        val stateAbbr = if (activeZip.length >= 2) CharityRepository.getStateForZipPrefix(activeZip.substring(0, 2)) else null

        val filtered = charities.filter { charity ->
            // Category filter
            val matchesCategory = (category == ResourceCategory.ALL) || charity.categories.contains(category)
            
            // Keyword filter
            val matchesKeyword = keyword.isBlank() ||
                charity.name.contains(keyword, ignoreCase = true) ||
                charity.description.contains(keyword, ignoreCase = true) ||
                charity.services.any { it.contains(keyword, ignoreCase = true) } ||
                charity.tagline.contains(keyword, ignoreCase = true)

            // Flags
            val matchesWalkIn = !walkIn || charity.isWalkInFriendly
            val matchesFree = !freeOnly || charity.isFreeService

            matchesCategory && matchesKeyword && matchesWalkIn && matchesFree
        }

        UiState(
            zipCodeInput = zipInput,
            activeZip = activeZip,
            activeStateName = stateAbbr,
            selectedCategory = category,
            searchKeyword = keyword,
            filterWalkInOnly = walkIn,
            filterFreeOnly = freeOnly,
            charities = charities,
            filteredCharities = filtered,
            bookmarkedIds = bookmarked,
            selectedCharity = selected,
            activeTab = tab,
            recentZips = recents,
            isSearching = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    init {
        performSearch("90001")
    }

    fun onZipCodeInputChange(newZip: String) {
        val filtered = newZip.filter { it.isDigit() }.take(5)
        _zipInput.value = filtered
        if (filtered.length == 5) {
            performSearch(filtered)
        }
    }

    fun submitZipSearch() {
        val zip = _zipInput.value.trim()
        if (zip.isNotEmpty()) {
            performSearch(zip)
        }
    }

    fun selectRecentZip(zip: String) {
        _zipInput.value = zip
        performSearch(zip)
    }

    private fun performSearch(zip: String) {
        _activeZip.value = zip
        val results = repository.findCharitiesByZip(zip)
        _activeCharities.value = results

        // update recents
        val currentRecents = _recentZips.value.toMutableList()
        currentRecents.remove(zip)
        currentRecents.add(0, zip)
        _recentZips.value = currentRecents.take(8)
    }

    fun selectCategory(category: ResourceCategory) {
        _selectedCategory.value = category
    }

    fun setKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun toggleWalkInFilter() {
        _filterWalkInOnly.value = !_filterWalkInOnly.value
    }

    fun toggleFreeOnlyFilter() {
        _filterFreeOnly.value = !_filterFreeOnly.value
    }

    fun toggleBookmark(charityId: String) {
        repository.toggleBookmark(charityId)
    }

    fun selectCharity(charity: Charity?) {
        _selectedCharity.value = charity
    }

    fun setActiveTab(tab: NavTab) {
        _activeTab.value = tab
    }
}
