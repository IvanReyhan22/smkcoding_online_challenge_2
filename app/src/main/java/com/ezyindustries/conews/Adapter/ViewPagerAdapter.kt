package com.ezyindustries.conews.Adapter

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ezyindustries.conews.ExploreFragment
import com.ezyindustries.conews.HomeFragment
import com.ezyindustries.conews.ProfileFragment

class ViewPagerAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val MENU_COUNT = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> { return HomeFragment() }
            1 -> { return ExploreFragment() }
            2 -> { return ProfileFragment() }
            else -> {
                return HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return  MENU_COUNT
    }


}