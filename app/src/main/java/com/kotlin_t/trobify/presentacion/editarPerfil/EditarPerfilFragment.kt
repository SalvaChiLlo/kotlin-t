package com.kotlin_t.trobify.presentacion.editarPerfil

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.database.AppDatabase
import com.kotlin_t.trobify.databinding.FragmentEditarPerfilBinding
import com.kotlin_t.trobify.logica.SharedViewModel
import com.kotlin_t.trobify.persistencia.SesionActual
import com.kotlin_t.trobify.persistencia.Usuario
import java.time.LocalDateTime

class EditarPerfilFragment : Fragment() {
    private lateinit var binding: FragmentEditarPerfilBinding
    private lateinit var database: AppDatabase
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var usuarioActual: Usuario
    private val REQUEST_CODE = 100
    private val USUARIO: Int = 1
    private val CONTRASENA: Int = 2
    private val DNI: Int = 3
    private val NOMBRE: Int = 4
    private val APELLIDOS: Int = 5
    private val TELEFONO: Int = 6
    private val IBAN: Int = 7
    private lateinit var contraseñaDesencriptada: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_editar_perfil, container, false)
        val application = requireNotNull(this.activity).application
        database = AppDatabase.getDatabase(application)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        usuarioActual = sharedViewModel.usuarioActual.value!!
        contraseñaDesencriptada = usuarioActual.contrasena
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.aplicarCambios.visibility = View.GONE
        setCampos()
        binding.cambiarUsuario.setOnClickListener {
            ensenarDialogo(USUARIO)

        }
        binding.cambiarContrasena.setOnClickListener {
            ensenarDialogo(CONTRASENA)

        }
        binding.cambiarDni.setOnClickListener {
            ensenarDialogo(DNI)
        }
        binding.cambiarNombre.setOnClickListener {
            ensenarDialogo(NOMBRE)
        }
        binding.cambiarApellidos.setOnClickListener {
            ensenarDialogo(APELLIDOS)

        }
        binding.cambiarTelefono.setOnClickListener {
            ensenarDialogo(TELEFONO)

        }
        binding.cambiarIban.setOnClickListener {
            ensenarDialogo(IBAN)

        }

        binding.nuevaImagen.setOnClickListener {
            nuevaImagen()
            binding.eliminarImagen.visibility = View.VISIBLE

        }
        binding.eliminarImagen.setOnClickListener {
            eliminarImagen()
            binding.eliminarImagen.visibility = View.GONE
        }
        binding.aplicarCambios.setOnClickListener {
            val usuarioNuevo = Usuario(
                binding.editDni.text.toString(),
                binding.editUsuario.text.toString(),
                contraseñaDesencriptada,
                binding.editNombre.text.toString(),
                binding.editApellidos.text.toString(),
                binding.editTelefono.text.toString(),
                binding.editIban.text.toString(),
                binding.imagen.drawable.toBitmap()
            )
            Log.d("Hola", usuarioNuevo.username)
            Log.d("Hola", usuarioNuevo.username)

            if (binding.editDni.text.toString() == usuarioActual.dni) {
                database.usuarioDAO().update(usuarioNuevo)
            } else {
                database.usuarioDAO().delete(usuarioActual)
                database.usuarioDAO().insertAll(usuarioNuevo)
            }
            sharedViewModel.usuarioActual.value = usuarioNuevo
            usuarioActual = usuarioNuevo
            binding.aplicarCambios.visibility = View.GONE
            database.sesionActualDAO().deleteSesion()
            database.sesionActualDAO().insertSesion(SesionActual(usuarioNuevo.username, LocalDateTime.now().toString()))
            setCampos()
        }
        binding.visibilidadContrasena.setOnClickListener {
            if (estaEncriptada(binding.editContrasena.text.toString())) {
                binding.visibilidadContrasena.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                binding.editContrasena.text = usuarioActual.contrasena
            } else {
                binding.visibilidadContrasena.setImageResource(R.drawable.ic_baseline_visibility_24)
                binding.editContrasena.text = encriptarContrasena(usuarioActual.contrasena)
            }
        }
        binding.eliminarIban.setOnClickListener {
            binding.editIban.text = ""
        }
        binding.editIban.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().isNotEmpty()){
                    binding.eliminarIban.visibility = View.VISIBLE
                }
                else{
                    binding.eliminarIban.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.eliminarCuenta.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Cuenata")
                .setMessage("¿Estás seguro de eliminar tu cuenta?")
                .setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {
                        eliminarUsuario()
                        database.sesionActualDAO().deleteSesion()
                        dialog.cancel()
                    })
                )
                .setNegativeButton("No", null)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()

        }

    }

    fun ensenarDialogo(tipoDialogo: Int) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setIcon(R.drawable.ic_baseline_info_24)
        val view: View =
            if (tipoDialogo == CONTRASENA) layoutInflater.inflate(R.layout.edit_contrasena, null) else layoutInflater.inflate(R.layout.edit_generico, null)
        dialog.setView(view)
        val textInputEditTextGenerico: TextInputEditText? =
            view.findViewById(R.id.inputEditarGenerico)
        val textInputLayoutGenerico: TextInputLayout? = view.findViewById(R.id.editarGenerico)
        val textInputEditTextContrasena: TextInputEditText? =
            view.findViewById(R.id.inputEditarContrasena)
        val textInputLayoutContrasena: TextInputLayout? = view.findViewById(R.id.editarContrasena)
        val textInputEditTextRepetirContrasena: TextInputEditText? =
            view.findViewById(R.id.inputEditarRepetirContrasena)
        val textInputLayoutRepetirContrasena: TextInputLayout? =
            view.findViewById(R.id.editarRepetirContrasena)
        var listener: DialogInterface.OnClickListener? = null
        var tituloGenerico: Int? = 0


        when (tipoDialogo) {
            USUARIO -> {
                tituloGenerico = R.string.usuario
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.username)
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {
                    binding.editUsuario.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                    dialog.cancel()
                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        var correcto = true
                        if (p0.toString().isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            return
                        }
                        if (p0.toString().length < 5) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.usuarioCorto)
                            return

                        }
                        if (database.usuarioDAO().existsUsername(p0.toString())) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.usuarioCogido)
                            correcto = false
                        }
                        for (i in p0.toString().indices) {
                            if (p0.toString()[i].toInt() == 32) {
                                textInputLayoutGenerico?.isErrorEnabled = true;
                                textInputLayoutGenerico?.error =
                                    getString(R.string.usuarioIncorrecto)
                                correcto = false
                                break
                            }
                        }
                        if(correcto) textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
            DNI -> {

                tituloGenerico = R.string.dni
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.dni)
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {


                    binding.editDni.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                    dialog.cancel()

                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        var correcto = true
                        val text = p0.toString()
                        if (text.isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            return
                        }
                        if (database.usuarioDAO().existsId(text)) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.dniCogido)
                            return
                        }
                        var esValido = true

                        for (i in text.indices) {
                            if (i == 8) {
                                if (!Character.isLetter(text[i])) {
                                    esValido = false;
                                    break
                                }
                            } else if (!Character.isDigit(text[i])) {
                                esValido = false;
                                break
                            }
                        }
                        if (text.length != 9 || !esValido) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.dniIncorrecto)
                            correcto = false
                        }
                        if(correcto)textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
            NOMBRE -> {
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.nombre)
                tituloGenerico = R.string.nombre
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {



                        binding.editNombre.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                        dialog.cancel()

                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher{
                    var correcto = true
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0.toString().isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            correcto = false
                            return
                        }
                        val text = p0.toString()
                        for (i in text.indices) {
                            if (!Character.isLetter(text[i]) && text[i].toInt() != 32) {
                                textInputLayoutGenerico?.isErrorEnabled = true;
                                textInputLayoutGenerico?.error = getString(R.string.nombreInvalido)
                                correcto = false

                            }
                            else{
                                correcto = true
                            }
                        }

                        if(correcto) textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
            APELLIDOS -> {

                tituloGenerico = R.string.apellidos
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.apellidos)
                textInputLayoutGenerico?.isErrorEnabled = false


                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {
                    binding.editApellidos.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                    dialog.cancel()


                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher {
                    var correcto = true
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        if (p0.toString().isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            return

                        }

                        for (i in p0.toString().indices) {
                            if (!Character.isLetter(p0.toString()[i]) && p0.toString()[i].toInt() != 32) {
                                textInputLayoutGenerico?.isErrorEnabled = true;
                                textInputLayoutGenerico?.error =
                                    getString(R.string.apellidosInvalidos)
                                correcto = false
                                break
                            }

                        }
                        if(correcto)textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }


                })
            }
            TELEFONO -> {

                tituloGenerico = R.string.telefono
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.telefono)
                textInputEditTextGenerico?.inputType = InputType.TYPE_CLASS_PHONE
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {
                    binding.editTelefono.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                        dialog.cancel()

                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher{
                    var correcto = true
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        val text = p0.toString()
                        if (text.isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            return
                        }

                        var esValido = true;
                        for (i in text.indices) {
                            if (!Character.isDigit(text[i])) {

                                esValido = false;
                                break
                            }
                        }
                        var res = text.length
                        Log.d("Hola", res.toString())
                        Log.d("Hola", (text.length != 9).toString())
                        if (text.length != 9 || !esValido) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.telefonoIncorrecto)
                            correcto = false
                        }
                        else{
                            correcto = true
                        }


                        if (correcto) textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
            IBAN -> {

                tituloGenerico = R.string.iban
                textInputEditTextGenerico?.text = SpannableStringBuilder(usuarioActual.iban)
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {




                        binding.editIban.text = textInputEditTextGenerico?.text.toString()
                    binding.aplicarCambios.visibility = View.VISIBLE
                        dialog.cancel()

                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextGenerico?.addTextChangedListener(object : TextWatcher{
                    var correcto = true
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val text = p0.toString()
                        if (text.isEmpty()) {
                            textInputLayoutGenerico?.isErrorEnabled = true
                            textInputLayoutGenerico?.error = getString(R.string.completaCampo)
                            return
                        }

                        var esValido = true;
                        for (i in text.indices) {
                            if (i == 0 || i == 1) {
                                if (!Character.isLetter(text[i])) {
                                    esValido = false
                                    break
                                }
                            } else {
                                if (!Character.isDigit(text[i])) {
                                    esValido = false
                                    break
                                }
                            }
                        }
                        if (text.length != 24 || !esValido) {
                            textInputLayoutGenerico?.isErrorEnabled = true;
                            textInputLayoutGenerico?.error = getString(R.string.ibanIncorrecto)
                            correcto = false
                        }
                        else{
                            correcto = true
                        }
                        if(correcto)textInputLayoutGenerico?.isErrorEnabled = false
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correcto

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
            CONTRASENA -> {
                textInputLayoutContrasena?.hint = getString(R.string.contrasena)
                textInputLayoutRepetirContrasena?.hint = getString(R.string.repetir_contrasena)
                var correctaContrasena = false
                var correctaContrasenaRepetida = false
                listener = DialogInterface.OnClickListener(fun(dialog: DialogInterface, _: Int) {
                        contraseñaDesencriptada = textInputEditTextContrasena?.text.toString()
                        binding.editContrasena.text =
                            encriptarContrasena(textInputEditTextContrasena?.text.toString())
                    binding.aplicarCambios.visibility = View.VISIBLE
                        dialog.cancel()

                })
                dialog.setPositiveButton("Confirmar", listener)
                dialog.setNegativeButton("Cancelar", null)
                val dialogCreado = dialog.create()
                dialogCreado.show()
                dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                textInputEditTextContrasena?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        p0: CharSequence?,
                        p1: Int,
                        p2: Int,
                        p3: Int
                    ) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (textInputLayoutContrasena != null) {
                            correctaContrasena = contrasenaValida(p0.toString(), textInputLayoutContrasena)
                        }
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correctaContrasena && correctaContrasenaRepetida

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
                textInputEditTextRepetirContrasena?.addTextChangedListener(object :
                    TextWatcher {
                    override fun beforeTextChanged(
                        p0: CharSequence?,
                        p1: Int,
                        p2: Int,
                        p3: Int
                    ) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (textInputLayoutRepetirContrasena != null) {
                            correctaContrasenaRepetida = coincidenContrasenas(
                                textInputEditTextContrasena?.text.toString(),
                                p0.toString(),
                                textInputLayoutRepetirContrasena
                            )
                        }
                        dialogCreado.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = correctaContrasena && correctaContrasenaRepetida
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })

            }

        }
        textInputLayoutGenerico?.hint = tituloGenerico?.let { getString(it) }


    }

    private fun setCampos() {
        binding.editUsuario.text = usuarioActual.username
        binding.editContrasena.text = encriptarContrasena(usuarioActual.contrasena)
        binding.editDni.text = usuarioActual.dni
        binding.editNombre.text = usuarioActual.nombre
        binding.editApellidos.text = usuarioActual.apellidos
        binding.editTelefono.text = usuarioActual.telefono
        binding.editIban.text = usuarioActual.iban
        if (usuarioActual.fotoPerfil != null) binding.imagen.setImageBitmap(usuarioActual.fotoPerfil) else binding.imagen.setImageDrawable(
            getDrawable(requireContext(), R.drawable.anonymous_user)
        )


    }

    fun estaEncriptada(contrasena: String): Boolean {
        for (i in contrasena.indices) {
            if (contrasena[i].toString() != "●") return false
        }
        return true
    }

    private fun encriptarContrasena(contrasena: String): String {
        var contrasenaEncriptada = ""
        repeat(contrasena.length) {
            contrasenaEncriptada += "●"
        }
        return contrasenaEncriptada
    }

    fun contrasenaValida(contrasena: String, textInputLayout: TextInputLayout): Boolean {
        if (contrasena.isEmpty()) {
            textInputLayout.isErrorEnabled = true;
            textInputLayout.error = getString(R.string.completaCampo)
            return false;
        }
        if (contrasena.length < 8) {
            textInputLayout.isErrorEnabled = true;
            textInputLayout.error = getString(R.string.contrasenaCorta)
            return false;
        }
        textInputLayout.isErrorEnabled = false;
        return true;
    }

    fun coincidenContrasenas(
        contrasena: String,
        contrasenaRepetida: String,
        textInputLayout: TextInputLayout
    ): Boolean {
        if (contrasenaRepetida.isEmpty()) {
            textInputLayout.isErrorEnabled = true;
            textInputLayout.error = getString(R.string.completaCampo)
            return false;
        }
        if (contrasenaRepetida != contrasena) {
            textInputLayout.isErrorEnabled = true;
            textInputLayout.error = getString(R.string.contrasenaNoCoinciden)
            return false;
        }
        textInputLayout.isErrorEnabled = false
        return true;
    }

    private fun nuevaImagen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                binding.imagen.setImageURI(data.data)
                binding.aplicarCambios.visibility = View.VISIBLE
            }

        }


    }


    private fun eliminarImagen() {
        binding.imagen.setImageResource(R.drawable.anonymous_user)
        binding.aplicarCambios.visibility = View.VISIBLE

    }

    private fun eliminarUsuario() {
        sharedViewModel.usuarioActual.value = null
        database.usuarioDAO().deleteById(usuarioActual.dni)
        findNavController().navigate(EditarPerfilFragmentDirections.actionNavMiCuentaToNavHome())

    }

}
