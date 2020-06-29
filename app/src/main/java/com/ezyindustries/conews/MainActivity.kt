package com.ezyindustries.conews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ezyindustries.conews.Adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val textMenu = arrayOf("Home", "Search", "Write", "Profile")
    val iconMenu = arrayOf(R.drawable.ic_off_sofa, R.drawable.ic_off_search,R.drawable.ic_off_add, R.drawable.ic_off_social)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Change background color
        ViewProps()

        var TabPosition = 0
        val extras = intent.extras

        if (extras != null){
            TabPosition = extras!!.getInt("viewpager_position")
        }

        val adapter = ViewPagerAdapter(this)
        home_view_pager.setAdapter(adapter)

        TabLayoutMediator(tab_layout, home_view_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->

            tab.text = textMenu[position]

        }).attach()

        val onIcons: Array<Int> = arrayOf(
            R.drawable.ic_on_sofa,
            R.drawable.ic_on_search,
            R.drawable.ic_on_add,
            R.drawable.ic_on_social
        )

        val offIcons: Array<Int> = arrayOf(
            R.drawable.ic_off_sofa,
            R.drawable.ic_off_search,
            R.drawable.ic_off_add,
            R.drawable.ic_off_social
        )

        //Set on icon
        for (i in onIcons.indices) {
            tab_layout.getTabAt(i)!!.icon = ResourcesCompat.getDrawable(resources, onIcons[0], null)
        }

        //Set off icon
        tab_layout.getTabAt(1)!!.icon = ResourcesCompat.getDrawable(resources, offIcons[1], null)
        tab_layout.getTabAt(2)!!.icon = ResourcesCompat.getDrawable(resources, offIcons[2], null)
        tab_layout.getTabAt(3)!!.icon = ResourcesCompat.getDrawable(resources, offIcons[3], null)

        //Set text on icon
        tab_layout.setTabTextColors(getResources().getColor(R.color.DarkGray), getResources().getColor(R.color.Primary))

        tab_layout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.icon = ResourcesCompat.getDrawable(resources, offIcons[tab.position], null)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.icon = ResourcesCompat.getDrawable(resources, onIcons[tab.position], null)
            }

        })

        home_view_pager.setCurrentItem(TabPosition)
    }

    private fun ViewProps() {
        homeContainer.setBackgroundColor(getResources().getColor(R.color.Background))
    }
}
