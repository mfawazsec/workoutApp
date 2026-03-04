package com.workoutapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.workoutapp.data.entity.ProgressionLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressionLogDao {
    @Insert
    suspend fun insertLog(log: ProgressionLogEntity)

    @Query("SELECT * FROM progression_log WHERE exerciseId = :exerciseId ORDER BY recordedAt DESC")
    fun getLogsForExercise(exerciseId: String): Flow<List<ProgressionLogEntity>>

    @Query("SELECT * FROM progression_log WHERE sessionId = :sessionId")
    suspend fun getLogsForSession(sessionId: String): List<ProgressionLogEntity>
}
