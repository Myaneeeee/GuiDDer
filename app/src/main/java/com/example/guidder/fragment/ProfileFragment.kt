package com.example.guidder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.guidder.HomeActivity
import com.example.guidder.R
import com.example.guidder.database.DatabaseHelper
import com.example.guidder.databinding.ActivityHomeBinding
import com.example.guidder.databinding.FragmentProfileBinding
import com.example.guidder.session.SessionManager

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        sessionManager = SessionManager(requireContext())

        binding.updateBtn.setOnClickListener {
            val newUsername = binding.usernameET.text.toString()
            if (newUsername.isNotEmpty()) {
                databaseHelper.updateUsername(sessionManager.getUserID(), newUsername)

                sessionManager.updateUserName(newUsername)
                (requireActivity() as? HomeActivity)?.updateWelcomeText(sessionManager.getUserName().toString())

                binding.usernameET.text.clear()
                Toast.makeText(requireContext(), "Username successfully changed!", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "New username can not be empty!", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}