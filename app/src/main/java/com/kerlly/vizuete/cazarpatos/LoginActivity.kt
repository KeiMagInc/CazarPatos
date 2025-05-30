package com.kerlly.vizuete.cazarpatos
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword:EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser:Button
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
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
            if(!validateRequiredData())
                return@setOnClickListener
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, email)
            startActivity(intent)
            finish()
        }
        buttonNewUser.setOnClickListener{

        }
        mediaPlayer=MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }
    private fun validateRequiredData():Boolean{
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
}
