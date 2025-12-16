package com.example.soulstone.ui.screens.admin_dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.soulstone.ui.components.UniversalImage
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.StoneUiItem
import com.example.soulstone.ui.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    viewModel: AdminDashboardViewModel = hiltViewModel(),
    onExitAdmin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateToAddStone -> {
                    navController.navigate(AppScreen.AddStone.route)
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(
                        onClick = { onExitAdmin() },
                        modifier = Modifier.padding(start = 48.dp, top = 40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )

            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(60.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(200.dp))
            Text(
                "Welcome, Admin!",
                fontWeight = FontWeight.Bold,
                fontSize = 80.sp
            )
            Spacer(Modifier.height(60.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Stones",
                    value = uiState.totalStones.toString(),
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.4f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Manage Inventory",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { viewModel.onAddStoneClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add new stone",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(0.4f),
                placeholder = { Text(
                    "Search by stone name...",
                    fontSize = 20.sp // Larger placeholder
                ) },
                leadingIcon = { Icon(Icons.Default.Search,modifier = Modifier.size(28.dp), contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

//            Row(
//                modifier = Modifier.fillMaxWidth(0.4f),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                SortButton(
//                    text = "Category",
//                    isSelected = uiState.activeSort == SortOption.CATEGORY,
//                    sortOrder = uiState.sortOrder,
//                    onClick = { viewModel.onSortOptionSelected(SortOption.CATEGORY) },
//                    modifier = Modifier.weight(1f)
//                )
//                SortButton(
//                    text = "Chakra",
//                    isSelected = uiState.activeSort == SortOption.CHAKRA,
//                    sortOrder = uiState.sortOrder,
//                    onClick = { viewModel.onSortOptionSelected(SortOption.CHAKRA) },
//                    modifier = Modifier.weight(1f)
//                )
//                SortButton(
//                    text = "Zodiac",
//                    isSelected = uiState.activeSort == SortOption.ZODIAC,
//                    sortOrder = uiState.sortOrder,
//                    onClick = { viewModel.onSortOptionSelected(SortOption.ZODIAC) },
//                    modifier = Modifier.weight(1f)
//                )
//                SortButton(
//                    text = "Chinese",
//                    isSelected = uiState.activeSort == SortOption.CHINESE_ZODIAC,
//                    sortOrder = uiState.sortOrder,
//                    onClick = { viewModel.onSortOptionSelected(SortOption.CHINESE_ZODIAC) },
//                    modifier = Modifier.weight(1f)
//                )
//            }

//            Spacer(modifier = Modifier.height(16.dp))

            InventoryListTable(
                stones = uiState.filteredStones,
                editingId = uiState.editingDescriptionId,
                onEditClick = viewModel::onEditDescriptionClick,
                onSaveDescription = viewModel::onSaveDescription,
                onCancelEdit = viewModel::onCancelEdit
            )
        }
    }
}


@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        border = BorderStroke(width = 3.dp, color = Color(0xFFE5E7EB)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 40.sp, color = Color.Gray)
            Text(text = value, fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun SortButton(
    text: String,
    isSelected: Boolean,
    sortOrder: SortOrder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFE8F0FE) else Color.Transparent
    val contentColor = if (isSelected) Color(0xFF1967D2) else Color.Gray
    val borderColor = if (isSelected) Color(0xFF1967D2) else Color.LightGray

    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50), // Pill shape
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp) // Compact
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, fontSize = 20.sp, maxLines = 1)
            if (isSelected) {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = if (sortOrder == SortOrder.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun InventoryListTable(
    stones: List<StoneUiItem>, // This can still be a normal List for now
    editingId: Int?,
    onEditClick: (Int) -> Unit,
    onSaveDescription: (Int, String) -> Unit,
    onCancelEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderCell("Image", 0.15f)
                HeaderCell("Name", 0.2f)
                HeaderCell("Category", 0.15f)
                HeaderCell("Signs", 0.2f)
                HeaderCell("Chakras", 0.15f)
                HeaderCell("Description", 0.15f)
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.heightIn(max = 500.dp)
            ) {
                items(
                    items = stones,
                    key = { it.id }
                ) { stone ->
                    InventoryRow(
                        stone = stone,
                        isEditing = stone.id == editingId,
                        onEditClick = { onEditClick(stone.id) },
                        onSaveDescription = { newDesc -> onSaveDescription(stone.id, newDesc) },
                        onCancelEdit = onCancelEdit
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text.uppercase(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(weight)
    )
}

@Composable
fun InventoryRow(
    stone: StoneUiItem,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveDescription: (String) -> Unit,
    onCancelEdit: () -> Unit
) {
    var tempDescription by remember(stone.description) { mutableStateOf(stone.description) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.15f)
                .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            UniversalImage(
                imageResId = stone.imageResId,
                imageFileName = stone.imageFileName,
                contentDescription = stone.name
            )
        }

        Text(
            text = stone.name,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(0.2f)
                .padding(horizontal = 4.dp)
        )

        Column(
            modifier = Modifier.weight(0.15f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            stone.category.forEach { item ->
                Text(
                    text = item,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Loop through Western Zodiacs
            stone.zodiacSign.forEach { sign ->
                Text(
                    text = sign,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }

             Spacer(modifier = Modifier.height(2.dp))

            // 2. Loop through Chinese Zodiacs
            stone.chineseZodiacSign.forEach { sign ->
                Text(
                    text = sign,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier.weight(0.15f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            stone.chakra.forEach { item ->
                Text(
                    text = item,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(
            modifier = Modifier.weight(0.15f),
            contentAlignment = Alignment.Center // Centrează conținutul în celulă
        ) {
            if (isEditing) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BasicTextField(
                        value = tempDescription,
                        onValueChange = { tempDescription = it },
                        modifier = Modifier
                            .background(Color(0xFFFFF8E1))
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                            .heightIn(min = 40.dp)
                            .fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.Center) {
                        IconButton(onClick = { onSaveDescription(tempDescription) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Check, "Save", tint = Color.Green)
                        }
                        IconButton(onClick = onCancelEdit, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, "Cancel", tint = Color.Red)
                        }
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center // Centrează elementele orizontal
                ) {
                    Text(
                        text = stone.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center, // Opțional: Dacă vrei textul descrierii centrat
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}