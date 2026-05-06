package com.example.soulstone.ui.screens.add_stone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.models.*
import com.example.soulstone.data.repository.StoneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.graphics.scale
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.remote.TranslationApiService
import com.example.soulstone.data.remote.TranslationRequest
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.BuildConfig
import com.example.soulstone.data.pojos.ZodiacSignListItem
import com.example.soulstone.data.repository.BenefitRepository
import com.example.soulstone.data.repository.ChakraRepository
import com.example.soulstone.data.repository.ChineseZodiacSignRepository
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.ui.models.BenefitUiItem
import com.example.soulstone.ui.models.ChakraListUiItem
import com.example.soulstone.ui.models.ChineseSignUiDetails
import com.example.soulstone.ui.models.ZodiacSignListUiItem
import com.example.soulstone.util.getDrawableIdByName
import dagger.hilt.android.lifecycle.HiltViewModel

data class AddStoneUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val description: String = "",
    val selectedImageUri: Uri? = null,
    val displayedBitmap: Bitmap? = null,

    val selectedZodiacIds: Set<Int> = emptySet(),
    val selectedChineseZodiacIds: Set<Int> = emptySet(),
    val selectedChakraIds: Set<Int> = emptySet(),
    val selectedBenefitIds: Set<Int> = emptySet(),

    val allZodiacs: List<ZodiacSignListUiItem> = emptyList(),
    val allChineseZodiacs: List<ZodiacSignListUiItem> = emptyList(),
    val allChakras: List<ChakraListUiItem> = emptyList(),
    val allBenefits: List<BenefitUiItem> = emptyList(),

    val isSaved: Boolean = false,
    val error: String? = null
)

sealed interface AddStoneEvent {
    data class NameChanged(val value: String) : AddStoneEvent
    data class DescriptionChanged(val value: String) : AddStoneEvent
    data class ImageSelected(val uri: Uri?) : AddStoneEvent
    object RemoveBackground : AddStoneEvent
    data class ToggleZodiac(val id: Int) : AddStoneEvent
    data class ToggleChineseZodiac(val id: Int) : AddStoneEvent
    data class ToggleChakra(val id: Int) : AddStoneEvent
    data class ToggleBenefit(val id: Int) : AddStoneEvent
    object SaveStone : AddStoneEvent
    object ErrorShown : AddStoneEvent
}

@HiltViewModel
class AddStoneViewModel @Inject constructor(
    private val stoneRepository: StoneRepository,
    private val zodiacRepository: ZodiacSignRepository,
    private val chineseRepository: ChineseZodiacSignRepository,
    private val chakraRepository: ChakraRepository,
    private val benefitRepository: BenefitRepository,
    private val translationApi: TranslationApiService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddStoneUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSelectionData()
    }

    private fun loadSelectionData() {
        viewModelScope.launch {
            combine(
                zodiacRepository.getZodiacSignListItems(LanguageCode.ENGLISH),        // Ensure these exist in your Repo
                chineseRepository.getAllChineseZodiacSignListItems(LanguageCode.ENGLISH), // and return Flow<List<T>>
                chakraRepository.getAllChakraListItems(LanguageCode.ENGLISH),
                benefitRepository.getAllTranslatedBenefits(LanguageCode.ENGLISH)
            ) { zodiacs, chineseZodiacs, chakras, benefits ->

                val zodiacUiItems = zodiacs.map { item ->
                    ZodiacSignListUiItem(
                        id = item.id,
                        keyName = item.keyName,
                        signName = item.signName,
                        imageResId = context.getDrawableIdByName(item.imageName)
                    )
                }

                val chineseUiItems = chineseZodiacs.map { item ->
                    ZodiacSignListUiItem(
                        id = item.id,
                        keyName = item.keyName,
                        signName = item.signName,
                        imageResId = context.getDrawableIdByName(item.imageName)
                    )
                }

                val chakraUiItems = chakras.map { item ->
                    ChakraListUiItem(
                        id = item.id,
                        sanskritName = item.sanskritName,
                        chakraName = item.chakraName,
                        imageResId = context.getDrawableIdByName(item.imageName)
                    )
                }

                val benefitUiItems = benefits.map { item ->
                    val imageName = item.imageName // Assuming entity has this
                    val resId = context.getDrawableIdByName(imageName)
                    BenefitUiItem(
                        id = item.id,
                        name = item.translatedName,
                        imageResId = resId,
                        imageFileName = if (resId == 0) imageName else null
                    )
                }
                DataResult(zodiacUiItems, chineseUiItems, chakraUiItems, benefitUiItems)
            }
                .catch { e ->
                    _uiState.update { it.copy(error = "Failed to load lists: ${e.message}") }
                }
                .collect { result ->
                    _uiState.update { it.copy(
                        allZodiacs = result.zodiacs,
                        allChineseZodiacs = result.chineseZodiacs,
                        allChakras = result.chakras,
                        allBenefits = result.benefits
                    )}
                }
        }
    }

    private data class DataResult(
        val zodiacs: List<ZodiacSignListUiItem>,
        val chineseZodiacs: List<ZodiacSignListUiItem>,
        val chakras: List<ChakraListUiItem>,
        val benefits: List<BenefitUiItem>
    )

    fun onEvent(event: AddStoneEvent) {
        when (event) {
            is AddStoneEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }
            is AddStoneEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.value) }
            is AddStoneEvent.ImageSelected -> handleImageSelection(event.uri)
            is AddStoneEvent.RemoveBackground -> handleRemoveBackground()
            is AddStoneEvent.ToggleZodiac -> _uiState.update { it.copy(selectedZodiacIds = toggleSet(it.selectedZodiacIds, event.id)) }
            is AddStoneEvent.ToggleChineseZodiac -> _uiState.update { it.copy(selectedChineseZodiacIds = toggleSet(it.selectedChineseZodiacIds, event.id)) }
            is AddStoneEvent.ToggleChakra -> _uiState.update { it.copy(selectedChakraIds = toggleSet(it.selectedChakraIds, event.id)) }
            is AddStoneEvent.ToggleBenefit -> _uiState.update { it.copy(selectedBenefitIds = toggleSet(it.selectedBenefitIds, event.id)) }
            is AddStoneEvent.SaveStone -> saveStone()
            is AddStoneEvent.ErrorShown -> _uiState.update { it.copy(error = null) }
        }
    }



    private fun toggleSet(current: Set<Int>, id: Int): Set<Int> {
        return if (current.contains(id)) current - id else current + id
    }

    private fun handleImageSelection(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                val compressed = compressBitmap(originalBitmap)
                _uiState.update { it.copy(selectedImageUri = uri, displayedBitmap = compressed) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load image") }
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val maxSize = 1024
        val width = bitmap.width
        val height = bitmap.height
        val ratio = width.toFloat() / height.toFloat()
        val newWidth = if (width > height) maxSize else (maxSize * ratio).toInt()
        val newHeight = if (width > height) (maxSize / ratio).toInt() else maxSize

        return if (width > maxSize || height > maxSize) {
            bitmap.scale(newWidth, newHeight)
        } else {
            bitmap
        }
    }

    private fun handleRemoveBackground() {
        val currentBitmap = _uiState.value.displayedBitmap ?: return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                // TODO: IMPLEMENT REAL ML KIT REMOVAL HERE
                // val result = MLKitSegmenter.process(currentBitmap)
                kotlinx.coroutines.delay(1000) // Mock delay
                _uiState.update { it.copy(isLoading = false) } // Update with result
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Bg Removal Failed") }
            }
        }
    }

    private fun saveStone() {
        val state = _uiState.value
        if (state.name.isBlank() || state.displayedBitmap == null) {
            _uiState.update { it.copy(error = "Name and Image are required") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageName = "stone_${System.currentTimeMillis()}.png"
                context.openFileOutput(imageName, Context.MODE_PRIVATE).use { stream ->
                    state.displayedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }

                val newStone = Stone(name = state.name, imageUri = imageName)
                val stoneId = stoneRepository.insertStone(newStone).toInt()

                state.selectedZodiacIds.forEach { stoneRepository.insertZodiacCrossRef(
                    StoneZodiacCrossRef(stoneId, it)
                ) }
                state.selectedChineseZodiacIds.forEach { stoneRepository.insertChineseZodiacCrossRef(
                    StoneChineseZodiacCrossRef(stoneId, it)
                ) }
                state.selectedChakraIds.forEach { stoneRepository.insertChakraCrossRef(
                    StoneChakraCrossRef(stoneId, it)
                ) }

                val apiKey = BuildConfig.TRANSLATION_API_KEY

                if (apiKey.isBlank()) {
                    _uiState.update { it.copy(error = "API Key not found!") }
                    return@launch
                }

                LanguageCode.entries.forEach { targetLang ->

                    val (finalName, finalDesc) = if (targetLang == LanguageCode.ENGLISH) {
                        state.name to state.description
                    } else {
                        try {
                            val requestBody = TranslationRequest(
                                q = listOf(state.name, state.description),
                                target = targetLang.code
                            )

                            val response = translationApi.translate(apiKey, requestBody)
                            val results = response.data.translations

                            val tName = results.getOrNull(0)?.translatedText ?: state.name
                            val tDesc = results.getOrNull(1)?.translatedText ?: state.description

                            val cleanName = android.text.Html.fromHtml(tName, 0).toString()
                            val cleanDesc = android.text.Html.fromHtml(tDesc, 0).toString()

                            cleanName to cleanDesc

                        } catch (e: Exception) {
                            e.printStackTrace()
                            state.name to state.description
                        }
                    }

                    stoneRepository.insertTranslation(
                        StoneTranslation(
                            stoneId = stoneId,
                            languageCode = targetLang,
                            name = finalName,
                            description = finalDesc
                        )
                    )
                }

                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Save failed: ${e.message}") }
            }
        }
    }
}