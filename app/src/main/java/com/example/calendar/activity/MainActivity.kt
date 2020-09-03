package com.example.calendar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calendar.R
import com.example.calendar.adapter.ViewPagerMainAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.addTab(tabLayout.newTab().setText("Ngày"))
        tabLayout.addTab(tabLayout.newTab().setText("Tháng"))

        tabLayout.getTabAt(0)?.setIcon(R.drawable.calendar_day)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.calendar_month)
        var viewPagerAdapter =
            ViewPagerMainAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = viewPagerAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

        })

    }
}
