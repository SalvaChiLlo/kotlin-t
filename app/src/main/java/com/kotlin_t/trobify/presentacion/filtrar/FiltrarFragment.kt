package com.kotlin_t.trobify.presentacion.filtrar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentFiltrarBinding
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.presentacion.favoritos.ListaFavoritosViewModelFactory

class FiltrarFragment : Fragment() {
    lateinit var binding: FragmentFiltrarBinding
    lateinit var filtrarViewModel: FiltrarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_filtrar, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = FiltrarViewModelFactory(datasource, application)
        filtrarViewModel =
            ViewModelProvider(this, viewModelFactory).get(FiltrarViewModel::class.java)
        binding.viewModel = filtrarViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}