package com.kotlin_t.trobify.presentacion.mapa

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.persistencia.Inmueble

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.home_item, null)

    private fun rendowWindowText(marker: Marker, view: View) {

        val direccion = view.findViewById<TextView>(R.id.home_direccion)
        val precio = view.findViewById<TextView>(R.id.home_precio_mes)
        val foto = view.findViewById<ImageView>(R.id.home_imagen)
        val publishToggle = view.findViewById<FloatingActionButton>(R.id.publicarInmueble)

        direccion.text = ((marker.tag as Inmueble)).direccion
        precio.text = ((marker.tag as Inmueble)).precio.toString() + "€"
        foto.setImageBitmap((marker.tag as Inmueble).miniatura)
        publishToggle.visibility = View.GONE

    }

    override fun getInfoContents(marker: Marker) : View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

}