package com.fosterbin.horizontalweekcalendar;

import org.joda.time.DateTime;

public class WeekModel {
    boolean isSelected;
    DateTime dateTime;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
