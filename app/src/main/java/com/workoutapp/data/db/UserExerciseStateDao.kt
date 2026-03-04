package com.workoutapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.workoutapp.data.entity.UserExerciseStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserExerciseStateDao {
    @Query("SELECT * FROM user_exercise_state WHERE exerciseId = :exerciseId")
    suspend fun getState(exerciseId: String): UserExerciseStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertState(state: UserExerciseStateEntity)

    @Query("SELECT * FROM user_exercise_state")
    fun getAllStates(): Flow<List<UserExerciseStateEntity>>
}
