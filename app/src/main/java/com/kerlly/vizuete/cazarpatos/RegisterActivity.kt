package com.kerlly.vizuete.cazarpatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailEditText = findViewById(R.id.editTextEmailRegister)
        passwordEditText = findViewById(R.id.editTextPasswordRegister)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        val backToLoginButton = findViewById<Button>(R.id.buttonBackToLogin)

        signUpButton.setOnClickListener {
            validateAndRegisterUser()
        }
        backToLoginButton.setOnClickListener {
            finish()
        }
    }

    private fun validateAndRegisterUser() {
        emailEditText.error = null
        passwordEditText.error = null
        confirmPasswordEditText.error = null

        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText.error = getString(R.string.error_email_required)
            emailEditText.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = getString(R.string.error_invalid_email)
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = getString(R.string.error_password_required)
            passwordEditText.requestFocus()
            return
        }

        if (password.length < 8) {
            passwordEditText.error = getString(R.string.error_password_length)
            passwordEditText.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = getString(R.string.error_password_required)
            confirmPasswordEditText.requestFocus()
            return
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = getString(R.string.error_password_mismatch)
            confirmPasswordEditText.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val playerDocument = hashMapOf(
                        "ducks" to 0,
                        "email" to email
                    )
                    db.collection("players").document(userId)
                        .set(playerDocument)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(EXTRA_LOGIN, email)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}