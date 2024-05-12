package com.example.taskapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentAddTaskBinding
import com.example.taskapp.model.Task
import com.example.taskapp.viewModel.TaskViewModel
import java.text.SimpleDateFormat

class AddTaskFragment : Fragment(R.layout.fragment_add_task),MenuProvider {

    private var addTaskBinding : FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view
    }

    private fun saveTask(view: View){
        val taskTitle = binding.addNoteTitle.text.toString().trim()
        val taskDesc = binding.addNoteDesc.text.toString().trim()
        val taskPriority = binding.addNotePriority.text.toString().trim()
        val taskStatus = binding.addNoteStatus.text.toString().trim()

        if(taskTitle.isNotEmpty()){

            val task = Task(0,taskTitle,taskDesc,taskStatus,taskPriority)
            taskViewModel.insertTask(task)

            Toast.makeText(addTaskView.context,"Task saved",Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment,false)

        }else{
            Toast.makeText(addTaskView.context,"Enter task title",Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.addtask_menu,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu ->{
                saveTask(addTaskView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }

}