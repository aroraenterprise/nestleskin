package com.sajarora.skintracker.custom;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by sajarora on 3/11/17.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {
    public DayAxisValueFormatter(BarChart chart) {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int day = (int)value;
        switch (day){
            case 0:
                return "Sun";
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thurs";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
        }
        return "";
    }
}
