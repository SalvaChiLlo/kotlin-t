package com.kotlin_t.trobify.presentacion.filtrar

import android.os.Bundle
import android.util.Log
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentFiltrarBinding
import com.kotlin_t.trobify.presentacion.Constantes
import com.kotlin_t.trobify.presentacion.SharedViewModel
import java.text.NumberFormat
import java.util.*

class FiltrarFragment : Fragment() {
    lateinit var binding: FragmentFiltrarBinding
    lateinit var filtrarViewModel: FiltrarViewModel
    lateinit var datasource: AppDatabase
    lateinit var model: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_filtrar, container, false)
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val viewModelFactory = FiltrarViewModelFactory(datasource, application, model)
        filtrarViewModel =
            ViewModelProvider(this, viewModelFactory).get(FiltrarViewModel::class.java)
        binding.viewModel = filtrarViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOperacionForm()
        setInmuebleForm()
        setHabitacionesForm()
        setBanosForm()
        setEstadoForm()
        setPlantaForm()
        setPriceForm()

//        binding.filtrarButton.setOnClickListener {
//            filtrarViewModel.filtrarInmuebles()
//            val action = FiltrarFragmentDirections.actionFiltrarFragmentToNavHome()
//            findNavController().navigate(action)
//        }

        binding.resetFiltros.setOnClickListener{
            resetForm()
        }
    }

    private fun setOperacionForm() {
        binding.ventaChkb.isChecked = model.operacionesOpciones.value!!.contains(Constantes.VENTA)
        binding.ventaChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.VENTA,
                isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.alquilerChkb.isChecked = model.operacionesOpciones.value!!.contains(Constantes.ALQUILER)
        binding.alquilerChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.ALQUILER,
                isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.alquilerHabitacionChkb.isChecked = model.operacionesOpciones.value!!.contains(Constantes.ALQUILER_HABITACION)
        binding.alquilerHabitacionChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.ALQUILER_HABITACION,
                isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.intecambioChkb.isChecked = model.operacionesOpciones.value!!.contains(Constantes.INTERCAMBIO_VIVIENDA)
        binding.intecambioChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.INTERCAMBIO_VIVIENDA,
                isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setInmuebleForm() {
        binding.aticoChbk.isChecked = model.tiposOpciones.value!!.contains(Constantes.ATICO)
        binding.aticoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.ATICO, isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.casaChaletChbk.isChecked = model.tiposOpciones.value!!.contains(Constantes.CASA_CHALET)
        binding.casaChaletChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.CASA_CHALET, isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.habitacionChbk.isChecked = model.tiposOpciones.value!!.contains(Constantes.HABITACION)
        binding.habitacionChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.HABITACION, isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
        binding.pisoChbk.isChecked = model.tiposOpciones.value!!.contains(Constantes.PISO)
        binding.pisoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.PISO, isChecked
            )
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setHabitacionesForm() {
        binding.habitaciones1Chbk.isChecked = model.habitacionesOpciones.value!!.contains(Constantes.UNO)
        binding.habitaciones1Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.UNO, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.habitaciones2Chbk.isChecked = model.habitacionesOpciones.value!!.contains(Constantes.DOS)
        binding.habitaciones2Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.DOS, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.habitaciones3Chbk.isChecked = model.habitacionesOpciones.value!!.contains(Constantes.TRES)
        binding.habitaciones3Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.TRES, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.habitaciones4Chbk.isChecked = model.habitacionesOpciones.value!!.contains(Constantes.CUATROoMAS)
        binding.habitaciones4Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.CUATROoMAS, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setBanosForm() {
        binding.banos1Chbk.isChecked = model.banosOpciones.value!!.contains(Constantes.UNO)
        binding.banos1Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.UNO, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.banos2Chbk.isChecked = model.banosOpciones.value!!.contains(Constantes.DOS)
        binding.banos2Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.DOS, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.banos3Chbk.isChecked = model.banosOpciones.value!!.contains(Constantes.TRES)
        binding.banos3Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.TRES, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.banos4Chbk.isChecked = model.banosOpciones.value!!.contains(Constantes.CUATROoMAS)
        binding.banos4Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.CUATROoMAS, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setEstadoForm() {
        binding.nuevaConstruccionChbk.isChecked = model.estadoOpciones.value!!.contains(Constantes.NUEVA_CONSTRUCCION)
        binding.nuevaConstruccionChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.NUEVA_CONSTRUCCION, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.buenEstadoChbk.isChecked = model.estadoOpciones.value!!.contains(Constantes.BUEN_ESTADO)
        binding.buenEstadoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.BUEN_ESTADO, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.aReformarChbk.isChecked = model.estadoOpciones.value!!.contains(Constantes.REFORMAR)
        binding.aReformarChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.REFORMAR, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setPlantaForm() {
        binding.plantaIntermediaChbk.isChecked = model.plantaOpciones.value!!.contains(Constantes.PLANTA_INTERMEDIA)
        binding.plantaIntermediaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_INTERMEDIA, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.plantaBajaChbk.isChecked = model.plantaOpciones.value!!.contains(Constantes.PLANTA_BAJA)
        binding.plantaBajaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_BAJA, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
        binding.plantaAltaChbk.isChecked = model.plantaOpciones.value!!.contains(Constantes.PLANTA_ALTA)
        binding.plantaAltaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_ALTA, isChecked)
            filtrarViewModel.filtrarInmuebles()
        }
    }

    private fun setPriceForm() {
        val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

        val touchListener: RangeSlider.OnChangeListener =
            RangeSlider.OnChangeListener { slider, value, fromUser ->
                binding.precioMaximoTextview.text = formatter.format(slider.values[1])
                binding.precioMinimoTextview.text = formatter.format(slider.values[0])
                filtrarViewModel.changePrecios(slider.values[0].toInt(), true)
                filtrarViewModel.changePrecios(slider.values[1].toInt(), false)
                filtrarViewModel.filtrarInmuebles()
            }

        binding.precioMaximoSlider.valueTo = datasource.inmuebleDAO().getMaxPrecio().toFloat()
        binding.precioMaximoSlider.valueFrom = datasource.inmuebleDAO().getMinPrecio().toFloat()
        binding.precioMaximoSlider.values =
            mutableListOf(binding.precioMaximoSlider.valueFrom, binding.precioMaximoSlider.valueTo)


        binding.precioMaximoSlider.addOnChangeListener(touchListener)
        binding.precioMaximoSlider.setMinSeparationValue(100f)

        binding.precioMinimoTextview.text = formatter.format(binding.precioMaximoSlider.valueFrom)
        binding.precioMaximoTextview.text = formatter.format(binding.precioMaximoSlider.valueTo)
    }

    private fun resetForm() {
        model.resetFiltro()
        binding.alquilerHabitacionChkb.isChecked = false
        binding.ventaChkb.isChecked = false
        binding.alquilerChkb.isChecked = false
        binding.intecambioChkb.isChecked = false
        binding.aticoChbk.isChecked = false
        binding.casaChaletChbk.isChecked = false
        binding.habitacionChbk.isChecked = false
        binding.pisoChbk.isChecked = false
        binding.habitaciones1Chbk.isChecked = false
        binding.habitaciones2Chbk.isChecked = false
        binding.habitaciones3Chbk.isChecked = false
        binding.habitaciones4Chbk.isChecked = false
        binding.banos1Chbk.isChecked = false
        binding.banos2Chbk.isChecked = false
        binding.banos3Chbk.isChecked = false
        binding.banos4Chbk.isChecked = false
        binding.nuevaConstruccionChbk.isChecked = false
        binding.buenEstadoChbk.isChecked = false
        binding.aReformarChbk.isChecked = false
        binding.plantaIntermediaChbk.isChecked = false
        binding.plantaBajaChbk.isChecked = false
        binding.plantaAltaChbk.isChecked = false

        val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)
        binding.precioMinimoTextview.text = formatter.format(model.preciosOpciones.value!![0])
        binding.precioMaximoTextview.text = formatter.format(model.preciosOpciones.value!![1])
        binding.precioMaximoSlider.values =
            mutableListOf(model.preciosOpciones.value!![0].toFloat(), model.preciosOpciones.value!![1].toFloat())
        filtrarViewModel.filtrarInmuebles()
    }
}