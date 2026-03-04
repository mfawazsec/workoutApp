package com.workoutapp.data.repository

import com.workoutapp.data.db.ProgressionLogDao
import com.workoutapp.data.db.UserExerciseStateDao
import com.workoutapp.data.db.WorkoutSessionDao
import com.workoutapp.data.entity.ProgressionLogEntity
import com.workoutapp.data.entity.UserExerciseStateEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import com.workoutapp.data.util.RepMaxCalculator
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    private val sessionDao: WorkoutSessionDao,
    private val stateDao: UserExerciseStateDao,
    private val logDao: ProgressionLogDao
) {
    // ── Sessions ────────────────────────────────────────────────────────────

    fun getRecentSessions(): Flow<List<WorkoutSessionEntity>> =
        sessionDao.getRecentSessions()

    suspend fun saveSession(session: WorkoutSessionEntity) =
        sessionDao.insertSession(session)

    suspend fun completeSession(sessionId: String) =
        sessionDao.markCompleted(sessionId, System.currentTimeMillis())

    suspend fun getSessionsSince(fromEpochDay: Long): List<WorkoutSessionEntity> =
        sessionDao.getSessionsSince(fromEpochDay)

    // ── Per-exercise state (weight / reps) ───────────────────────────────────

    suspend fun getExerciseState(exerciseId: String): UserExerciseStateEntity? =
        stateDao.getState(exerciseId)

    fun getAllExerciseStates(): Flow<List<UserExerciseStateEntity>> =
        stateDao.getAllStates()

    suspend fun saveExerciseState(
        exerciseId: String,
        weightKg: Float,
        reps: Int,
        sets: Int
    ) {
        val oneRepMax = RepMaxCalculator.estimateOneRepMax(weightKg, reps)
        stateDao.upsertState(
            UserExerciseStateEntity(
                exerciseId = exerciseId,
                currentWeightKg = weightKg,
                currentReps = reps,
                currentSets = sets,
                estimatedOneRepMaxKg = oneRepMax,
                lastUpdatedEpoch = System.currentTimeMillis()
            )
        )
    }

    // ── Progression logs ─────────────────────────────────────────────────────

    suspend fun logProgression(log: ProgressionLogEntity) =
        logDao.insertLog(log)

    fun getLogsForExercise(exerciseId: String): Flow<List<ProgressionLogEntity>> =
        logDao.getLogsForExercise(exerciseId)
}
