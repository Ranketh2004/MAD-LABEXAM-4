package com.example.taskapp.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.repository.TaskRepository

class TaskViewModelFactory(val app:Application,private val taskRepository:TaskRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(app,taskRepository) as T

    }
}