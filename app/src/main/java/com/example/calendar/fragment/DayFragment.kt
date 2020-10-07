package com.example.calendar.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.calendar.Interface.MonthCalendarInterface
import com.example.calendar.R
import com.example.calendar.adapter.ViewPagerDayDetailAdapter
import com.example.calendar.instance.CaculateDate
import com.example.calendar.instance.DayMonthYearSelected.dayMonthYearSelected
import com.example.calendar.model.DayMonthYearModel
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_day_detail.*
import java.util.*


class DayFragment : Fragment() {
    val TAG = " TagDayFragment"

    //lateinit var dmy: DayMonthYearModel
    lateinit var calendar: Calendar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = Calendar.getInstance()
        dayMonthYearSelected = DayMonthYearModel(
            calendar?.get(Calendar.DAY_OF_MONTH),
            calendar?.get(Calendar.MONTH) + 1,
            calendar?.get(Calendar.YEAR)
        )

        return inflater.inflate(R.layout.fragment_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        openDatePickerDialog()
        viewPagerDayDetail()
        setTextToLayout()

    }


    fun openDatePickerDialog() {
        tvChoseDay.setOnClickListener {
            val cal = Calendar.getInstance()
            var datePickerDialog = DatePickerDialog(
                context!!, android.R.style.Theme_Holo_Light_Dialog_MinWidth, { view, yyyy, mm, dd ->
                    dayMonthYearSelected = DayMonthYearModel(dd, mm + 1, yyyy)
                    setTextToLayout()

                    // Dem khoang cach ngay`
                    var dem =
                        CaculateDate.getJdFromDate(
                            DayMonthYearModel(
                                calendar?.get(Calendar.DAY_OF_MONTH),
                                calendar?.get(Calendar.MONTH) + 1,
                                calendar?.get(Calendar.YEAR)
                            )
                        ) -
                                CaculateDate.getJdFromDate(dayMonthYearSelected)
                    vpDayDetail.setCurrentItem(Int.MAX_VALUE / 2 - dem)
                }, dayMonthYearSelected.yyyy, dayMonthYearSelected.mm - 1, dayMonthYearSelected.dd
            )
            datePickerDialog.show()

        }

    }

    fun viewPagerDayDetail() {

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
                    var pos = position - Int.MAX_VALUE / 2
                    pos?.let {
                        val calendar1 = Calendar.getInstance()
                        calendar1.add(Calendar.DATE, it);
                        val dayOfWeek = calendar1[Calendar.DAY_OF_WEEK]
                        dayMonthYearSelected = DayMonthYearModel(
                            calendar1.get(Calendar.DAY_OF_MONTH),
                            calendar1.get(Calendar.MONTH) + 1,
                            calendar1.get(Calendar.YEAR)
                        )
                        tvNgayDuong.text = dayMonthYearSelected.dd.toString()
                        setTextToLayout()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    fun setTextToLayout() {
        //  hôm nay or ngày
        if (dayMonthYearSelected == DayMonthYearModel(
                calendar?.get(Calendar.DAY_OF_MONTH),
                calendar?.get(Calendar.MONTH) + 1,
                calendar?.get(Calendar.YEAR)
            )
        ) {
            tvChoseDay.text = "Hôm nay"
        } else {
            tvChoseDay.text = "Ngày ${dayMonthYearSelected.dd}"
        }

        // tháng năm dương
        tvThangDuong.text = "Tháng ${dayMonthYearSelected.mm}"
        tvNamDuong.text = dayMonthYearSelected.yyyy.toString()

        // âm
        var dmyam = CaculateDate.convertSolar2Lunar(dayMonthYearSelected)
        tvThangNamAm.text = "${dmyam.mm}/ ${dmyam.yyyy}"
        tvNgayAm.text = "${dmyam.dd}"

        // Can chi
        tvCanChiNgay.text =
            "${CaculateDate.getCanNgay(dayMonthYearSelected)} ${
                CaculateDate.getChiNgay(
                    dayMonthYearSelected
                )
            }"
        tvCanChiThang.text =
            "${CaculateDate.getCanThang(dmyam)} ${CaculateDate.getChiThang(dmyam)}"
        tvCanChiNam.text = "${CaculateDate.getCanNam(dmyam)} ${CaculateDate.getChiNam(dmyam)}"

        // Gio hoang dao
        var textGioHoangDao = StringBuilder()
        for (i in 0..CaculateDate.getGioHoangDao(dayMonthYearSelected)!!.size - 1) {
            textGioHoangDao.append(CaculateDate.getGioHoangDao(dayMonthYearSelected)!!.get(i))
            if (i != CaculateDate.getGioHoangDao(dayMonthYearSelected)!!.size - 1) {
                textGioHoangDao.append(", ")
            }
        }
        tvGioHoangDao.text = textGioHoangDao
        tvChiGio.text = CaculateDate.getChiGio(calendar?.get(Calendar.HOUR_OF_DAY))

    }

}
