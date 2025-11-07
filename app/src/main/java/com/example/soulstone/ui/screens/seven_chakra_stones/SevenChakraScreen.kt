package com.example.soulstone.ui.screens.seven_chakra_stones

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
import com.example.soulstone.domain.model.Chakra
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.ui.screens.horoscope_monthly_birthstones.ZodiacViewModel
import com.example.soulstone.util.ChakraEnum
import com.example.soulstone.util.ZodiacSign
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


private data class ChakraCircle(
    val chakra: ChakraEnum,
    val centerX: Float,
    val centerY: Float,
    val radius: Float
)

// Helper function to convert degrees to radians for trigonometry
private fun Float.toRadians() = this * (Math.PI / 180f).toFloat()

@Composable
fun SevenChakraScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: SevenChakraViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                is UiEvent.NavigateToChakraDetails -> {
                    navController.navigate(
                        AppScreen.ChakraDetails.createRoute(event.chakraId)
                    )
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
                text = "Seven Chakra Stones Energetic stones for good fortune according to the seven chakras",
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
            ClickableChakraWheel(
                onChakraClick = {
                        clickedChakra -> viewModel.onChakraClicked(clickedChakra)
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
fun ClickableChakraWheel(
    modifier: Modifier = Modifier,
    onChakraClick: (ChakraEnum) -> Unit // Changed to return your Chakra enum
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Keep it a square
        contentAlignment = Alignment.Center
    ) {
        val imageSizePx = constraints.maxWidth.toFloat()
        val boxCenterX = imageSizePx / 2f
        val boxCenterY = imageSizePx / 2f

        // --- Define the 7 Chakra Circles ---

        // This is the radius of the chakra "buttons" themselves
        // You may need to adjust this value!
        val chakraButtonRadius = imageSizePx * 0.12f // 12% of the total width

        // This is the radius of the large circle that the 6 outer chakras sit on
        // You may need to adjust this value!
        val outerRingRadius = imageSizePx * 0.37f

        // Create a list of all 7 chakra circles with their calculated positions
        val chakraCircles = listOf(
            // 1. Crown (Center)
            ChakraCircle(ChakraEnum.CROWN, boxCenterX, boxCenterY, chakraButtonRadius),

            // 2. Root (Bottom) - 90 degrees
            ChakraCircle(
                chakra = ChakraEnum.ROOT,
                centerX = boxCenterX + cos(90f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(90f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            ),

            // 3. Sacral (Bottom-Right) - 30 degrees
            ChakraCircle(
                chakra = ChakraEnum.SACRAL,
                centerX = boxCenterX + cos(30f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(30f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            ),

            // 4. Solar Plexus (Bottom-Left) - 150 degrees
            ChakraCircle(
                chakra = ChakraEnum.SOLAR_PLEXUS,
                centerX = boxCenterX + cos(150f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(150f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            ),

            // 5. Heart (Top-Left) - 210 degrees
            ChakraCircle(
                chakra = ChakraEnum.HEART,
                centerX = boxCenterX + cos(210f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(210f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            ),

            // 6. Third Eye (Top) - 270 degrees
            ChakraCircle(
                chakra = ChakraEnum.THIRD_EYE,
                centerX = boxCenterX + cos(270f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(270f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            ),

            // 7. Throat (Top-Right) - 330 degrees
            ChakraCircle(
                chakra = ChakraEnum.THROAT,
                centerX = boxCenterX + cos(330f.toRadians()) * outerRingRadius,
                centerY = boxCenterY + sin(330f.toRadians()) * outerRingRadius,
                radius = chakraButtonRadius
            )
        )

        Image(
            // TODO: Make sure your new chakra image is named 'chakra_wheel'
            // and is in the res/drawable folder
            painter = painterResource(id = R.drawable.chakras),
            contentDescription = "Chakra Wheel",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val clickX = offset.x
                        val clickY = offset.y

                        // Find the first chakra circle that the user tapped on
                        val clickedChakra = chakraCircles.find { circle ->
                            // Calculate distance from click to the circle's center
                            val distance = sqrt(
                                (clickX - circle.centerX).pow(2) +
                                        (clickY - circle.centerY).pow(2)
                            )
                            // Check if the distance is within the circle's radius
                            distance <= circle.radius
                        }

                        if (clickedChakra != null) {
                            Log.d("CHAKRA_WHEEL", "Clicked on: ${clickedChakra.chakra.name}")
                            onChakraClick(clickedChakra.chakra)
                        } else {
                            Log.d("CHAKRA_WHEEL", "Clicked on empty space.")
                        }
                    }
                }
        )
    }
}