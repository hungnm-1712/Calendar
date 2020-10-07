package com.example.calendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.calendar.R;
import com.example.calendar.instance.CaculateDate;
import com.example.calendar.Interface.MonthCalendarInterface;
import com.example.calendar.instance.DayMonthYearSelected;
import com.example.calendar.model.DayMonthYearModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.calendar.instance.DayMonthYearSelected.dayMonthYearSelected;

public class CalendarAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private static final String TAG = "TagAdapter";
    MonthCalendarInterface iMonthCalendar;

    public CalendarAdapter(Context context, ArrayList<Date> days, MonthCalendarInterface iMonthCalendar) {
        super(context, R.layout.calendar_item, days);
        inflater = LayoutInflater.from(context);
        this.iMonthCalendar = iMonthCalendar;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null)
            view = inflater.inflate(R.layout.calendar_item, parent, false);
        TextView tvDate = view.findViewById(R.id.tvDayOfMonth);
        TextView tvDateLunar = view.findViewById(R.id.tvDayLunar);
        CardView cardView = view.findViewById(R.id.cardview);

        // day in question
        final Calendar calendar = Calendar.getInstance();
        final Date date = getItem(position);
        calendar.setTime(date);
        final int day = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);
        if(dayMonthYearSelected==null){
            dayMonthYearSelected = new DayMonthYearModel(calendarToday.get(Calendar.DATE),calendarToday.get(Calendar.MONTH),calendarToday.get(Calendar.YEAR));
        }
        /** Set text*/

        final DayMonthYearModel solarDay = new DayMonthYearModel(day, month + 1, year);
        final DayMonthYearModel lunarDay = CaculateDate.Companion.convertSolar2Lunar(solarDay);
        tvDate.setText(String.valueOf(solarDay.getDd()));
        tvDateLunar.setText(String.valueOf(lunarDay.getDd()));
        // Nếu là ngày đầu tháng thì hiển thị thêm tháng và bold
        if (lunarDay.getDd() == 1) {
            tvDateLunar.setText(lunarDay.getDd() + "/" + lunarDay.getMm());
            tvDateLunar.setTypeface(null, Typeface.BOLD);
        }


        /** --- STYLING --- */
        tvDate.setTypeface(null, Typeface.NORMAL);
        tvDate.setTextColor(Color.BLACK);
        tvDateLunar.setTextColor(Color.GRAY);


        /*Tô xanh những ô đang click*/
        if (day == dayMonthYearSelected.getDd() && month+1== dayMonthYearSelected.getMm() ) {
            cardView.setBackgroundColor(Color.parseColor("#69C095"));
            tvDate.setTextColor(Color.WHITE);
            tvDateLunar.setTextColor(Color.WHITE);
        } else {
            cardView.setBackgroundColor(Color.WHITE);
            tvDate.setTextColor(Color.BLACK);
            tvDateLunar.setTextColor(Color.GRAY);
        }


        //Nếu ngày này nằm ngoài tháng hiện tại thì để màu xám
        if (month+1 != dayMonthYearSelected.getMm()|| year != dayMonthYearSelected.getYyyy()) {
            tvDate.setTextColor(Color.parseColor("#E0E0E0"));
        } else if (day == calendarToday.get(Calendar.DATE)&&month == calendarToday.get(Calendar.MONTH)&&year==calendarToday.get(Calendar.YEAR)) {
            tvDate.setTextColor(Color.parseColor("#9B1D1B"));
            tvDate.setTypeface(tvDate.getTypeface(), Typeface.BOLD);
            tvDate.setGravity(Gravity.CENTER);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayMonthYearSelected = new DayMonthYearModel(day, month+1, year);
                iMonthCalendar.setCalendarToView(calendar);
                iMonthCalendar.setDateToView(new DayMonthYearModel(solarDay.getDd(), solarDay.getMm(), solarDay.getYyyy()));
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
