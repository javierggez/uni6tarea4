package com.example.uni6tarea4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uni6tarea4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = getString(R.string.users_title)

            setupViewModel()
            setupRecyclerView()
            setupObservers()
            setupEventListeners()

            viewModel.fetchUsers()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error al iniciar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart: actividad visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: actividad en primer plano")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause: actividad pierde el foco")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop: actividad no visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: actividad destruida")
    }

    private fun setupViewModel() {
        val database = AppDatabase.getInstance(this)
        val repository = UserRepository(RetrofitClient.apiService, database.userDao())
        val factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(onItemClick = { user ->
            try {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_USER_ID, user.id)
                    putExtra(DetailActivity.EXTRA_USER_NAME, user.name)
                    putExtra(DetailActivity.EXTRA_USER_EMAIL, user.email)
                    putExtra(DetailActivity.EXTRA_USER_PHONE, user.phone)
                    putExtra(DetailActivity.EXTRA_USER_WEBSITE, user.website)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error opening DetailActivity", e)
                Toast.makeText(this, "Error al abrir detalles", Toast.LENGTH_SHORT).show()
            }
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.users.observe(this) { users ->
            adapter.updateUsers(users)
            binding.tvEmpty.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupEventListeners() {
        binding.fab.setOnClickListener {
            try {
                // Intentamos abrir FormActivity explícitamente usando su clase
                val intent = Intent(this, FormActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error opening FormActivity", e)
                Toast.makeText(this, "No se pudo abrir el formulario", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.fetchUsers()
        }
    }
}
