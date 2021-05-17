package com.kotlin_t.trobify.presentacion.recuperarFavoritos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentListaFavoritosBinding
import com.kotlin_t.trobify.databinding.FragmentRecuperarFavoritosBinding
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModel
import com.kotlin_t.trobify.logica.favoritos.ListaFavoritosViewModelFactory
import com.kotlin_t.trobify.logica.recuperarFavoritos.RecuperarFavoritosViewModel
import com.kotlin_t.trobify.logica.recuperarFavoritos.RecuperarFavoritosViewModelFactory
import com.kotlin_t.trobify.persistencia.Favorito
import com.kotlin_t.trobify.presentacion.favoritos.FavoritoAdapter

class RecuperarFavoritosFragment : Fragment() {
    private lateinit var binding: FragmentRecuperarFavoritosBinding
    private lateinit var recuperarFavoritosViewModel: RecuperarFavoritosViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recuperar_favoritos, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = RecuperarFavoritosViewModelFactory(datasource, application, sharedViewModel)
        recuperarFavoritosViewModel =
            ViewModelProvider(this, viewModelFactory).get(RecuperarFavoritosViewModel::class.java)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recuperarFavoritosRecyclerView
        Log.e("RECUPERAR", sharedViewModel.favoritosEliminados.toList().toString())
        recyclerView.adapter = FavoritoAdapter(
            requireContext(),
            sharedViewModel.favoritosEliminados.toList() as List<Favorito>, null,recuperarFavoritosViewModel, recuperarFavoritosViewModel.database, sharedViewModel, true
        )
    }
}