package com.workoutapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.workoutapp.data.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions ORDER BY dateEpochDay DESC LIMIT 30")
    fun getRecentSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE dateEpochDay >= :fromEpochDay ORDER BY dateEpochDay DESC")
    suspend fun getSessionsSince(fromEpochDay: Long): List<WorkoutSessionEntity>

    @Query("UPDATE workout_sessions SET completedAt = :epochMillis WHERE sessionId = :sessionId")
    suspend fun markCompleted(sessionId: String, epochMillis: Long)
}
