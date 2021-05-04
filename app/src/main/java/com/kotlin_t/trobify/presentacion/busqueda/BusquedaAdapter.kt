package com.kotlin_t.trobify.presentacion.busqueda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.busqueda.BusquedaViewModel
import com.kotlin_t.trobify.persistencia.Busqueda

class BusquedaAdapter(val context: Context, val dataset: List<Busqueda>, val busquedaViewModel: BusquedaViewModel): RecyclerView.Adapter<BusquedaAdapter.BusquedaViewHolder>() {
    class BusquedaViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val boton: Button = view.findViewById(R.id.historialBoton);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusquedaAdapter.BusquedaViewHolder {
        val layoutInflater =
            LayoutInflater.from(context).inflate(R.layout.busqueda_item, parent, false)

        return BusquedaAdapter.BusquedaViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: BusquedaAdapter.BusquedaViewHolder, position: Int) {
        holder.boton.text = dataset[position].busqueda
        holder.boton.setOnClickListener {
            val btn = it as Button
            busquedaViewModel.search(btn.text.toString())
            val action = BusquedaFragmentDirections.actionBusquedaFragmentToNavHome()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataset.size
}