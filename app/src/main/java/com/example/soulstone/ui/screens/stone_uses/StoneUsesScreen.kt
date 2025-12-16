package com.example.soulstone.ui.screens.stone_uses

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import com.example.soulstone.R
import com.example.soulstone.data.pojos.TranslatedBenefit
import com.example.soulstone.ui.components.SocialMediaFooter
import com.example.soulstone.ui.components.UniversalImage
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.BenefitUiItem
import com.example.soulstone.ui.navigation.AppScreen
import kotlinx.coroutines.launch

@Composable
fun StoneUsesScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: StoneUsesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateToBenefit -> {
                    navController.navigate(
                        AppScreen.StoneForX.createRoute(event.benefitId)
                    )
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Ensure padding is applied
                .padding(start = 54.dp, end = 54.dp, bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.stone_uses_and_properties),
                fontSize = 80.sp,
                lineHeight = 90.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(1000.dp)
                    .height(400.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color(0xFF2B4F84)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 68.dp, end = 68.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(uiState.benefits) { benefit ->
                            BenefitItem(
                                benefit = benefit,
                                onBenefitClicked = { viewModel.onBenefitClicked(benefit.id) }
                            )
                        }
                    }
                }
            }
            SocialMediaFooter()
        }
    }
}

@Composable
fun BenefitItem(
    benefit: BenefitUiItem,
    onBenefitClicked: () -> Unit
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
            .clickable(onClick = onBenefitClicked)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UniversalImage(
            imageResId = benefit.imageResId,
            imageFileName = benefit.imageFileName,
            contentDescription = benefit.name,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(110.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = benefit.name,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 30.sp,
            color = Color.Black
        )
    }
}