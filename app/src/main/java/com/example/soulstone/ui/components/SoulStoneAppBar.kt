package com.example.soulstone.screens.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.R
import com.example.soulstone.screens.navigation.AppScreen
import com.example.soulstone.ui.components.AppBarViewModel
import com.example.soulstone.ui.theme.SoulStoneTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoulStoneTopBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToSettings: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateChinese: () -> Unit,
    onNavigateStoneUses: () -> Unit,
    onNavigateChakras: () -> Unit,
    viewModel: AppBarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
    var currentTime by remember {
        mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
    }
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            delay(1000L)
        }
    }

    // State for language dropdown
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
                // Home Icon
                IconButton(
                    onClick = {
                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
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
                        onClick = {}
                    )
                }

                Spacer(Modifier.width(20.dp))

                // Language Dropdown
                Box {
                    CustomDropdownButton(
                        text = uiState.selectedLanguage,
                        onClick = { languageExpanded = true }
                    )
                    DropdownMenu(
                        expanded = languageExpanded,
                        onDismissRequest = { languageExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Spanish") },
                            onClick = {
                                viewModel.onLanguageSelected("Spanish")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("English") },
                            onClick = {
                                viewModel.onLanguageSelected("English")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("French") },
                            onClick = {
                                viewModel.onLanguageSelected("French")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("Italian") },
                            onClick = {
                                viewModel.onLanguageSelected("Italian")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("German") },
                            onClick = {
                                viewModel.onLanguageSelected("German")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("Polish") },
                            onClick = {
                                viewModel.onLanguageSelected("Polish")
                                languageExpanded = false
                            })
                        DropdownMenuItem(
                            text = { Text("Russian") },
                            onClick = {
                                viewModel.onLanguageSelected("Russian")
                                languageExpanded = false
                            })
                    }
                }

                Spacer(Modifier.width(20.dp))

                Image(
                    painter = painterResource(R.drawable.admin_menu),
                    contentDescription = "Admin menu",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            onNavigateToSettings()
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientButton(
                    text = "Horoscope Monthly Birthstones",
                    onClick = onNavigateHome,
//                    onClick = {
//                        navController.navigate(AppScreen.Home.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
                    isSelected = currentRoute == AppScreen.Home.route // Highlight if active
                )

                GradientButton(
                    text = "Chinese Annual Birthstones",
                    onClick = onNavigateChinese,
//                    onClick = {
//                        navController.navigate(AppScreen.ChineseBirthstones.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
                    isSelected = currentRoute == AppScreen.ChineseBirthstones.route
                )

                GradientButton(
                    text = "Stones Uses and Properties",
                    onClick = onNavigateStoneUses,
//                    onClick = {
//                        navController.navigate(AppScreen.StoneUses.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
                    isSelected = currentRoute == AppScreen.StoneUses.route
                )

                GradientButton(
                    text = "Seven Chakras Stones",
                    onClick = onNavigateChakras,
//                    onClick = {
//                        navController.navigate(AppScreen.SevenChakraStones.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    name = "24 inch Full HD (1920x1080)",
    widthDp = 1920 / 3,
    heightDp = 1080 / 3,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PreviewSoulStoneTopBar() {
    SoulStoneTheme {
        SoulStoneTopBar(navController = rememberNavController(), onNavigateToSettings = {})
    }
}