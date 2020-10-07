package com.example.calendar.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.calendar.fragment.DayFragment
import com.example.calendar.fragment.MonthFragment

class ViewPagerMainAdapter(val mContext: Context, fm: FragmentManager, var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                var dayFragment = DayFragment()
                return dayFragment
            }
            1 -> return MonthFragment()
        }
        return DayFragment()
    }

    override fun getCount(): Int {
        return totalTabs
    }

}