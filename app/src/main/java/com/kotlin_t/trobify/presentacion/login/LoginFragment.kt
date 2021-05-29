package com.kotlin_t.trobify.presentacion.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.logica.ContextClass

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val contextClass: ContextClass by activityViewModels()
    private lateinit var usernameField : EditText
    private lateinit var passwordField : EditText
    private lateinit var loginButton : Button
    private lateinit var registerButton : Button
    private lateinit var errorText : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // De momento nada
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        usernameField = view.findViewById(R.id.textFieldUsername)
        passwordField = view.findViewById(R.id.textFieldPassword)
        loginButton = view.findViewById(R.id.loginButton)
        registerButton = view.findViewById(R.id.registrarseButton)
        errorText = view.findViewById(R.id.errorMessageText)

        loginButton.setOnClickListener {
            checkLoginCredentials(usernameField.text.toString(), passwordField.text.toString(), view)
        }
        loginButton.isEnabled = false


        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrarseFragment, null)
        }

        val editTexts = listOf(usernameField, passwordField)

        for(editText in editTexts) {
            editText.addTextChangedListener(object: TextWatcher{
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var et1 = usernameField.text.toString().trim()
                    var et2 = passwordField.text.toString().trim()

                    loginButton.isEnabled = et1.isNotEmpty() && et2.isNotEmpty()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun afterTextChanged(
                    s: Editable
                ) {
                }

            })
        }



    }

    //Se asume que username y password no están vacios
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLoginCredentials(username: String, password: String, view: View) {

        var usuario = contextClass.getUserFromCredentials(username, password)

        if(usuario == null) {

            errorText.text = "Usuario o Contraseña incorrectos, por favor inténtelo de nuevo"

        } else {

            contextClass.updateCurrentUser(usuario)
            contextClass.insertarSesionActual(username)
            findNavController().navigate(R.id.action_loginFragment_to_nav_home, null)

        }

    }

}