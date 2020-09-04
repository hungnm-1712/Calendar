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
import com.example.calendar.instance.ConvertDate
import kotlinx.android.synthetic.main.dialog_chose_day.view.*
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_day_detail.*
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
        setTextToLayout()

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
                    var ddtoday = calendar?.get(Calendar.DAY_OF_MONTH)!!
                    var mmtoday = calendar?.get(Calendar.MONTH)!! + 1
                    var yyyytoday = calendar?.get(Calendar.YEAR)!!
                    Log.d(TAG, "$dd $mm $yyyy")
                    setTextToLayout()
//                    viewPagerDayDetail()
                    var dem =
                        ConvertDate.getJdFromDate(ddtoday, mmtoday, yyyytoday) -
                                ConvertDate.getJdFromDate(dd, mm, yyyy)
                    vpDayDetail.setCurrentItem(Int.MAX_VALUE/2 -dem)
                    alertDialog?.dismiss()
                }

                show.btnToday.setOnClickListener {
                    dd = calendar?.get(Calendar.DAY_OF_MONTH)!!
                    mm = calendar?.get(Calendar.MONTH)!! + 1
                    yyyy = calendar?.get(Calendar.YEAR)!!
                    setTextToLayout()
                    vpDayDetail.currentItem = Int.MAX_VALUE / 2
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
        vpDayDetail.currentItem = Int.MAX_VALUE / 2


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
                try {
                    Log.d(TAG, "----------- Position $position ------------")
                    var pos = position - Int.MAX_VALUE / 2
                    Log.d(TAG, "pos $pos")
                    pos?.let {
                        val calendar1 = Calendar.getInstance()
                        calendar1.add(Calendar.DATE, it);
                        dd = calendar1.get(Calendar.DAY_OF_MONTH)!!
                        mm = calendar1.get(Calendar.MONTH)!! + 1
                        yyyy = calendar1.get(Calendar.YEAR)!!
                        val dayOfWeek = calendar1[Calendar.DAY_OF_WEEK]
                        Log.d(
                            TAG,
                            "ngay $dd thang $mm nam $yyyy + ${ConvertDate.getThu(dd, mm, yyyy)}"
                        )
                        tvNgayDuong.text = dd.toString()
                        tvThu.text = "${ConvertDate.getThu(dd, mm, yyyy)}"
                        setTextToLayout()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    fun setTextToLayout() {


        if (dd == calendar?.get(Calendar.DAY_OF_MONTH)!!) {
            tvChoseDay.text = "Hôm nay"
        } else {
            tvChoseDay.text = "Ngày $dd"
        }


        tvThangDuong.text = "Tháng $mm"
        tvNamDuong.text = yyyy.toString()

        var ngayAm = ConvertDate.convertSolar2Lunar(dd, mm, yyyy)!!.get(0)
        var thangAm = ConvertDate.convertSolar2Lunar(dd, mm, yyyy)!!.get(1)
        var namAm = ConvertDate.convertSolar2Lunar(dd, mm, yyyy)!!.get(2)

        if (thangAm < 10) {
            tvThangNamAm.text =
                "0$thangAm/ $namAm"
        } else {
            tvThangNamAm.text =
                "$thangAm/ $namAm"
        }

        tvNgayAm.text = "${ConvertDate.convertSolar2Lunar(dd, mm, yyyy)!!.get(0)}"

        tvCanChiNam.text = "${ConvertDate.getCanNam(yyyy)} ${ConvertDate.getChiNam(yyyy)}"

        tvCanChiThang.text =
            "${ConvertDate.getCanThang(thangAm, namAm)} ${ConvertDate.getChiThang(thangAm)}"

        tvCanChiNgay.text =
            "${ConvertDate.getCanNgay(dd, mm, yyyy)} ${ConvertDate.getChiNgay(dd, mm, yyyy)}"

//        tvGioHoangDao.text = SimpleDateFormat("HH:mm").format(calendar?.time)
        tvChiGio.text = ConvertDate.getChiGio(calendar?.get(Calendar.HOUR_OF_DAY)!!)
        Log.d(TAG, "Hour: ${calendar?.get(Calendar.HOUR_OF_DAY)}")

    }

}
