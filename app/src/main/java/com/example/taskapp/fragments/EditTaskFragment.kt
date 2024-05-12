package com.example.taskapp.fragments

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentEditTaskBinding
import com.example.taskapp.model.Task
import com.example.taskapp.viewModel.TaskViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditTaskFragment : Fragment(R.layout.fragment_edit_task),MenuProvider {

    private var editTaskBinding : FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask : Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editNoteTitle.setText(currentTask.title)
        binding.editNoteDesc.setText(currentTask.descr)
        binding.editNoteStatus.setText(currentTask.status)
        binding.editNotePriority.setText(currentTask.priority)

        binding.editNoteFab.setOnClickListener {
            val taskTitle = binding.editNoteTitle.text.toString().trim()
            val taskDesc = binding.editNoteDesc.text.toString().trim()
            val taskStatus = binding.editNoteStatus.text.toString().trim()
            val taskPriority = binding.editNotePriority.text.toString().trim()

            if(taskTitle.isNotEmpty()){
                val task = Task(currentTask.id,taskTitle,taskDesc,taskStatus,taskPriority)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment,false)
            }else{
                Toast.makeText(context,"Enter task title",Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun deleteTask(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task")
            setPositiveButton("Delete"){_,_ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context,"Task deleted",Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment,false)

            }
            setNegativeButton("cancel",null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edittask_menu,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu ->{
                deleteTask()
                true
            }else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }
}


