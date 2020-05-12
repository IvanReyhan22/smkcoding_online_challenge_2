package com.ezyindustries.conews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.ezyindustries.conews.Adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val textMenu = arrayOf("Home", "Search", "Profile")
    val iconMenu = arrayOf(R.drawable.ic_sofa, R.drawable.ic_search, R.drawable.ic_social)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewProps()

        val adapter = ViewPagerAdapter(this)
        home_view_pager.setAdapter(adapter)

        TabLayoutMediator(tab_layout, home_view_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->

            tab.text = textMenu[position]

            tab.icon = ResourcesCompat.getDrawable(resources, iconMenu[position], null)

        }).attach()
    }

    private fun ViewProps() {
        homeContainer.setBackgroundColor(getResources().getColor(R.color.Background))
    }
}
