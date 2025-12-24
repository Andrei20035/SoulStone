package com.example.soulstone.ui.screens.admin_dashboard

import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.StoneInventoryView
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.StoneUiItem
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.util.getDrawableIdByName
import com.example.soulstone.util.toSqlList
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
    val searchQuery: TextFieldValue = TextFieldValue(""),
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
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchQuery = MutableStateFlow(TextFieldValue(""))
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
            // 1. Force English Language directly (No more observing settings)
            val stonesFlow = stoneRepository.getAllStonesInventory(LanguageCode.ENGLISH)

            combine(
                stonesFlow,
                _searchQuery,
                _editingDescriptionId
            ) { stones, queryTextField, editingId ->

                val queryText = queryTextField.text
                // 2. Only Search Filtering (No Sorting)
                val filteredList = if (queryText.isBlank()) {
                    stones
                } else {
                    stones.filter { it.stoneName.contains(queryText, ignoreCase = true) }
                }

                // 3. Map to UI Items
                val uiItems = filteredList.map { dbItem ->
                    val imageName = dbItem.imageUri ?: ""
                    val drawableId = context.getDrawableIdByName(imageName)

                    StoneUiItem(
                        id = dbItem.id,
                        name = dbItem.stoneName,
                        category = dbItem.benefit.toSqlList(),
                        zodiacSign = dbItem.zodiacSign.toSqlList(),
                        chineseZodiacSign = dbItem.chineseZodiacSign.toSqlList(),
                        chakra = dbItem.chakra.toSqlList(),
                        description = dbItem.description,
                        imageResId = drawableId,
                        imageFileName = if (drawableId == 0) imageName else null,
                        isEditing = dbItem.id == editingId,
                    )
                }

                AdminDashboardUiState(
                    isLoading = false,
                    filteredStones = uiItems,
                    totalStones = stones.size,
                    searchQuery = queryTextField,
                    activeSort = SortOption.NONE,
                    sortOrder = SortOrder.ASC,
                    editingDescriptionId = editingId
                )
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

    fun onSearchQueryChange(query: TextFieldValue) {
        _searchQuery.value = query
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
                // 4. Force English when saving updates
                stoneRepository.updateStoneDescription(
                    stoneId,
                    newDescription,
                    LanguageCode.ENGLISH
                )

                _editingDescriptionId.value = null
                _uiEvent.emit(UiEvent.ShowSnackbar("Description updated successfully"))

            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to update: ${e.message}"))
            }
        }
    }
}