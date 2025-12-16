package com.example.soulstone.ui.screens.horoscope_monthly_birthstones

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.R
import com.example.soulstone.ui.components.SocialMediaFooter
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.util.ZodiacSignEnum
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: ZodiacViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateToZodiacSign -> {
                    navController.navigate(
                        AppScreen.HoroscopeSignDetails.createRoute(event.keyName)
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
                .padding(horizontal = 54.dp, vertical = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.energy_stones_of_good_fortune),
                fontSize = 80.sp,
                lineHeight = 90.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 32.dp),
                color = Color(0xFF2B4F84)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ClickableZodiacWheel(
                    onSignClick = { clickedSign ->
                        viewModel.onSignClicked(clickedSign)
                    }
                )
            }
            SocialMediaFooter()
        }
    }
}


@Composable
fun ClickableZodiacWheel(
    modifier: Modifier = Modifier,
    onSignClick: (ZodiacSignEnum) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val imageSizePx = constraints.maxWidth.toFloat()
        val centerX = imageSizePx / 2f
        val centerY = imageSizePx / 2f
        Log.d("CENTER X", centerX.toString())
        Log.d("CENTER Y", centerY.toString())

        val innerRadius = imageSizePx * 0.22f
        val outerRadius = imageSizePx * 0.5f
        Log.d("INNER RADIUS", innerRadius.toString())
        Log.d("OUTER RADIUS", outerRadius.toString())

        Image(
            painter = painterResource(id = R.drawable.zodiac),
            contentDescription = "Zodiac Wheel",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val x = offset.x - centerX
                        val y = offset.y - centerY

                        val radius = sqrt(x * x + y * y)

                        if (radius in innerRadius..outerRadius) {
                            val angleRad = atan2(y, x)

                            var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
                            angleDeg = (angleDeg + 360) % 360


                            Log.d("UNGHI", angleDeg.toString())
                            val clickedSign: ZodiacSignEnum = when (angleDeg) {
                                in 0f..30f -> ZodiacSignEnum.TAURUS
                                in 30f..60f -> ZodiacSignEnum.GEMINI
                                in 60f..90f -> ZodiacSignEnum.CANCER
                                in 90f..120f -> ZodiacSignEnum.LEO
                                in 120f..150f -> ZodiacSignEnum.VIRGO
                                in 150f..180f -> ZodiacSignEnum.LIBRA
                                in 180f..210f -> ZodiacSignEnum.SCORPIO
                                in 210f..240f -> ZodiacSignEnum.SAGITTARIUS
                                in 240f..270f -> ZodiacSignEnum.CAPRICORN
                                in 270f..300f -> ZodiacSignEnum.AQUARIUS
                                in 300f..330f -> ZodiacSignEnum.PISCES
                                else -> ZodiacSignEnum.ARIES
                            }
                            println("A APASAT PE: $clickedSign")
                            onSignClick(clickedSign)
                        } else {
                            println("Click în afara zonei active.")
                        }
                    }
                }
        )
    }
}