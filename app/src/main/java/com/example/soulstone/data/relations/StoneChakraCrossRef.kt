package com.example.soulstone.data.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["stoneId", "chakraId"],
    indices = [Index(value = ["chakraId"])]
)
data class StoneChakraCrossRef(
    val stoneId: Int,
    val chakraId: Int
)
