package com.example.taskapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskapp.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(t:Task)

    @Update
    suspend fun updateTask(t:Task)

    @Delete
    suspend fun deleteTask(t:Task)

    @Query("SELECT * FROM TASKS ORDER BY id DESC")
    fun getAllTasks():LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE title LIKE :query OR descr LIKE :query")
    fun searchTask(query:String?):LiveData<List<Task>>
}