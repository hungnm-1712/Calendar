package com.example.calendar.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendar.R
import com.example.calendar.instance.CaculateDate
import com.example.calendar.model.DayMonthYearModel
import kotlinx.android.synthetic.main.fragment_day_detail.*
import java.util.*

private const val ARG_POS = "pos"


class DayDetailFragment : Fragment() {

    val TAG = "TagDayDetailFragment"
    private var position: Int? = null


    companion object {
        fun newInstance(pos: Int) =
            DayDetailFragment().apply {
                position = pos - Int.MAX_VALUE / 2
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_detail, container, false)
    }


    lateinit var dmy : DayMonthYearModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            position?.let {
                Log.d(TAG,"Position $position")
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, it);

               dmy = DayMonthYearModel(calendar?.get(Calendar.DAY_OF_MONTH)!!,calendar?.get(Calendar.MONTH)!! + 1,calendar?.get(Calendar.YEAR)!!)
                val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]


                Log.d(TAG,"$dmy+ ${CaculateDate.getThu(dmy)}")
                tvNgayDuong.text = dmy.dd.toString()
                tvThu.text = "${CaculateDate.getThu(dmy)}"

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onViewCreated(view, savedInstanceState)
    }


}
