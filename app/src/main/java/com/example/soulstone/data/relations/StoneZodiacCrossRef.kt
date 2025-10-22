package com.example.soulstone.data.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["stoneId", "zodiacSignId"],
    indices = [Index(value = ["zodiacSignId"])]
)
data class StoneZodiacCrossRef(
    val stoneId: Int,
    val zodiacSignId: Int
)
