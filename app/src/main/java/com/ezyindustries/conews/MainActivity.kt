package com.ezyindustries.conews

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.ezyindustries.conews.Adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Typeface
import android.widget.TextView
import android.view.ViewGroup
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {

    val textMenu = arrayOf("Home", "Search", "Profile")
    val iconMenu = arrayOf(R.drawable.ic_off_sofa, R.drawable.ic_off_search, R.drawable.ic_off_social)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewProps()

        val adapter = ViewPagerAdapter(this)
        home_view_pager.setAdapter(adapter)

        TabLayoutMediator(tab_layout, home_view_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->

            tab.text = textMenu[position]

//            tab.icon = ResourcesCompat.getDrawable(resources, iconMenu[position], null)

        }).attach()

        val onIcons: Array<Int> = arrayOf(
            R.drawable.ic_on_sofa,
            R.drawable.ic_on_search,
            R.drawable.ic_on_social
        )

        val offIcons: Array<Int> = arrayOf(
            R.drawable.ic_off_sofa,
            R.drawable.ic_off_search,
            R.drawable.ic_off_social
        )

        for (i in onIcons.indices) {
            tab_layout.getTabAt(i)!!.icon = ResourcesCompat.getDrawable(resources, onIcons[0], null)
        }

        tab_layout.getTabAt(1)!!.icon = ResourcesCompat.getDrawable(resources, offIcons[1], null)
        tab_layout.getTabAt(2)!!.icon = ResourcesCompat.getDrawable(resources, offIcons[2], null)

//        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Primary))
        tab_layout.setTabTextColors(getResources().getColor(R.color.DarkGray), getResources().getColor(R.color.Primary))

        tab_layout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                val tabLayout = (tab_layout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
//                val tabTextView = tabLayout.getChildAt(1) as TextView
//                tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                tab!!.icon = applicationContext.getDrawable( offIcons [tab.position] )
                tab!!.icon = ResourcesCompat.getDrawable(resources, offIcons[tab.position], null)
//                val tabLayout = (tab_layout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
//                val tabTextView = tabLayout.getChildAt(1) as TextView
//                tabTextView.setTypeface(tabTextView.typeface, Typeface.NORMAL)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.icon = ResourcesCompat.getDrawable(resources, onIcons[tab.position], null)
            }

        })
    }

    private fun ViewProps() {
        homeContainer.setBackgroundColor(getResources().getColor(R.color.Background))
    }
}
