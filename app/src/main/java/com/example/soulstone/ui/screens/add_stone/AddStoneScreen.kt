package com.example.soulstone.ui.screens.add_stone

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.soulstone.ui.theme.Purple40 // Replace with your actual Purple color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoneScreen(
    navController: NavHostController,
    viewModel: AddStoneViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var showZodiacDialog by remember { mutableStateOf(false) }
    var showChineseDialog by remember { mutableStateOf(false) }
    var showChakraDialog by remember { mutableStateOf(false) }
    var showBenefitDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onEvent(AddStoneEvent.ImageSelected(uri)) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add New Stone",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 48.dp, top = 40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center //
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.6f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabeledField(label = "Stone name") {
                    CustomTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.onEvent(AddStoneEvent.NameChanged(it)) },
                        placeholder = "e.g. Amethyst"
                    )
                }

                LabeledField(label = "Associated Zodiac Signs") {
                    SelectorField(
                        text = if (uiState.selectedZodiacIds.isEmpty()) "" else "${uiState.selectedZodiacIds.size} selected",
                        onClick = { showZodiacDialog = true }
                    )
                }

                // 3. Chinese Zodiac Selector
                LabeledField(label = "Associated Chinese Zodiac Signs") {
                    SelectorField(
                        text = if (uiState.selectedChineseZodiacIds.isEmpty()) "" else "${uiState.selectedChineseZodiacIds.size} selected",
                        onClick = { showChineseDialog = true }
                    )
                }

                // 4. Chakra Selector
                LabeledField(label = "Associated Chakras") {
                    SelectorField(
                        text = if (uiState.selectedChakraIds.isEmpty()) "" else "${uiState.selectedChakraIds.size} selected",
                        onClick = { showChakraDialog = true }
                    )
                }

                LabeledField(label = "Associated Benefits (Uses)") {
                    SelectorField(
                        text = if (uiState.selectedBenefitIds.isEmpty()) "" else "${uiState.selectedBenefitIds.size} selected",
                        onClick = { showBenefitDialog = true }
                    )
                }

                // 5. Image Upload (Dashed Border)
                LabeledField(label = "Stone Image") {
                    DashedImageUploadBox(
                        bitmap = uiState.displayedBitmap,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onRemoveBg = { viewModel.onEvent(AddStoneEvent.RemoveBackground) },
                        isLoading = uiState.isLoading
                    )
                }

                // 6. Description
                LabeledField(label = "Stone description") {
                    CustomTextField(
                        value = uiState.description,
                        onValueChange = { viewModel.onEvent(AddStoneEvent.DescriptionChanged(it)) },
                        placeholder = "Describe the stone's properties and history...",
                        singleLine = false,
                        minLines = 5,
                        modifier = Modifier.height(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 7. Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAAAAAA)), // Grey
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }

                    Button(
                        onClick = { viewModel.onEvent(AddStoneEvent.SaveStone) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8)), // Deep Purple
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save stone", fontSize = 16.sp)
                    }
                }

                // Bottom padding
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF512DA8))
                }
            }
        }
    }

    if (showZodiacDialog) {
        MultiSelectDialog(
            title = "Select Zodiac Signs",
            options = uiState.allZodiacs.map { it.id to it.signName },
            selectedIds = uiState.selectedZodiacIds,
            onToggle = { viewModel.onEvent(AddStoneEvent.ToggleZodiac(it)) },
            onDismiss = { showZodiacDialog = false }
        )
    }

    if (showChineseDialog) {
        MultiSelectDialog(
            title = "Select Chinese Zodiacs",
            options = uiState.allChineseZodiacs.map { it.id to it.signName },
            selectedIds = uiState.selectedChineseZodiacIds,
            onToggle = { viewModel.onEvent(AddStoneEvent.ToggleChineseZodiac(it)) },
            onDismiss = { showChineseDialog = false }
        )
    }

    if (showChakraDialog) {
        MultiSelectDialog(
            title = "Select Chakras",
            options = uiState.allChakras.map { it.id to it.chakraName },
            selectedIds = uiState.selectedChakraIds,
            onToggle = { viewModel.onEvent(AddStoneEvent.ToggleChakra(it)) },
            onDismiss = { showChakraDialog = false }
        )
    }

    if (showBenefitDialog) {
        MultiSelectDialog(
            title = "Select Benefits / Uses",
            options = uiState.allBenefits.map { it.id to it.name },
            selectedIds = uiState.selectedBenefitIds,
            onToggle = { viewModel.onEvent(AddStoneEvent.ToggleBenefit(it)) },
            onDismiss = { showBenefitDialog = false }
        )
    }
}


@Composable
fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            focusedBorderColor = Color(0xFF512DA8)
        ),
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = if(singleLine) ImeAction.Next else ImeAction.Default
        )
    )
}

@Composable
fun SelectorField(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text.ifEmpty { "Select..." },
            color = if (text.isEmpty()) Color.LightGray else Color.Black
        )

        Icon(
            imageVector = Icons.Default.UnfoldMore,
            contentDescription = "Select",
            tint = Color.Gray,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun DashedImageUploadBox(
    bitmap: Bitmap?,
    onClick: () -> Unit,
    onRemoveBg: () -> Unit,
    isLoading: Boolean
) {
    val stroke = Stroke(
        width = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .drawBehind {
                drawRoundRect(
                    color = Color.LightGray,
                    style = stroke,
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Button(
                onClick = onRemoveBg,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8).copy(alpha = 0.9f))
            ) {
                Text("Remove BG", fontSize = 12.sp)
            }
        } else {
            // Placeholder
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Click to upload a stone picture",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun MultiSelectDialog(
    title: String,
    options: List<Pair<Int, String>>,
    selectedIds: Set<Int>,
    onToggle: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 400.dp), // Limit height
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Scrollable List
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false) // Allow shrinking if list is small
                        .verticalScroll(rememberScrollState())
                ) {
                    options.forEach { (id, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggle(id) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                            if (selectedIds.contains(id)) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFF512DA8)
                                )
                            }
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8))
                ) {
                    Text("Done")
                }
            }
        }
    }
}