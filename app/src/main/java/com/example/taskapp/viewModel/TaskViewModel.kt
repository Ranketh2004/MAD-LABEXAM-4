package com.example.taskapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.model.Task
import com.example.taskapp.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(app:Application,private val taskRepository: TaskRepository):AndroidViewModel(app) {

    fun insertTask(t: Task) =
        viewModelScope.launch {
            taskRepository.insertTask(t)
        }

    fun updateTask(t: Task) =
        viewModelScope.launch {
            taskRepository.updateTask(t)
        }

    fun deleteTask(t: Task) =
        viewModelScope.launch {
            taskRepository.deleteTask(t)
        }

    fun getAllTasks() = taskRepository.getAllTasks()

    fun searchTask(query:String?) =
        taskRepository.searchTask(query)
}