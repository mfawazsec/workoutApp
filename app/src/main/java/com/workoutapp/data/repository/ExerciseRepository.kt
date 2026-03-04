package com.workoutapp.data.repository

import com.workoutapp.data.db.ExerciseDao
import com.workoutapp.data.model.Exercise
import com.workoutapp.data.model.toDomain
import com.workoutapp.data.seed.ExerciseSeedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    suspend fun seedIfEmpty() {
        if (exerciseDao.count() == 0) {
            exerciseDao.insertAll(ExerciseSeedData.exercises)
        }
    }

    fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercises().map { list -> list.map { it.toDomain() } }

    fun getExercisesByGroup(groupId: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByGroup(groupId).map { list -> list.map { it.toDomain() } }

    suspend fun getExercisesByGroupSync(groupId: String): List<Exercise> =
        exerciseDao.getExercisesByGroupSync(groupId).map { it.toDomain() }
}
