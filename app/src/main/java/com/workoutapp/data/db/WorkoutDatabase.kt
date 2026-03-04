package com.workoutapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.workoutapp.data.entity.ExerciseEntity
import com.workoutapp.data.entity.ProgressionLogEntity
import com.workoutapp.data.entity.UserExerciseStateEntity
import com.workoutapp.data.entity.WorkoutSessionEntity

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        UserExerciseStateEntity::class,
        ProgressionLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun userExerciseStateDao(): UserExerciseStateDao
    abstract fun progressionLogDao(): ProgressionLogDao

    companion object {
        @Volatile private var INSTANCE: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
