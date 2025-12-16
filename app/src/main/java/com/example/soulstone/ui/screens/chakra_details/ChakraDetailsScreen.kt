import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.soulstone.R
import com.example.soulstone.data.pojos.ChakraListItem
import com.example.soulstone.data.pojos.ZodiacSignListItem
import com.example.soulstone.ui.components.SocialMediaFooter
import com.example.soulstone.ui.components.UniversalImage
import com.example.soulstone.ui.components.ZodiacCenterData
import com.example.soulstone.ui.components.ZodiacSignsList
import com.example.soulstone.ui.components.ZodiacStoneWheelViewer
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.ChakraListUiItem
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.ui.screens.chakra_details.ChakraDetailsUiState
import com.example.soulstone.ui.screens.chakra_details.ChakraDetailsViewModel
import com.example.soulstone.util.DescriptionTextStyle
import com.example.soulstone.util.simpleVerticalScrollbar
import kotlinx.coroutines.launch

@Composable
fun ChakraDetailsScreen(
    viewModel: ChakraDetailsViewModel = hiltViewModel(),
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
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is UiEvent.NavigateToStoneDetail -> {
                    navController.navigate(
                        AppScreen.StoneDetails.createRoute(event.stoneId)
                    )
                }
                is UiEvent.NavigateToChakraDetails -> {
                    navController.navigate(
                        AppScreen.ChakraDetails.createRoute(event.keyName)
                    ) {
                        popUpTo(navController.currentBackStackEntry?.destination?.route ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }

                else -> {}
            }
        }
    }

    ChakraDetails(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onStoneClick = { stoneId ->
            viewModel.onStoneClicked(stoneId)
        },
        onChakraClicked = { keyName ->
            viewModel.onChakraClicked(keyName)
        }
    )

}

@Composable
fun ChakraDetails(
    uiState: ChakraDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onStoneClick: (Int) -> Unit,
    onChakraClicked: (String) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 54.dp, end = 54.dp, bottom = 54.dp)
//                .background(Color.Green)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.Gray)
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Color(0xFF2B4F84),
                            strokeWidth = 4.dp
                        )
                    }
                } else if (uiState.sign != null) {
                    val signWrapper = uiState.sign
                    val signData = signWrapper.data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp),
//                            .background(Color.Yellow),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.seven_chakra_stones),
                            fontSize = 45.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
//                                .background(Color.Red)
                                .wrapContentHeight(Alignment.CenterVertically),
                            color = Color(0xFF2B4F84)
                        )
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            ) {
                                UniversalImage(
                                    imageResId = signWrapper.imageResId,
                                    imageFileName = signData.imageName,
                                    contentDescription = signData.name,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Column(
                                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = signData.name,
                                    fontSize = 70.sp,
                                    lineHeight = 48.sp,
                                    color = Color(0xFF2B4F84),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    signData.sanskritName.replaceFirstChar { it.uppercase() },
                                    color = Color(0xFF2B4F84),
                                    fontSize = 40.sp
                                )
                                Text(
                                    stringResource(R.string.planet) + signData.rulingPlanet,
                                    color = Color(0xFF2B4F84),
                                    fontSize = 40.sp
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.88f) // Takes up ~88% of the screen width
                                    .align(Alignment.CenterStart) // Aligned to the Left
                            ) {
                                ZodiacStoneWheelViewer(
                                    centerData = ZodiacCenterData(signData.imageName, signWrapper.imageResId),
                                    stonesList = uiState.associatedStones,
                                    backgroundImageRes = R.drawable.flower,
                                    onStoneClick = onStoneClick,
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.25f) // Takes ~25% width (creating the overlap)
                                    .align(Alignment.TopEnd) // "Not centered but up" -> Aligned Top Right
                                    .padding(top = 40.dp) // Push it down slightly from the very top edge
                            ) {
                                ChakraSignList(
                                    signs = uiState.allChakras,
                                    onSignClick = onChakraClicked
                                )
                            }
                        }
                        val descriptionScrollState = rememberScrollState()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                // Apply scrollbar to the OUTER Box
                                .simpleVerticalScrollbar(
                                    state = descriptionScrollState,
                                    width = 6.dp,
                                    color = Color(0xFF2B4F84).copy(alpha = 0.5f)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 12.dp) // Padding for the text content
                                    .verticalScroll(descriptionScrollState), // 3. Scroll applied ONCE here
                            ) {

                                Text(
                                    text = "${signData.name} (${signData.sanskritName.replaceFirstChar { it.uppercase() }})",
                                    style = DescriptionTextStyle()
                                )

                                Text(
                                    text = signData.description,
                                    style = DescriptionTextStyle()
                                )

                                Spacer(Modifier.height(8.dp))

                                // 4. Use a Helper Function to remove 50 lines of duplicate code
                                SignInfoSection(stringResource(R.string.location), signData.location)
                                SignInfoSection(stringResource(R.string.stone_colors), signData.stoneColors)
                                SignInfoSection(stringResource(R.string.healing_qualities), signData.healingQualities)
                                SignInfoSection(stringResource(R.string.stones), signData.stones)
                                SignInfoSection(stringResource(R.string.body_placement), signData.bodyPlacement)
                                SignInfoSection(stringResource(R.string.house_placement), signData.housePlacement)
                                SignInfoSection(stringResource(R.string.herbs, signData.name), signData.herbs)
                                SignInfoSection(stringResource(R.string.essential_oils), signData.essentialOils)

                                Spacer(Modifier.height(24.dp))
                            }
                        }
                        SocialMediaFooter()
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInfoSection(title: String, content: String) {
    if (content.isNotEmpty()) {
        Column {
            Text(
                text = "$title: $content",
                style = DescriptionTextStyle()
            )
        }
    }
}


@Composable
fun ChakraSignList(
    signs: List<ChakraListUiItem>,
    onSignClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        signs.forEach { sign ->
            ChakraPill(
                sign = sign,
                onClick = { onSignClick(sign.sanskritName) }
            )
        }
    }
}

@Composable
fun ChakraPill(
    sign: ChakraListUiItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        border = BorderStroke(3.dp, Color.LightGray),
        color = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Box(modifier = Modifier.size(32.dp)) {
                UniversalImage(
                    imageResId = sign.imageResId,
                    imageFileName = sign.imageFileName,
                    contentDescription = sign.chakraName,
                    modifier = Modifier.fillMaxHeight()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = sign.chakraName,
                fontSize = 28.sp,
                maxLines = 1,
                softWrap = false,
                color = Color(0xFF2B4F84),
                modifier = Modifier.basicMarquee(
                    // 1. Wait 2 seconds before starting to scroll (User sees the start clearly)
                    initialDelayMillis = 2000,

                    // 2. Wait 2 seconds at the end before restarting (User sees the end clearly)
                    repeatDelayMillis = 2000,

                    // Optional: Slow down the scroll speed (default is usually around 30.dp)
                    velocity = 30.dp
                )
            )
        }
    }
}











