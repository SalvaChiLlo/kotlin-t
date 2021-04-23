package com.kotlin_t.trobify.presentacion.registrar

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.databinding.FragmentRegistrarseBinding
import com.kotlin_t.trobify.logica.Usuario
import com.kotlin_t.trobify.presentacion.SharedViewModel

class RegistrarseFragment : Fragment() {
    private lateinit var binding: FragmentRegistrarseBinding
    private lateinit var viewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registrarse, container, false);
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return binding.root
    }

    private fun registrarUsuario(dni: String, nombre: String, apellidos: String, telefono: Int, contrsena: String, imagen: Bitmap): Usuario {

    }
}