package com.example.soulstone.ui.screens.stone_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.soulstone.R
import com.example.soulstone.ui.components.SocialMediaFooter
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.ui.screens.stones_for_benefit.BenefitChip
import kotlinx.coroutines.launch

@Composable
fun StoneDetailsScreen(
    viewModel: StoneDetailsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is UiEvent.NavigateToBenefit -> {
                    navController.navigate(
                        AppScreen.StoneForX.createRoute(event.benefitId)
                    ) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
                is UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }

    StoneDetailsContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBenefitClicked = { benefitId -> viewModel.onBenefitClicked(benefitId) },
        onBackClicked = { viewModel.onBackClicked() }
    )
}

@Composable
private fun StoneDetailsContent(
    uiState: StoneDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onBenefitClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        // snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Uncomment when ready
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 54.dp, end = 54.dp, bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Match the height you previously gave the Text
            ) {
                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Or your custom resource
                        contentDescription = "Back",
                        tint = Color(0xFF2B4F84),
                        modifier = Modifier.fillMaxSize() // Adjust icon visual size
                    )
                }

                // 2. Title Text (Aligned Center)
                Text(
                    text = "Stone Uses and Properties",
                    fontSize = 80.sp,
                    lineHeight = 90.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2B4F84),
                    modifier = Modifier
                        .align(Alignment.Center) // This keeps it perfectly centered in the Box
                        .width(1000.dp)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }

            // --- DYNAMIC SUBTITLE ---
            Text(
                text = uiState.stone?.translatedName ?: "Loading...",
                fontSize = 60.sp,
                lineHeight = 70.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color(0xFF2B4F84)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (uiState.isLoading && uiState.stone == null) {
                            CircularProgressIndicator()
                        } else {
                            Column(
                                horizontalAlignment = Alignment.Start,
                            ) {
                                val context = LocalContext.current
                                val imageName = uiState.stone?.imageUri

                                val imageResId = remember(imageName) {
                                    context.resources.getIdentifier(
                                        imageName,
                                        "drawable",
                                        context.packageName
                                    )
                                }

                                if (imageResId != 0) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(imageResId)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = imageName,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(400.dp)
                                    )
                                } else {
                                    Box(modifier = Modifier.background(Color.LightGray))
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = uiState.stone?.description ?: "No description available",
                                    fontSize = 30.sp,
                                    lineHeight = 36.sp,
                                    color = Color.Black,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (uiState.isLoading && uiState.benefits.isEmpty()) {
                            CircularProgressIndicator()
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(start = 50.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.benefits) { benefit ->
                                    BenefitChip(
                                        benefit = benefit,
                                        onClick = { onBenefitClicked(benefit.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            SocialMediaFooter()
        }
    }
}