package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tracking")
data class DailyTrackingEntity(
    @PrimaryKey val date: String,  // ISO date string: "2024-03-04"
    val waterGlasses: Int = 0,
    val creatineServings: Int = 0,  // each serving = 3g
    val multivitaminTaken: Int = 0  // 0 or 1
)
