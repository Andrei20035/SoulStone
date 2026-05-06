package com.example.soulstone.ui.screens.stones_for_benefit

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.soulstone.R
import com.example.soulstone.data.pojos.TranslatedBenefit
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.ui.components.SocialMediaFooter
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.navigation.AppScreen
import kotlinx.coroutines.launch

@Composable
fun StonesForXScreen(
    viewModel: StonesForXViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                        popUpTo(navController.currentBackStackEntry?.destination?.route ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }
                is UiEvent.NavigateToStoneDetail -> {
                    navController.navigate(
                        AppScreen.StoneDetails.createRoute(event.stoneId)
                    )
                }
                is UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }
    StoneForBenefitScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBenefitClicked = viewModel::onBenefitClicked,
        onStoneClicked = viewModel::onStoneClicked,
        onBackClicked = viewModel::onBackClicked
    )


}

@Composable
fun StoneForBenefitScreenContent(
    uiState: StonesForXUiState,
    snackbarHostState: SnackbarHostState,
    onBenefitClicked: (Int) -> Unit,
    onStoneClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    .height(200.dp)
            ) {
                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B4F84),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(
                    text = stringResource(R.string.stone_uses_and_properties),
                    fontSize = 80.sp,
                    lineHeight = 90.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2B4F84),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(1000.dp)
                        .height(200.dp)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }

            Text(
                text = if (uiState.isBenefitsLoading) stringResource(R.string.loading) else uiState.benefitName,
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
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isStonesLoading) {
                            CircularProgressIndicator()
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2), // 2 columns for stones
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(0.dp),
                                horizontalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                items(uiState.stones) { stone ->
                                    StoneItem(
                                        stone = stone,
                                        onStoneClicked = { onStoneClicked(stone.id) }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (uiState.isBenefitsLoading) {
                            CircularProgressIndicator()
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(start = 50.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.allBenefits) { benefit ->
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


@Composable
fun StoneItem(
    stone: TranslatedStone,
    onStoneClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(220.dp)
            .border(
                width = 0.5.dp,
                color = Color.Black,
                shape = RectangleShape
            )
            .clip(RectangleShape)
            .clickable(onClick = onStoneClicked)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        val imageName = stone.imageUri

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
                contentDescription = stone.translatedName,
                    contentScale = ContentScale.Fit,
                modifier = Modifier.size(110.dp)
            )
        } else {
            Box(modifier = Modifier.background(Color.LightGray))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stone.translatedName,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 30.sp,
            color = Color.Black
        )
    }
}

@Composable
fun BenefitChip(
    benefit: TranslatedBenefit,
    onClick: () -> Unit
) {
    Text(
        text = benefit.translatedName,
        fontSize = 24.sp,
        color = Color(0xFF2B4F84),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFF2B4F84),
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    )
}