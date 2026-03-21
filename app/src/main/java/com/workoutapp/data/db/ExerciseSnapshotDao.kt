package com.workoutapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSnapshotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshots(snapshots: List<ExerciseSnapshotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: ExerciseSnapshotEntity)

    @Query("SELECT * FROM exercise_snapshots WHERE sessionId = :sessionId ORDER BY id ASC")
    suspend fun getSnapshotsForSession(sessionId: String): List<ExerciseSnapshotEntity>

    @Query("SELECT * FROM exercise_snapshots WHERE sessionId = :sessionId ORDER BY id ASC")
    fun getSnapshotsForSessionFlow(sessionId: String): Flow<List<ExerciseSnapshotEntity>>

    @Query("UPDATE exercise_snapshots SET completedAtMs = :ms, sets = :sets, weightKg = :weightKg, reps = :reps WHERE sessionId = :sessionId AND exerciseId = :exerciseId")
    suspend fun markExerciseDone(sessionId: String, exerciseId: String, sets: Int, weightKg: Float, reps: Int, ms: Long)

    @Query("UPDATE exercise_snapshots SET setCompletionTimestamps = :timestamps WHERE sessionId = :sessionId AND exerciseId = :exerciseId")
    suspend fun updateSetTimestamps(sessionId: String, exerciseId: String, timestamps: String)

    @Query("UPDATE exercise_snapshots SET sets = :sets WHERE sessionId = :sessionId AND exerciseId = :exerciseId")
    suspend fun updateSets(sessionId: String, exerciseId: String, sets: Int)
}
