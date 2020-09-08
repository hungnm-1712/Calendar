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
import com.example.calendar.model.DayMonthYearModel
import kotlinx.android.synthetic.main.dialog_chose_day.view.*
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_day_detail.*
import java.util.*

class DayFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val TAG = " TAG Day fragment"

    lateinit var dmy: DayMonthYearModel
    lateinit var calendar: Calendar
    var alertDialog: AlertDialog? = null

    var listDay = arrayListOf<Int>()
    var listMonth = arrayListOf<String>()
    var listYear = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = Calendar.getInstance()

        dmy = DayMonthYearModel(
            calendar?.get(Calendar.DAY_OF_MONTH),
            calendar?.get(Calendar.MONTH) + 1,
            calendar?.get(Calendar.YEAR)
        )


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

                show.spnDay.setSelection(dmy.dd - 1)
                show.spnMonth.setSelection(dmy.mm - 1)
                val spinnerPosition: Int = adapterYear.getPosition(dmy.yyyy)
                show.spnYear.setSelection(spinnerPosition)

                /** ChoseDay */

                show.btnChoseDay.setOnClickListener {
//                    dd = show.spnDay.selectedItem.toString().toInt()
//                    mm = show.spnMonth.selectedItemPosition + 1
//                    yyyy = show.spnYear.selectedItem.toString().toInt()
                    dmy = DayMonthYearModel(
                        show.spnDay.selectedItem.toString().toInt(),
                        show.spnMonth.selectedItemPosition + 1,
                        show.spnYear.selectedItem.toString().toInt()
                    )

                    Log.d(TAG, "$dmy")
                    setTextToLayout()
//                    viewPagerDayDetail()

                    // Đếm khoảng cách giữa ngày chọn và ngày hiện tại
                    var dem =
                        CaculateDate.getJdFromDate(
                            DayMonthYearModel(
                                calendar?.get(Calendar.DAY_OF_MONTH),
                                calendar?.get(Calendar.MONTH) + 1,
                                calendar?.get(Calendar.YEAR)
                            )
                        ) -
                                CaculateDate.getJdFromDate(dmy)
                    vpDayDetail.setCurrentItem(Int.MAX_VALUE / 2 - dem)
                    alertDialog?.dismiss()
                }

                show.btnToday.setOnClickListener {

                    dmy = DayMonthYearModel(
                        calendar?.get(Calendar.DAY_OF_MONTH),
                        calendar?.get(Calendar.MONTH) + 1,
                        calendar?.get(Calendar.YEAR)
                    )
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
//                        dd = calendar1.get(Calendar.DAY_OF_MONTH)
//                        mm = calendar1.get(Calendar.MONTH) + 1
//                        yyyy = calendar1.get(Calendar.YEAR)
                        val dayOfWeek = calendar1[Calendar.DAY_OF_WEEK]
                        dmy = DayMonthYearModel(
                            calendar1.get(Calendar.DAY_OF_MONTH),
                            calendar1.get(Calendar.MONTH) + 1,
                            calendar1.get(Calendar.YEAR)
                        )

                        Log.d(
                            TAG,
                            "$dmy + ${CaculateDate.getThu(dmy)}"
                        )
                        tvNgayDuong.text = dmy.dd.toString()
                        setTextToLayout()


                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    fun setTextToLayout() {
        if (dmy == DayMonthYearModel(
                calendar?.get(Calendar.DAY_OF_MONTH),
                calendar?.get(Calendar.MONTH) + 1,
                calendar?.get(Calendar.YEAR)
            )
        ) {
            tvChoseDay.text = "Hôm nay"
        } else {
            tvChoseDay.text = "Ngày ${dmy.dd}"
        }

        tvThangDuong.text = "Tháng ${dmy.mm}"
        tvNamDuong.text = dmy.yyyy.toString()
        var dmyam = CaculateDate.convertSolar2Lunar(dmy)
        if (dmyam.mm < 10) {
            tvThangNamAm.text =
                "0${dmyam.mm}/ ${dmyam.yyyy}"
        } else {
            tvThangNamAm.text =
                "${dmyam.mm}/ ${dmyam.yyyy}"
        }

        tvNgayAm.text = "${dmyam.dd}"

        tvCanChiNam.text = "${CaculateDate.getCanNam(dmy)} ${CaculateDate.getChiNam(dmy)}"

        tvCanChiThang.text =
            "${CaculateDate.getCanThang(dmyam)} ${CaculateDate.getChiThang(dmyam)}"

        tvCanChiNgay.text =
            "${CaculateDate.getCanNgay(dmy)} ${CaculateDate.getChiNgay(dmy)}"

        var textGioHoangDao =StringBuilder()
        for(i in 0 .. CaculateDate.getGioHoangDao(dmy)!!.size-1){
            textGioHoangDao.append(CaculateDate.getGioHoangDao(dmy)!!.get(i))
            if(i!=CaculateDate.getGioHoangDao(dmy)!!.size-1){
                textGioHoangDao.append(", ")
            }
        }

        tvGioHoangDao.text = textGioHoangDao

        tvChiGio.text = CaculateDate.getChiGio(calendar?.get(Calendar.HOUR_OF_DAY))
        Log.d(TAG, "Hour: ${calendar?.get(Calendar.HOUR_OF_DAY)}")

    }

}
