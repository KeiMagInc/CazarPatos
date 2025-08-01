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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordRegister)
        val registerButton = findViewById<Button>(R.id.buttonRegisterUser)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                emailEditText.error = "Se requiere un correo electrónico"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Se requiere una contraseña"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid
                            if (userId != null) {
                            val playerDocument = hashMapOf(
                                "ducks" to 0,
                                "email" to email
                            )
                            db.collection("players").document(userId)
                                .set(playerDocument)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra(EXTRA_LOGIN, email)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext, "Error al guardar datos del jugador: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}