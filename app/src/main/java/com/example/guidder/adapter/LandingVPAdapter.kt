package com.example.guidder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.guidder.fragment.LandingSlide1
import com.example.guidder.fragment.LandingSlide2
import com.example.guidder.fragment.LandingSlide3
import com.example.guidder.fragment.LandingSlide4

class LandingVPAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LandingSlide1()
            1 -> LandingSlide2()
            2 -> LandingSlide3()
            3 -> LandingSlide4()
            else -> LandingSlide1()
        }
    }
}