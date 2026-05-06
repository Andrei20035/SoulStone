package com.example.soulstone.ui.screens.gemstone_index

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.StoneGridItem
import com.example.soulstone.util.getDrawableIdByName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GemstoneIndexUiState(
    val isLoading: Boolean = false,
    val currentPageItems: List<StoneGridItem> = emptyList(),
    val pageIndex: Int = 0
)

@HiltViewModel
class GemstoneIndexViewModel @Inject constructor(
    private val stoneRepository: StoneRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // --- CONFIGURATION ---
    private val COLUMNS = 4
    private val ROWS = 6
    private val TOTAL_SLOTS = COLUMNS * ROWS
    private val NAV_SLOT_PREV = 20
    private val NAV_SLOT_NEXT = 23
    private val STONES_PER_PAGE = TOTAL_SLOTS - 2

    private var allStones: List<StoneGridItem.StoneData> = emptyList()

    private val _uiState = MutableStateFlow(GemstoneIndexUiState(isLoading = true))
    val uiState: StateFlow<GemstoneIndexUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadContent()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadContent() {
        viewModelScope.launch {
            settingsRepository.language.flatMapLatest { language ->
                stoneRepository.getAllStonesForIndex(language)
            }
                .map { dbList ->
                    dbList.map { item ->
                        val imageName = item.imageUri ?: ""
                        val resId = context.getDrawableIdByName(imageName)

                        StoneGridItem.StoneData(
                            id = item.id,
                            name = item.name,
                            imageResId = resId,
                            imageFileName = if (resId == 0) imageName else null
                        )
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect { mappedStones ->
                    allStones = mappedStones
                    updatePage(0)
                }
        }
    }

    fun onNextPage() {
        val maxPages = (allStones.size + STONES_PER_PAGE - 1) / STONES_PER_PAGE
        if (_uiState.value.pageIndex < maxPages - 1) {
            updatePage(_uiState.value.pageIndex + 1)
        }
    }

    fun onPrevPage() {
        if (_uiState.value.pageIndex > 0) {
            updatePage(_uiState.value.pageIndex - 1)
        }
    }

    fun onStoneClicked(stoneId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToStoneDetail(stoneId))
        }
    }

    private fun updatePage(index: Int) {
        val pageItems = generatePageList(index)
        _uiState.update {
            it.copy(
                isLoading = false,
                pageIndex = index,
                currentPageItems = pageItems
            )
        }
    }

    private fun generatePageList(pageIndex: Int): List<StoneGridItem> {
        val start = pageIndex * STONES_PER_PAGE
        val end = (start + STONES_PER_PAGE).coerceAtMost(allStones.size)
        val stonesForPage = allStones.subList(start, end).toMutableList()

        val fullGridList = ArrayList<StoneGridItem>()
        var stoneIterator = 0

        for (i in 0 until TOTAL_SLOTS) {
            when (i) {
                NAV_SLOT_PREV -> {
                    fullGridList.add(
                        StoneGridItem.Navigation(
                            type = StoneGridItem.NavType.PREVIOUS,
                            enabled = pageIndex > 0
                        )
                    )
                }
                NAV_SLOT_NEXT -> {
                    val maxPages = (allStones.size + STONES_PER_PAGE - 1) / STONES_PER_PAGE
                    fullGridList.add(
                        StoneGridItem.Navigation(
                            type = StoneGridItem.NavType.NEXT,
                            enabled = pageIndex < maxPages - 1
                        )
                    )
                }
                else -> {
                    if (stoneIterator < stonesForPage.size) {
                        fullGridList.add(stonesForPage[stoneIterator])
                        stoneIterator++
                    } else {
                        fullGridList.add(StoneGridItem.Empty)
                    }
                }
            }
        }
        return fullGridList
    }
}