package com.example.uni6tarea4

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uni6tarea4.databinding.ActivityFormBinding

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.form_title)
            setDisplayHomeAsUpEnabled(true)
        }

        setupEventListeners()
    }

    private fun setupEventListeners() {
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                submitForm()
            }
        }

        binding.btnClear.setOnClickListener {
            clearForm()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        // Validación usando let
        name.let {
            if (it.isBlank()) {
                binding.tilName.error = getString(R.string.error_field_required)
                isValid = false
            } else {
                binding.tilName.error = null
            }
        }

        email.let {
            if (it.isBlank()) {
                binding.tilEmail.error = getString(R.string.error_field_required)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                binding.tilEmail.error = getString(R.string.error_email_invalid)
                isValid = false
            } else {
                binding.tilEmail.error = null
            }
        }

        message.let {
            if (it.isBlank()) {
                binding.tilMessage.error = getString(R.string.error_field_required)
                isValid = false
            } else {
                binding.tilMessage.error = null
            }
        }

        return isValid
    }

    private fun submitForm() {
        val formData = mapOf(
            "name" to binding.etName.text.toString().trim(),
            "email" to binding.etEmail.text.toString().trim(),
            "message" to binding.etMessage.text.toString().trim()
        )

        // Usar map/filter para procesar los datos del formulario
        val validData = formData.filter { it.value.isNotBlank() }
        val summary = validData.map { "${it.key}: ${it.value}" }.joinToString("\n")

        Toast.makeText(this, getString(R.string.form_success), Toast.LENGTH_LONG).show()
        clearForm()
        finish()
    }

    private fun clearForm() {
        binding.apply {
            etName.text?.clear()
            etEmail.text?.clear()
            etMessage.text?.clear()
            tilName.error = null
            tilEmail.error = null
            tilMessage.error = null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
