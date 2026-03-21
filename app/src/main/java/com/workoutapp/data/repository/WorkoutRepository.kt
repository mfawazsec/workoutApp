package com.workoutapp.data.repository

import com.workoutapp.data.db.ExerciseSnapshotDao
import com.workoutapp.data.db.ProgressionLogDao
import com.workoutapp.data.db.UserExerciseStateDao
import com.workoutapp.data.db.WorkoutSessionDao
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import com.workoutapp.data.entity.ProgressionLogEntity
import com.workoutapp.data.entity.UserExerciseStateEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import com.workoutapp.data.util.RepMaxCalculator
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    private val sessionDao: WorkoutSessionDao,
    private val stateDao: UserExerciseStateDao,
    private val logDao: ProgressionLogDao,
    private val snapshotDao: ExerciseSnapshotDao
) {
    // ── Sessions ────────────────────────────────────────────────────────────

    fun getRecentSessions(): Flow<List<WorkoutSessionEntity>> =
        sessionDao.getRecentSessions()

    fun getCompletedSessions(): Flow<List<WorkoutSessionEntity>> =
        sessionDao.getCompletedSessions()

    suspend fun saveSession(session: WorkoutSessionEntity) =
        sessionDao.insertSession(session)

    suspend fun completeSession(sessionId: String) =
        sessionDao.markCompleted(sessionId, System.currentTimeMillis())

    suspend fun getSessionsSince(fromEpochDay: Long): List<WorkoutSessionEntity> =
        sessionDao.getSessionsSince(fromEpochDay)

    /** Returns the most recent completed session for this exact muscle group combination. */
    suspend fun getLastCompletedSessionForDayKey(dayKey: String): WorkoutSessionEntity? =
        sessionDao.getLastCompletedSessionForDayKey(dayKey)

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

    // ── Exercise snapshots ───────────────────────────────────────────────────

    suspend fun saveExerciseSnapshots(snapshots: List<ExerciseSnapshotEntity>) =
        snapshotDao.insertSnapshots(snapshots)

    suspend fun getSnapshotsForSession(sessionId: String): List<ExerciseSnapshotEntity> =
        snapshotDao.getSnapshotsForSession(sessionId)

    fun getSnapshotsForSessionFlow(sessionId: String): Flow<List<ExerciseSnapshotEntity>> =
        snapshotDao.getSnapshotsForSessionFlow(sessionId)

    suspend fun markExerciseDone(sessionId: String, exerciseId: String, sets: Int, weightKg: Float, reps: Int) =
        snapshotDao.markExerciseDone(sessionId, exerciseId, sets, weightKg, reps, System.currentTimeMillis())

    suspend fun updateSetTimestamps(sessionId: String, exerciseId: String, timestamps: String) =
        snapshotDao.updateSetTimestamps(sessionId, exerciseId, timestamps)

    suspend fun updateSnapshotSets(sessionId: String, exerciseId: String, sets: Int) =
        snapshotDao.updateSets(sessionId, exerciseId, sets)
}
