package com.example.calendar.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.calendar.fragment.DayDetailFragment

class ViewPagerDayDetailAdapter(
    private val mContext: Context,
    fm: FragmentManager
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return  DayDetailFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return  Integer.MAX_VALUE;
    }

}