package com.example.taskapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.adapter.TaskAdapter
import com.example.taskapp.databinding.FragmentHomeBinding
import com.example.taskapp.model.Task
import com.example.taskapp.viewModel.TaskViewModel

class HomeFragment : Fragment(R.layout.fragment_home),SearchView.OnQueryTextListener,MenuProvider {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var taskViewModel : TaskViewModel
    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        setupRecyclerView()

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }
    }

    private fun updateUI(task: List<Task>?){
        if(task != null){
            if(task.isNotEmpty()){
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(){
        taskAdapter = TaskAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        activity?.let {
            taskViewModel.getAllTasks().observe(viewLifecycleOwner){task ->
                taskAdapter.differ.submitList(task)
                updateUI(task)
            }
        }
    }

    private fun searchTask(query: String?){
        val searchQuery = "%$query"

        taskViewModel.searchTask(searchQuery).observe(this){list ->
            taskAdapter.differ.submitList(list)

        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchTask(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu,menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}