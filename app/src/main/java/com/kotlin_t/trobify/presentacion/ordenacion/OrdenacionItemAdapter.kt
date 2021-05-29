package com.kotlin_t.trobify.presentacion.ordenacion

import android.graphics.Typeface.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.ordenacion.OrdenacionViewModel

class OrdenacionItemAdapter(
    private val ordenacionViewModel: OrdenacionViewModel,
    private val dataset: List<String>,
    private val fragment: OrdenacionFragment
) : RecyclerView.Adapter<OrdenacionItemAdapter.OrdenacionItemViewHolder>() {

    class OrdenacionItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val criterio: TextView = view.findViewById(R.id.orden_relevancia)
        val selecionado: ImageView = view.findViewById(R.id.seleccionar_orden)
        val cancelar: ImageView = view.findViewById(R.id.cancelar_orden)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenacionItemViewHolder {
        val layoutAdapter =
            LayoutInflater.from(parent.context).inflate(R.layout.ordenacion_item, parent, false)
        return OrdenacionItemViewHolder(layoutAdapter)
    }

    override fun onBindViewHolder(holder: OrdenacionItemViewHolder, position: Int) {
        val estrategia = dataset[position]
        holder.criterio.text = estrategia
        if (estrategia == ordenacionViewModel.getEstrategiaOrdenacion()) {
            holder.selecionado.visibility = View.VISIBLE
            holder.cancelar.visibility = View.VISIBLE
            holder.criterio.typeface = DEFAULT_BOLD
        }
        holder.criterio.setOnClickListener {
            ordenacionViewModel.elegirEstrategia(estrategia)
            fragment.findNavController().navigate(OrdenacionFragmentDirections.actionOrdenacionFragmentToNavHome())
        }
        holder.cancelar.setOnClickListener {
            ordenacionViewModel.eliminarEstrategia()
            holder.selecionado.visibility = View.INVISIBLE
            holder.cancelar.visibility = View.INVISIBLE
            holder.criterio.typeface = DEFAULT
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}