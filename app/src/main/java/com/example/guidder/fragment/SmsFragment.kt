package com.example.guidder.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.guidder.R
import com.example.guidder.databinding.FragmentSmsBinding
import com.example.guidder.service.BackgroundService

class SmsFragment : Fragment() {
    private lateinit var binding: FragmentSmsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsBinding.inflate(layoutInflater, container, false)

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.RECEIVE_SMS), 100)
        }

        binding.sendBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberET.text.toString()
            val message = binding.messageET.text.toString()
            sendSms(phoneNumber, message)
        }

        binding.startServiceBtn.setOnClickListener {
            val intent = Intent(requireContext(), BackgroundService::class.java)
            requireContext().startService(intent)
        }

        return binding.root
    }

    fun sendSms(phoneNumber: String, message: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS), 101)
        }
        else {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }
    }
}