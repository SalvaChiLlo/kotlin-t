package com.kotlin_t.trobify.presentacion.mapa

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.kotlin_t.trobify.R
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.kotlin_t.trobify.logica.Inmueble

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.home_item, null)

    private fun rendowWindowText(marker: Marker, view: View) {

        val direccion = view.findViewById<TextView>(R.id.home_direccion)
        val precio = view.findViewById<TextView>(R.id.home_precio_mes)
        val foto = view.findViewById<ImageView>(R.id.home_imagen)

        direccion.text = ((marker.tag as Inmueble)).direccion
        precio.text = ((marker.tag as Inmueble)).precio.toString() + "€"
        foto.setImageBitmap((marker.tag as Inmueble).miniatura)

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