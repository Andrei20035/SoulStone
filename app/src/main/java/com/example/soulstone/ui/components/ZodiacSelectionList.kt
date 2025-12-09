package com.example.soulstone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soulstone.data.pojos.ZodiacSignListItem

@Composable
fun ZodiacSignsList(
    signs: List<ZodiacSignListItem>,
    onSignClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(signs) { sign ->
            ZodiacSignItem(
                sign = sign,
                onClick = { onSignClick(sign.keyName) }
            )
        }
    }
}

@Composable
fun ZodiacSignItem(
    sign: ZodiacSignListItem,
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
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Box(modifier = Modifier.size(32.dp)) {
                ZodiacImage(sign.imageName, sign.imageName, modifier = Modifier.fillMaxHeight())
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = sign.signName,
                fontSize = 28.sp,
                color = Color(0xFF2B4F84)
            )
        }
    }
}