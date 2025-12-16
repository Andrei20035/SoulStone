package com.example.soulstone.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soulstone.LocalLanguage
import com.example.soulstone.R
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.util.LanguageCode
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoulStoneTopBar(
    modifier: Modifier = Modifier,
    onNavigateToAdmin: () -> Unit,
    navController: NavController,
    viewModel: AppBarViewModel = hiltViewModel(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateHome -> navController.navigateSingleTop(AppScreen.Home.route)
                is UiEvent.NavigateChineseBirthstones -> navController.navigateSingleTop(AppScreen.ChineseBirthstones.route)
                is UiEvent.NavigateStoneUses -> navController.navigateSingleTop(AppScreen.StoneUses.route)
                is UiEvent.NavigateSevenChakras -> navController.navigateSingleTop(AppScreen.SevenChakraStones.route)
                is UiEvent.NavigateAdmin -> onNavigateToAdmin()
                is UiEvent.NavigateGemstoneIndex -> navController.navigate(AppScreen.GemstoneIndex.route)
                else -> Unit
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth < 840.dp) {
            MobileTopBar(
                viewModel = viewModel,
                currentRoute = currentRoute,
                onNavigateToAdmin = { viewModel.onAdminClicked() } // Use VM event for consistency
            )
        } else {
            DesktopTopBar(
                viewModel = viewModel,
                currentRoute = currentRoute
            )
        }
    }
}

// --- DESKTOP LAYOUT (Your Original Design) ---
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DesktopTopBar(
    viewModel: AppBarViewModel,
    currentRoute: String?
) {
    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
    var currentTime by remember { mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            delay(1000L)
        }
    }

    var languageExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 30.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 54.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.onHomeClicked() },
                    modifier = Modifier.size(54.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home), // Ensure this resource exists
                        contentDescription = "Home",
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(15.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentDate,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 29.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(25.dp))
                    Text(
                        text = currentTime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 29.sp,
                        color = Color.Black
                    )
                }

                Spacer(Modifier.weight(1f))

                StyledTextButton(
                    text = stringResource(R.string.gemstone_index),
                    onClick = { viewModel.onGemstoneIndexClicked() }
                )

                Spacer(Modifier.width(20.dp))

                LanguageDropdown(
                    currentLanguage = LocalLanguage.current,
                    onLanguageSelected = { newLang ->
                        viewModel.onLanguageSelected(newLang)
                    }
                )

                Spacer(Modifier.width(20.dp))

                Icon(
                    painter = painterResource(R.drawable.admin_menu),
                    contentDescription = "Admin menu",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 2.dp)
                        .clickable { viewModel.onAdminClicked() },
                    tint = Color.Unspecified
                )
            }

            Spacer(Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientButton(
                    text = stringResource(R.string.horoscope_monthly_birthstones),
                    onClick = { viewModel.onHomeClicked() },
                    isSelected = currentRoute == AppScreen.Home.route,
                    width = 320.dp,
                    height = 100.dp,
                    fontSize = 20.sp
                )

                GradientButton(
                    text = stringResource(R.string.chinese_annual_birthstones),
                    onClick = { viewModel.onChineseClicked() },
                    isSelected = currentRoute == AppScreen.ChineseBirthstones.route,
                    width = 320.dp,
                    height = 100.dp,
                    fontSize = 20.sp
                )

                GradientButton(
                    text = stringResource(R.string.stones_uses_and_properties),
                    onClick = { viewModel.onStoneUsesClicked() },
                    isSelected = currentRoute == AppScreen.StoneUses.route,
                    width = 320.dp,
                    height = 100.dp,
                    fontSize = 20.sp
                )

                GradientButton(
                    text = stringResource(R.string.seven_chakras_stones),
                    onClick = { viewModel.onChakrasClicked() },
                    isSelected = currentRoute == AppScreen.SevenChakraStones.route,
                    width = 320.dp,
                    height = 100.dp,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MobileTopBar(
    viewModel: AppBarViewModel,
    currentRoute: String?,
    onNavigateToAdmin: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var languageDialogShow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Home Icon
            IconButton(
                onClick = { viewModel.onHomeClicked() },
                modifier = Modifier.size(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    contentScale = ContentScale.Fit
                )
            }


            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
                    DropdownMenuItem(
                        text = { Text("Date: $currentDate", fontSize = 14.sp, color = Color.Gray) },
                        onClick = { },
                        enabled = false
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.gemstone_index)) },
                        onClick = {
                            viewModel.onGemstoneIndexClicked()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Language: ${LocalLanguage.current.name}") },
                        onClick = {
                            languageDialogShow = true
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.admin_panel)) },
                        onClick = {
                            onNavigateToAdmin()
                            menuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                GradientButton(
                    text = stringResource(R.string.horoscope),
                    onClick = { viewModel.onHomeClicked() },
                    isSelected = currentRoute == AppScreen.Home.route,
                    width = 160.dp,   // Smaller width for mobile
                    height = 60.dp,   // Smaller height
                    fontSize = 16.sp  // Smaller font
                )
            }
            item {
                GradientButton(
                    text = stringResource(R.string.chinese_zodiac),
                    onClick = { viewModel.onChineseClicked() },
                    isSelected = currentRoute == AppScreen.ChineseBirthstones.route,
                    width = 160.dp,
                    height = 60.dp,
                    fontSize = 16.sp
                )
            }
            item {
                GradientButton(
                    text = stringResource(R.string.stone_uses),
                    onClick = { viewModel.onStoneUsesClicked() },
                    isSelected = currentRoute == AppScreen.StoneUses.route,
                    width = 160.dp,
                    height = 60.dp,
                    fontSize = 16.sp
                )
            }
            item {
                GradientButton(
                    text = stringResource(R.string.chakras),
                    onClick = { viewModel.onChakrasClicked() },
                    isSelected = currentRoute == AppScreen.SevenChakraStones.route,
                    width = 160.dp,
                    height = 60.dp,
                    fontSize = 16.sp
                )
            }
        }
    }

    if (languageDialogShow) {
        AlertDialog(
            onDismissRequest = { languageDialogShow = false },
            title = { Text(stringResource(R.string.select_language)) },
            text = {
                Column {
                    LanguageCode.entries.forEach { lang ->
                        TextButton(
                            onClick = {
                                viewModel.onLanguageSelected(lang)
                                languageDialogShow = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = lang.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { languageDialogShow = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}


@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    width: Dp = 300.dp,
    height: Dp = 100.dp,
    fontSize: TextUnit = 24.sp
) {
    val gradientColors = listOf(Color(0xFF502DF6), Color(0xFFB927FC))
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(50.dp)
            )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = (fontSize.value + 4).sp, // Adapt line height
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

//@Composable
//fun CustomDropdownButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .clip(RoundedCornerShape(50.dp))
//            .background(Color.White)
//            .border(4.dp, Color.Black, RoundedCornerShape(50.dp))
//            .clickable(onClick = onClick)
//            .padding(horizontal = 18.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = text,
//            color = Color.Black,
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Medium
//        )
//        Spacer(Modifier.width(24.dp))
//        Icon(
//            painter = painterResource(R.drawable.dropdown),
//            contentDescription = null,
//            tint = Color.Black,
//            modifier = Modifier.size(32.dp)
//        )
//    }
//}

fun NavController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = false
    }
}

private val BUTTON_HEIGHT = 50.dp

@Composable
fun StyledTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(BUTTON_HEIGHT)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(4.dp, Color.Black, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp), // Matching padding
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun LanguageDropdown(
    currentLanguage: LanguageCode,
    onLanguageSelected: (LanguageCode) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var buttonWidth by remember { mutableStateOf(0) }

    // Animation for the arrow icon
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Arrow Rotation"
    )

    Box(modifier = modifier) {
        // 1. The Custom Button (Now shows Flag)
        CustomDropdownButton(
            flagResId = currentLanguage.flagResId, // Pass the flag ID
            onClick = { expanded = true },
            rotation = rotationState,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                buttonWidth = coordinates.size.width
            }
        )

        // 2. The Menu
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { buttonWidth.toDp() })
                    .background(Color.White)
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
                offset = androidx.compose.ui.unit.DpOffset(0.dp, 8.dp)
            ) {
                LanguageCode.entries.forEach { lang ->
                    val isSelected = lang == currentLanguage

                    DropdownMenuItem(
                        text = {
                            // In the menu, show Flag + Text for clarity
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = lang.flagResId),
                                    contentDescription = lang.name,
                                    modifier = Modifier.size(28.dp), // Smaller size for menu
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = lang.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 18.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.Black
                                )
                            }
                        },
                        onClick = {
                            onLanguageSelected(lang)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (isSelected) Color(0xFFEEEEEE) else Color.Transparent
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDropdownButton(
    flagResId: Int,
    onClick: () -> Unit,
    rotation: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(BUTTON_HEIGHT)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(4.dp, Color.Black, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp), // Adjusted padding slightly for flags
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Replace Text with Image
        Image(
            painter = painterResource(id = flagResId),
            contentDescription = "Selected Language Flag",
            // A fixed size ensures the button height stays consistent
            // regardless of the flag image's aspect ratio.
            modifier = Modifier.size(42.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.width(20.dp))

        Icon(
            painter = painterResource(R.drawable.dropdown), // Ensure you have this asset
            contentDescription = "Dropdown Arrow",
            tint = Color.Black,
            modifier = Modifier
                .size(32.dp)
                .rotate(rotation)
        )
    }
}