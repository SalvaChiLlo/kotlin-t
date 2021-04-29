package com.kotlin_t.trobify.presentacion.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.kotlin_t.trobify.MainActivity
import com.kotlin_t.trobify.R
import com.kotlin_t.trobify.presentacion.SharedViewModel
import com.kotlin_t.trobify.presentacion.home.HomeFragmentDirections

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

    private val sharedViewModel: SharedViewModel by activityViewModels()
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
            registrarse()
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
    fun checkLoginCredentials(username: String, password: String, view: View) {

        var usuario = sharedViewModel.getUserFromCredentials(username, password)

        if(usuario == null) {

            errorText.text = "Usuario o Contraseña incorrectos, por favor inténtelo de nuevo"

        } else {

            sharedViewModel.updateCurrentUser(usuario)

            findNavController().navigate(R.id.action_loginFragment_to_nav_home, null)

        }

    }

    fun registrarse() {

        // TODO: Navegar al fragment de Registrarse

    }

}