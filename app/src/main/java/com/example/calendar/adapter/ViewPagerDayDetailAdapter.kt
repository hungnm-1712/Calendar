package com.example.calendar.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.calendar.fragment.DayDetailFragment
import com.example.calendar.fragment.DayFragment
import com.example.calendar.fragment.MonthFragment

class ViewPagerDayDetailAdapter(
    private val mContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int,
    var dd: Int, var mm: Int, var yyyy: Int
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return  DayDetailFragment.newInstance(dd, mm, yyyy)
    }

    override fun getCount(): Int {
        return totalTabs
    }

}