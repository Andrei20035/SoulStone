package com.example.soulstone.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                is UiEvent.NavigateHome -> {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
                is UiEvent.NavigateChineseBirthstones -> {
                    navController.navigate(AppScreen.ChineseBirthstones.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
                is UiEvent.NavigateStoneUses -> {
                    navController.navigate(AppScreen.StoneUses.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
                is UiEvent.NavigateSevenChakras -> {
                    navController.navigate(AppScreen.SevenChakraStones.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
                is UiEvent.NavigateAdmin -> {
                    onNavigateToAdmin()
                }
                is UiEvent.NavigateGemstoneIndex -> {
                    navController.navigate(AppScreen.GemstoneIndex.route)
                }
                else -> Unit
            }
        }
    }

    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
    var currentTime by remember {
        mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
    }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            delay(1000L)
        }
    }

    var languageExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
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
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
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

                Box {
                    CustomDropdownButton(
                        text = "Gemstone Index",
                        onClick =  { viewModel.onGemstoneIndexClicked() }
                    )
                }

                Spacer(Modifier.width(20.dp))

                Box {
                    CustomDropdownButton(
                        text = LocalLanguage.current.name,
                        onClick = { languageExpanded = true }
                    )
                    DropdownMenu(
                        expanded = languageExpanded,
                        onDismissRequest = { languageExpanded = false }
                    ) {
                        LanguageCode.entries.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.name.lowercase().capitalize()) },
                                onClick = {
                                    viewModel.onLanguageSelected(lang)
                                    languageExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.width(20.dp))

                Icon(
                    painter = painterResource(R.drawable.admin_menu),
                    contentDescription = "Admin menu",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { viewModel.onAdminClicked() },
                    tint = Color.Unspecified
                )
            }

            Spacer(Modifier.height(38.dp))

            // Navigation Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientButton(
                    text = "Horoscope Monthly Birthstones",
                    onClick = { viewModel.onHomeClicked() },
                    isSelected = currentRoute == AppScreen.Home.route
                )

                GradientButton(
                    text = "Chinese Annual Birthstones",
                    onClick = { viewModel.onChineseClicked() },
                    isSelected = currentRoute == AppScreen.ChineseBirthstones.route
                )

                GradientButton(
                    text = "Stones Uses and Properties",
                    onClick = { viewModel.onStoneUsesClicked() },
                    isSelected = currentRoute == AppScreen.StoneUses.route
                )

                GradientButton(
                    text = "Seven Chakras Stones",
                    onClick = { viewModel.onChakrasClicked() },
                    isSelected = currentRoute == AppScreen.SevenChakraStones.route
                )
            }
        }
    }
}

@Composable

fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val gradientColors = listOf(Color(0xFF502DF6), Color(0xFFB927FC))
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(80.dp)
            .width(300.dp)
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
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 30.dp)
        )
    }
}

@Composable

fun CustomDropdownButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(4.dp, Color.Black, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.width(24.dp))
        Icon(
            painter = painterResource(R.drawable.dropdown),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(32.dp)
        )
    }
}