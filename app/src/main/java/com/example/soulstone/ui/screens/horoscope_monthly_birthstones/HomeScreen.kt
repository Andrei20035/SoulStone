package com.example.soulstone.screens.horoscope_monthly_birthstones

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.R
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.ZodiacSignEnum
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.ui.screens.horoscope_monthly_birthstones.ZodiacViewModel
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: ZodiacViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val signNameToNavigate by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(signNameToNavigate) {
        if (signNameToNavigate != null) {
            navController.navigate(
                AppScreen.HoroscopeSignDetails.createRoute(signNameToNavigate!!)
            )
            viewModel.onNavigationHandled()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 54.dp, end = 54.dp, bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Energy stones of good fortune according to your birth date",
                fontSize = 80.sp,
                lineHeight = 90.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(1000.dp)
                    .height(500.dp)
//                    .background(Color.Yellow)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color(0xFF2B4F84)
            )
            ClickableZodiacWheel(
                onSignClick = {
                    clickedSign -> viewModel.onSignClicked(clickedSign)
                }
            )
            Text(
                text = "Healing crystals, protection, and lucky stones according to the Horoscope Signs, Chinese Zodiac, and the Seven Chakras",
                fontSize = 50.sp,
                lineHeight = 60.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(300.dp)
    //                .background(Color.Yellow)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color.Black
            )
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 120.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.height(80.dp)
                )
                Spacer(Modifier.width(15.dp))
                Image(
                    painter = painterResource(R.drawable.instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.height(85.dp)

                )
                Spacer(Modifier.width(15.dp))
                Text(
                    "Mandala.Art.Spain",
                    color = Color.Black,
                    fontSize = 60.sp
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "696121347",
                    color = Color.Black,
                    fontSize = 60.sp
                )
                Spacer(Modifier.width(15.dp))
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(85.dp)

                )
            }

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