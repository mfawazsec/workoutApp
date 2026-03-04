package com.workoutapp.data.db

import androidx.room.*
import com.workoutapp.data.entity.DailyTrackingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTrackingDao {
    @Query("SELECT * FROM daily_tracking WHERE date = :date LIMIT 1")
    fun getByDate(date: String): Flow<DailyTrackingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DailyTrackingEntity)

    @Query("SELECT * FROM daily_tracking ORDER BY date DESC LIMIT 90")
    fun getRecentHistory(): Flow<List<DailyTrackingEntity>>
}
