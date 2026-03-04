package com.workoutapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.workoutapp.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY sortOrder ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE muscleGroupId = :groupId ORDER BY sortOrder ASC")
    fun getExercisesByGroup(groupId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE muscleGroupId = :groupId ORDER BY sortOrder ASC")
    suspend fun getExercisesByGroupSync(groupId: String): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: String): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(exercises: List<ExerciseEntity>)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun count(): Int
}
