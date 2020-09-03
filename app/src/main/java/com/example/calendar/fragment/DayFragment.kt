package com.example.calendar.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.calendar.R
import com.example.calendar.adapter.ViewPagerDayDetailAdapter
import com.example.calendar.instance.CaculateDate
import kotlinx.android.synthetic.main.dialog_chose_day.*
import kotlinx.android.synthetic.main.dialog_chose_day.view.*
import kotlinx.android.synthetic.main.fragment_day.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val TAG = " TAG Day fragment"
    var dd = 0
    var mm = 0
    var yyyy = 0
    var calendar: Calendar? = null
    var alertDialog: AlertDialog? = null

    var listDay = arrayListOf<Int>()
    var listMonth = arrayListOf<String>()
    var listYear = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = Calendar.getInstance()
        dd = calendar?.get(Calendar.DAY_OF_MONTH)!!
        mm = calendar?.get(Calendar.MONTH)!! + 1
        yyyy = calendar?.get(Calendar.YEAR)!!

        for (i in 1900..2100) {
            listYear.add(i)
        }
        for (i in 1..12) {
            listMonth.add("Tháng $i")
        }
        for (i in 1..31) {
            listDay.add(i)
        }
        return inflater.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        openSpinnerChoseDay()
        viewPagerDayDetail()
        setTextToLayout(dd, mm, yyyy)

    }

    private fun openSpinnerChoseDay() {
        tvChoseDay.setOnClickListener {
            try {
                var builder = AlertDialog.Builder(context)
                var show = LayoutInflater.from(context).inflate(R.layout.dialog_chose_day, null)
                var adapterDay =
                    ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listDay)
                adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                show.spnDay?.adapter = adapterDay
                var adapterMonth =
                    ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listMonth)
                adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                show.spnMonth?.adapter = adapterMonth
                var adapterYear =
                    ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listYear)
                adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                show.spnYear?.adapter = adapterYear


                builder.setView(show)
                alertDialog = builder.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun viewPagerDayDetail() {
        var adapterDetailDayViewPager =
            ViewPagerDayDetailAdapter(context!!, fragmentManager!!, 3, dd, mm, yyyy)
        vpDayDetail.adapter = adapterDetailDayViewPager

        vpDayDetail.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }


    fun setTextToLayout(day: Int, month: Int, year: Int) {
        //tvTime.text = SimpleDateFormat("HH:mm").format(calendar?.time)
        tvThangDuong.text = "Tháng $mm"
        tvNamDuong.text = yyyy.toString()


        tvThangNamAm.text = "${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(1)}/ ${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(2)}"
        tvNgayAm.text = "${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(0)}"

        tvCanChiNam.text = "Năm ${CaculateDate.getCanNam(year)} ${CaculateDate.getChiNam(year)}"
        tvCanChiThang.text =
            "Tháng ${CaculateDate.getCanThang(month, year)} ${CaculateDate.getChiThang(month)}"
        tvCanChiNgay.text =
            "Ngày ${CaculateDate.getCanNgay(day, month, year)} ${CaculateDate.getChiNgay(
                day,
                month,
                year
            )}"

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
