package com.example.uni6tarea4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uni6tarea4.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_USER_EMAIL = "extra_user_email"
        const val EXTRA_USER_PHONE = "extra_user_phone"
        const val EXTRA_USER_WEBSITE = "extra_user_website"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.detail_title)
            setDisplayHomeAsUpEnabled(true)
        }

        // Recibir datos via Bundle/Intent
        val userId = intent.getIntExtra(EXTRA_USER_ID, -1)
        val userName = intent.getStringExtra(EXTRA_USER_NAME) ?: ""
        val userEmail = intent.getStringExtra(EXTRA_USER_EMAIL) ?: ""
        val userPhone = intent.getStringExtra(EXTRA_USER_PHONE) ?: ""
        val userWebsite = intent.getStringExtra(EXTRA_USER_WEBSITE) ?: ""

        // Pasar argumentos al Fragment via Bundle
        if (savedInstanceState == null) {
            val fragment = UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(UserDetailFragment.ARG_USER_ID, userId)
                    putString(UserDetailFragment.ARG_USER_NAME, userName)
                    putString(UserDetailFragment.ARG_USER_EMAIL, userEmail)
                    putString(UserDetailFragment.ARG_USER_PHONE, userPhone)
                    putString(UserDetailFragment.ARG_USER_WEBSITE, userWebsite)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
