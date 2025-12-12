package com.example.soulstone.ui.screens.admin_dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.StoneInventoryView
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.StoneUiItem
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val filteredStones: List<StoneUiItem> = emptyList(),
    val totalStones: Int = 0,
    val searchQuery: String = "",
    val activeSort: SortOption = SortOption.NONE,
    val sortOrder: SortOrder = SortOrder.ASC,
    val editingDescriptionId: Int? = null
)

enum class SortOption {
    NONE, CATEGORY, CHAKRA, ZODIAC, CHINESE_ZODIAC
}

enum class SortOrder {
    ASC, DESC
}

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val stoneRepository: StoneRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _activeSort = MutableStateFlow(SortOption.NONE)
    private val _sortOrder = MutableStateFlow(SortOrder.ASC)
    private val _editingDescriptionId = MutableStateFlow<Int?>(null)

    private val _uiState = MutableStateFlow(AdminDashboardUiState(isLoading = true))
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeDashboardContent()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeDashboardContent() {
        viewModelScope.launch {
            settingsRepository.language
                .flatMapLatest { languageCode ->
                    val stonesFlow = stoneRepository.getAllStonesInventory(languageCode)

                    combine(
                        stonesFlow,
                        _searchQuery,
                        _activeSort,
                        _sortOrder,
                        _editingDescriptionId
                    ) { stones, query, sortOption, sortOrder, editingId ->

                        val filteredList = if (query.isBlank()) {
                            stones
                        } else {
                            stones.filter { it.stoneName.contains(query, ignoreCase = true) }
                        }

                        val sortedList = when (sortOption) {
                            SortOption.CATEGORY -> if (sortOrder == SortOrder.ASC) filteredList.sortedBy { it.benefit } else filteredList.sortedByDescending { it.benefit }
                            SortOption.CHAKRA -> if (sortOrder == SortOrder.ASC) filteredList.sortedBy { it.chakra } else filteredList.sortedByDescending { it.chakra }
                            SortOption.ZODIAC -> if (sortOrder == SortOrder.ASC) filteredList.sortedBy { it.zodiacSign } else filteredList.sortedByDescending { it.zodiacSign }
                            SortOption.CHINESE_ZODIAC -> if (sortOrder == SortOrder.ASC) filteredList.sortedBy { it.chineseZodiacSign } else filteredList.sortedByDescending { it.chineseZodiacSign }
                            SortOption.NONE -> filteredList
                        }

                        val uiItems = sortedList.map { dbItem ->
                            StoneUiItem(
                                id = dbItem.id,
                                name = dbItem.stoneName,
                                category = dbItem.benefit ?: "",
                                zodiacSign = dbItem.zodiacSign ?: "",
                                chineseZodiacSign = dbItem.chineseZodiacSign ?: "",
                                chakra = dbItem.chakra ?: "",
                                description = dbItem.description,
                                imageResId = context.getDrawableIdByName(dbItem.imageUri ?: ""),
                                isEditing = dbItem.id == editingId,
                            )
                        }

                        val totalCount = stones.size

                        AdminDashboardUiState(
                            isLoading = false,
                            filteredStones = uiItems,
                            totalStones = totalCount,
                            searchQuery = query,
                            activeSort = sortOption,
                            sortOrder = sortOrder,
                            editingDescriptionId = editingId
                        )
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error loading inventory: ${e.message}"))
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSortOptionSelected(option: SortOption) {
        val currentSort = _activeSort.value
        val currentOrder = _sortOrder.value

        val newOrder = if (currentSort == option) {
            if (currentOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
        } else {
            SortOrder.ASC
        }

        _activeSort.value = option
        _sortOrder.value = newOrder
    }

    fun onAddStoneClick() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToAddStone)
        }
    }


    fun onEditDescriptionClick(stoneId: Int) {
        _editingDescriptionId.value = stoneId
    }

    fun onCancelEdit() {
        _editingDescriptionId.value = null
    }

    fun onSaveDescription(stoneId: Int, newDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentLanguage = settingsRepository.language.first()

                stoneRepository.updateStoneDescription(stoneId, newDescription, currentLanguage)

                _editingDescriptionId.value = null

                _uiEvent.emit(UiEvent.ShowSnackbar("Description updated successfully"))

            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to update: ${e.message}"))
            }
        }
    }
}