package com.example.guidder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragmentList = mutableListOf<Fragment>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}