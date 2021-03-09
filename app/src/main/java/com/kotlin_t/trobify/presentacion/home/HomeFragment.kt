package com.kotlin_t.trobify.presentacion.home

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = HomeViewModelFactory(datasource, application)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = HomeItemAdapter(requireContext(), homeViewModel.getListaInmubles(), homeViewModel)
                setHasFixedSize(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filterButton = view.findViewById<ImageView>(R.id.filter_button)

        filterButton.setOnClickListener{
            val action = HomeFragmentDirections.actionNavHomeToFiltrarFragment()
            findNavController().navigate(action)
        }
    }



}