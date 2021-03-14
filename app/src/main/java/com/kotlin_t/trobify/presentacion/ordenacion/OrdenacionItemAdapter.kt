package com.kotlin_t.trobify.presentacion.ordenacion

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin_t.trobify.R

class OrdenacionItemAdapter(private val context: Context, private val dataset: List<String>): RecyclerView.Adapter<OrdenacionItemAdapter.OrdenacionItemViewHolder>() {

    class OrdenacionItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val criterio: CheckedTextView = view.findViewById(R.id.orden_relevancia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenacionItemViewHolder {
        val layoutAdapter = LayoutInflater.from(parent.context).inflate(R.layout.ordenacion_item, parent, false)
        return OrdenacionItemViewHolder(layoutAdapter)
    }

    override fun onBindViewHolder(holder: OrdenacionItemViewHolder, position: Int) {
        val item = dataset[position]
        Log.d("item", item)
        holder.criterio.text = item
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}