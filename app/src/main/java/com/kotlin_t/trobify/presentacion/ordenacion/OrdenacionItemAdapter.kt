package com.kotlin_t.trobify.presentacion.ordenacion

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.ordenacion.criteria.*

class OrdenacionItemAdapter(private val sharedViewModel: SharedViewModel, private val dataset: List<String>, private val fragment: OrdenacionFragment): RecyclerView.Adapter<OrdenacionItemAdapter.OrdenacionItemViewHolder>() {

    class OrdenacionItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val criterio: TextView = view.findViewById(R.id.orden_relevancia)
        val selecionado: ImageView = view.findViewById(R.id.seleccionar_orden)
        val cancelar: ImageView = view.findViewById(R.id.cancelar_orden)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenacionItemViewHolder {
        val layoutAdapter = LayoutInflater.from(parent.context).inflate(R.layout.ordenacion_item, parent, false)
        return OrdenacionItemViewHolder(layoutAdapter)
    }

    override fun onBindViewHolder(holder: OrdenacionItemViewHolder, position: Int) {
        val item = dataset[position]
        if(item.equals(sharedViewModel.estrategiaOrdenacion.toString())){
            holder.selecionado.visibility = View.VISIBLE
            holder.cancelar.visibility = View.VISIBLE
            holder.criterio.typeface = DEFAULT_BOLD
        }
        holder.criterio.text = item
        holder.criterio.setOnClickListener { 
            if(item.equals("Relevancia")) sharedViewModel.estrategiaOrdenacion = Relevancia()
            else if (item.equals("Precio más bajo")) sharedViewModel.estrategiaOrdenacion = PrecioMasBajo()
            else if (item.equals("Precio más alto")) sharedViewModel.estrategiaOrdenacion = PrecioMasAlto()
            else if (item.equals("Más grande")) sharedViewModel.estrategiaOrdenacion = MayorTamano()
            else if(item.equals("Más pequeño")) sharedViewModel.estrategiaOrdenacion = MenorTamano()
            else if (item.equals("Pisos altos")) sharedViewModel.estrategiaOrdenacion = PisosAltos()
            else if(item.equals("Pisos bajos")) sharedViewModel.estrategiaOrdenacion = PisosBajos()


            fragment.findNavController().navigate(OrdenacionFragmentDirections.actionOrdenacionFragmentToNavHome())


        }
        holder.cancelar.setOnClickListener {
            sharedViewModel.estrategiaOrdenacion = null
            holder.selecionado.visibility = View.INVISIBLE
            holder.cancelar.visibility = View.INVISIBLE
            holder.criterio.typeface = DEFAULT
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}