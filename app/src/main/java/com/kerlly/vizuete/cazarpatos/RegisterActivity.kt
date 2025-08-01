package com.kerlly.vizuete.cazarpatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias a las vistas del layout
        val emailEditText = findViewById<EditText>(R.id.editTextEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordRegister)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        val backToLoginButton = findViewById<Button>(R.id.buttonBackToLogin)

        // Lógica para el botón "Registrar"
        signUpButton.setOnClickListener {
            // Limpia errores previos
            emailEditText.error = null
            passwordEditText.error = null
            confirmPasswordEditText.error = null

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // --- VALIDACIONES ---
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                confirmPasswordEditText.error = getString(R.string.error_password_mismatch)
                return@setOnClickListener
            }
            if (password.length < 6) {
                passwordEditText.error = "La contraseña debe tener al menos 6 caracteres."
                return@setOnClickListener
            }

            // --- REGISTRO EN FIREBASE ---
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Si el registro de autenticación es exitoso, crea un documento para el jugador en Firestore
                        val userId = auth.currentUser?.uid ?: ""
                        val playerDocument = hashMapOf(
                            "ducks" to 0,
                            "email" to email
                        )
                        db.collection("players").document(userId)
                            .set(playerDocument)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
                                // Navega a la pantalla principal del juego
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra(EXTRA_LOGIN, email)
                                // Limpia la pila de actividades para que el usuario no pueda volver al registro o login
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        // Si el registro falla (ej. el email ya existe)
                        Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Lógica para el botón "Regresar a Login"
        backToLoginButton.setOnClickListener {
            // Simplemente cierra esta actividad para volver a la anterior (LoginActivity)
            finish()
        }
    }
}