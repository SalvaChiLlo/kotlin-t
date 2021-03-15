package com.kotlin_t.trobify.presentacion.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.databinding.FragmentHomeBinding
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.ordenacion.OrdenacionFragmentDirections


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel


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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            val estrategia = sharedViewModel.estrategiaOrdenacion
            sharedViewModel.inmuebles.observe(viewLifecycleOwner, Observer {
                if (estrategia != null) {
                    Log.d("estrategia", estrategia.toString())
                    recyclerView.adapter = HomeItemAdapter(requireContext(), estrategia.ordenar(it), homeViewModel)
                }
                else{
                    recyclerView.adapter = HomeItemAdapter(requireContext(), it, homeViewModel)
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filterButton = view.findViewById<ImageView>(R.id.filter_button)
        val orderButton = view.findViewById<ImageView>(R.id.order_button)

        filterButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToFiltrarFragment()
            findNavController().navigate(action)
        }
        orderButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToOrdenacionFragment())
        }

    }


}