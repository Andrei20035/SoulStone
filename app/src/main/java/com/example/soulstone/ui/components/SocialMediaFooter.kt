package com.example.soulstone.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soulstone.R

@Composable
fun SocialMediaFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Healing crystals, protection, and lucky stones according to the Horoscope Signs, Chinese Zodiac, and the Seven Chakras",
            fontSize = 50.sp,
            lineHeight = 60.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(300.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            color = Color.Black
        )
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