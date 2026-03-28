package com.example.uni6tarea4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uni6tarea4.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_USER_ID = "arg_user_id"
        const val ARG_USER_NAME = "arg_user_name"
        const val ARG_USER_EMAIL = "arg_user_email"
        const val ARG_USER_PHONE = "arg_user_phone"
        const val ARG_USER_WEBSITE = "arg_user_website"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getInt(ARG_USER_ID) ?: -1
        val name = arguments?.getString(ARG_USER_NAME) ?: ""
        val email = arguments?.getString(ARG_USER_EMAIL) ?: ""
        val phone = arguments?.getString(ARG_USER_PHONE) ?: ""
        val website = arguments?.getString(ARG_USER_WEBSITE) ?: ""

        binding.apply {
            tvDetailId.text = "ID: $userId"
            tvDetailName.text = name
            tvDetailEmail.text = email
            tvDetailPhone.text = phone.let { if (it.isBlank()) "No disponible" else it }
            tvDetailWebsite.text = website.let { if (it.isBlank()) "No disponible" else it }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
