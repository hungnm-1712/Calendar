package com.example.calendar.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.calendar.R
import com.example.calendar.adapter.ViewPagerDayDetailAdapter
import com.example.calendar.instance.CaculateDate
import kotlinx.android.synthetic.main.dialog_chose_day.view.*
import kotlinx.android.synthetic.main.fragment_day.*
import java.util.*

class DayFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                /**  Add data to spinner*/

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

                /** Selection on spinner*/

                show.spnDay.setSelection(dd - 1)
                show.spnMonth.setSelection(mm - 1)
                val spinnerPosition: Int = adapterYear.getPosition(yyyy)
                show.spnYear.setSelection(spinnerPosition)

                /** ChoseDay */

                show.btnChoseDay.setOnClickListener {
                    dd = show.spnDay.selectedItem.toString().toInt()
                    mm = show.spnMonth.selectedItemPosition + 1
                    yyyy = show.spnYear.selectedItem.toString().toInt()
                    Log.d(TAG, "$dd $mm $yyyy")
                    setTextToLayout(dd, mm, yyyy)
//                    viewPagerDayDetail()
                    alertDialog?.dismiss()
                }

                show.btnToday.setOnClickListener {
                    dd = calendar?.get(Calendar.DAY_OF_MONTH)!!
                    mm = calendar?.get(Calendar.MONTH)!! + 1
                    yyyy = calendar?.get(Calendar.YEAR)!!
                    setTextToLayout(dd, mm, yyyy)
                    vpDayDetail.adapter?.notifyDataSetChanged()
                    alertDialog?.dismiss()
                }
                builder.setView(show)
                alertDialog = builder.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun viewPagerDayDetail() {

        var adapterDetailDayViewPager =
            ViewPagerDayDetailAdapter(context!!, fragmentManager!!)
        vpDayDetail.adapter = adapterDetailDayViewPager
        vpDayDetail.currentItem = Int.MAX_VALUE/2
//        (vpDayDetail.adapter as ViewPagerDayDetailAdapter).notifyDataSetChanged()

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
        tvThangDuong.text = "Tháng $mm"
        tvNamDuong.text = yyyy.toString()

        var thangAm = "${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(1)}"
        if (CaculateDate.convertSolar2Lunar(day, month, year)!!.get(1) < 10) {
            thangAm = "0${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(1)}"
        }
        tvThangNamAm.text =
            "$thangAm/ ${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(2)}"
        tvNgayAm.text = "${CaculateDate.convertSolar2Lunar(day, month, year)!!.get(0)}"

        tvCanChiNam.text = "${CaculateDate.getCanNam(year)} ${CaculateDate.getChiNam(year)}"
        tvCanChiThang.text =
            "${CaculateDate.getCanThang(month, year)} ${CaculateDate.getChiThang(month)}"
        tvCanChiNgay.text =
            "${CaculateDate.getCanNgay(day, month, year)} ${CaculateDate.getChiNgay(
                day,
                month,
                year
            )}"

//        tvGioHoangDao.text = SimpleDateFormat("HH:mm").format(calendar?.time)
        tvChiGio.text = CaculateDate.getChiGio(calendar?.get(Calendar.HOUR_OF_DAY)!!)
        Log.d(TAG, "Hour: ${calendar?.get(Calendar.HOUR_OF_DAY)}")

    }

}
