package com.kerlly.vizuete.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log

class LoginActivity : AppCompatActivity() {
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var mediaPlayer: MediaPlayer

    lateinit var manejadorArchivo: FileHandler
    lateinit var checkBoxRecordarme: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //Inicialización de variables
        manejadorArchivo = SharedPreferencesManager(this)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)


        leerDatosDePreferencias()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        //Inicialización de variables
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        //Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()
            //Validaciones de datos requeridos y formatos
            if (!validarDatosRequeridos())
                return@setOnClickListener
            //Guardar datos en preferencias.
            guardarDatosEnPreferencias()
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intencion = Intent(this, MainActivity::class.java)
            intencion.putExtra(EXTRA_LOGIN, email)
            startActivity(intencion)
        }
        buttonNewUser.setOnClickListener {

        }
        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }


    private fun validarDatosRequeridos(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email_required))
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_password_required))
            editTextPassword.requestFocus()
            return false
        }
        if (password.length < 3) {
            editTextPassword.setError(getString(R.string.error_password_min_length))
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun leerDatosDePreferencias() {
        val listadoLeido = manejadorArchivo.ReadInformation()
        if (listadoLeido.first != null) {
            checkBoxRecordarme.isChecked = true
        }
        editTextEmail.setText(listadoLeido.first)
        editTextPassword.setText(listadoLeido.second)
    }

    private fun GuardarDatosEnPreferencias() {
        manejadorArchivo =
            SharedPreferencesManager(this)
        manejadorArchivo. SaveInformation ("epn.fis" to "123")
        manejadorArchivo = EncriptedSharedPreferencesManager (this)
        manejadorArchivo. SaveInformation ("epn.fis" to "1234")
    }
    private fun LeerDatosDePreferencias() {
        var datoLeido: Pair<String, String>
        manejadorArchivo = SharedPreferencesManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "SharedPreferencesManager"+datoLeido.toList().toString())
        manejadorArchivo = EncriptedSharedPreferencesManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "EncriptedSharedPreferencesManager"+datoLeido.toList().toString())
    }

    private fun guardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val listadoAGrabar: Pair<String, String>
        if (checkBoxRecordarme.isChecked) {
            listadoAGrabar = email to clave
        } else {
            listadoAGrabar = "" to ""
        }
        manejadorArchivo.SaveInformation(listadoAGrabar)
    }
}
