package com.kotlin_t.trobify.presentacion.filtrar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.slider.Slider
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentFiltrarBinding
import com.kotlin_t.trobify.presentacion.Constantes
import java.text.NumberFormat
import java.util.*

class FiltrarFragment : Fragment() {
    lateinit var binding: FragmentFiltrarBinding
    lateinit var filtrarViewModel: FiltrarViewModel
    lateinit var datasource: AppDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_filtrar, container, false)
        val application = requireNotNull(this.activity).application
        datasource = AppDatabase.getDatabase(application)
        val viewModelFactory = FiltrarViewModelFactory(datasource, application)
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
    }

    private fun setOperacionForm() {
        binding.ventaChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.VENTA,
                isChecked
            )
        }
        binding.alquilerChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.ALQUILER,
                isChecked
            )
        }
        binding.alquilerHabitacionChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.ALQUILER_HABITACION,
                isChecked
            )
        }
        binding.intecambioChkb.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeOperaciones(
                Constantes.INTERCAMBIO_VIVIENDA,
                isChecked
            )
        }
    }

    private fun setInmuebleForm() {
        binding.aticoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.ATICO, isChecked
            )
        }
        binding.casaChaletChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.CASA_CHALET, isChecked
            )
        }
        binding.habitacionChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.HABITACION, isChecked
            )
        }
        binding.pisoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeTipos(
                Constantes.PISO, isChecked
            )
        }
    }

    private fun setHabitacionesForm() {
        binding.habitaciones1Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.UNO, isChecked)
        }
        binding.habitaciones2Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.DOS, isChecked)
        }
        binding.habitaciones3Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.TRES, isChecked)
        }
        binding.habitaciones4Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeHabitaciones(Constantes.CUATROoMAS, isChecked)
        }
    }

    private fun setBanosForm() {
        binding.banos1Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.UNO, isChecked)
        }
        binding.banos2Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.DOS, isChecked)
        }
        binding.banos3Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.TRES, isChecked)
        }
        binding.banos4Chbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeBanos(Constantes.CUATROoMAS, isChecked)
        }
    }

    private fun setEstadoForm() {
        binding.nuevaConstruccionChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.NUEVA_CONSTRUCCION, isChecked)
        }
        binding.buenEstadoChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.BUEN_ESTADO, isChecked)
        }
        binding.aReformarChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changeEstado(Constantes.REFORMAR, isChecked)
        }
    }

    private fun setPlantaForm() {
        binding.plantaIntermediaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_INTERMEDIA, isChecked)
        }
        binding.plantaBajaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_BAJA, isChecked)
        }
        binding.plantaAltaChbk.setOnCheckedChangeListener { _, isChecked ->
            filtrarViewModel.changePlanta(Constantes.PLANTA_ALTA, isChecked)
        }
    }

    fun setPriceForm() {
        val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)
        val touchListener: Slider.OnChangeListener = object : Slider.OnChangeListener {
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                if (slider == binding.precioMaximoSlider) {
                    binding.precioMaximoTextview.text = formatter.format(slider.value)
                    if(slider.value > binding.precioMinimoSlider.valueFrom) {
                        binding.precioMinimoSlider.valueTo = slider.value
                    }
                    filtrarViewModel.changePrecios(slider.value.toInt(), false)
                } else {
                    binding.precioMinimoTextview.text = formatter.format(slider.value)
                    if(slider.value < binding.precioMaximoSlider.valueTo) {
                        binding.precioMaximoSlider.valueFrom = slider.value
                    }
                    filtrarViewModel.changePrecios(slider.value.toInt(), true)
                }
            }
        }

        binding.precioMaximoSlider.stepSize = 10.0.toFloat();
        binding.precioMinimoSlider.stepSize = 10.0.toFloat();

        binding.precioMaximoSlider.valueTo= datasource.inmuebleDAO().getMaxPrecio().toFloat()
        binding.precioMinimoSlider.valueTo= binding.precioMaximoSlider.valueTo
        binding.precioMinimoSlider.valueFrom= datasource.inmuebleDAO().getMinPrecio().toFloat()
        binding.precioMaximoSlider.valueFrom= binding.precioMinimoSlider.valueFrom

        binding.precioMaximoSlider.value = binding.precioMaximoSlider.valueTo
        binding.precioMaximoSlider.value = binding.precioMaximoSlider.valueTo

        binding.precioMaximoSlider.addOnChangeListener(touchListener)
        binding.precioMinimoSlider.addOnChangeListener(touchListener)

        binding.precioMaximoTextview.text = formatter.format(
            datasource.inmuebleDAO().getMaxPrecio()
        )
        binding.precioMinimoTextview.text = formatter.format(
            datasource.inmuebleDAO().getMinPrecio()
        )

    }

}