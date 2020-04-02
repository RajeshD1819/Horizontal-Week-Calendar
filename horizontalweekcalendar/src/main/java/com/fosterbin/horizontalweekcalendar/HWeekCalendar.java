package com.fosterbin.horizontalweekcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HWeekCalendar extends LinearLayout {

    private static final String TAG = HWeekCalendar.class.getSimpleName();
    RecyclerView mRecyclerView;
    LinearLayout containerDay1, containerDay2, containerDay3, containerDay4, containerDay5, containerDay6, containerDay7;
    TextView textDay1, textDay2, textDay3, textDay4, textDay5, textDay6, textDay7;
    CheckedTextView textDate1, textDate2, textDate3, textDate4, textDate5, textDate6, textDate7;
    LinearLayout containerWeekDays;
    WeekAdapter mWeekAdapter;
    Context context;
    int mWeekOffset = 4;
    int mWeekMax = 8;
    DateTimeFormatter dayFormat = DateTimeFormat.forPattern("E");
    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd");
    private LinearLayoutManager mLinearLayoutManager;
    private CenterSmoothScroller mSmoothScroller;
    private int mLastCheckPos;
    private OnWeekCalendarClick mOnWeekCalendarClick;


    public HWeekCalendar(Context context) {
        super(context);
        init();
    }

    public void init() {

        inflate(getContext(), R.layout.h_week_calendar, this);

        containerWeekDays = findViewById(R.id.containerWeekDays);
        mRecyclerView = findViewById(R.id.recyclerViewWeek);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mWeekAdapter = new WeekAdapter(new OnRecyclerViewClick() {
            @Override
            public void onWeekSelected(View view, int layoutPosition) {

                if (view.getId() == R.id.rootView) {

                    mWeekAdapter.getItem(mLastCheckPos).setSelected(false);
                    mWeekAdapter.notifyItemChanged(mLastCheckPos);

                    mWeekAdapter.getItem(layoutPosition).setSelected(true);
                    mWeekAdapter.notifyItemChanged(layoutPosition);

                    mLastCheckPos = layoutPosition;

                    mSmoothScroller.setTargetPosition(layoutPosition);
                    mLinearLayoutManager.startSmoothScroll(mSmoothScroller);

                    setWeekDateView(layoutPosition);
                }
            }
        });


        DateTime dateTime = new DateTime();
        dateTime = dateTime.withDayOfWeek(DateTimeConstants.MONDAY);
        dateTime = dateTime.minusWeeks(mWeekOffset);
        int totalWeek = mWeekOffset + mWeekMax;
        List<WeekModel> modelList = new ArrayList<>();
        for (int i = 0; i < totalWeek; i++) {
            WeekModel weekModel = new WeekModel();
            weekModel.setDateTime(dateTime);
            weekModel.setSelected(false);
            modelList.add(weekModel);
            dateTime = dateTime.plusWeeks(1);
        }
        mWeekAdapter.addItem(modelList);
        mRecyclerView.setAdapter(mWeekAdapter);
        mSmoothScroller = new CenterSmoothScroller(mRecyclerView.getContext());

        mSmoothScroller.setTargetPosition(mWeekOffset);
        mWeekAdapter.getItem(mWeekOffset).setSelected(true);
        mLastCheckPos = mWeekOffset;
        mWeekAdapter.notifyItemChanged(mWeekOffset);
        mLinearLayoutManager.startSmoothScroll(mSmoothScroller);

        final int finalFirstCheckedPost = mWeekOffset;
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setWeekDateView(finalFirstCheckedPost);
            }
        });
    }

    private void setWeekDateView(int layoutPosition) {

        DateTime dateTime = mWeekAdapter.getItem(layoutPosition).getDateTime();

        Log.i(TAG, "setWeekDateView: " + dateFormat.print(dateTime));

        int todaySelectedPos = -1;
        for (int i = 0; i < containerWeekDays.getChildCount(); i++) {

            LinearLayout dayContainer = (LinearLayout) containerWeekDays.getChildAt(i);
            TextView textDay = (TextView) dayContainer.getChildAt(0);
            CheckedTextView textDate = (CheckedTextView) dayContainer.getChildAt(1);
            textDay.setText(dayFormat.print(dateTime));
            textDate.setText(dateFormat.print(dateTime));

            DateTime finalDateTime = dateTime;
            dayContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnWeekCalendarClick != null) {
                        mOnWeekCalendarClick.onDateClick(finalDateTime);
                    }
                    for (int j = 0; j < containerWeekDays.getChildCount(); j++) {

                        LinearLayout dayContainer = (LinearLayout) containerWeekDays.getChildAt(j);
                        TextView textDay = (TextView) dayContainer.getChildAt(0);
                        CheckedTextView textDate = (CheckedTextView) dayContainer.getChildAt(1);

                        if (v.getId() == dayContainer.getId()) {
                            if (!textDate.isChecked()) {
                                textDate.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textDate.setChecked(true);
                                    }
                                });
                            }
                        } else {
                            if (textDate.isChecked()) {
                                textDate.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textDate.setChecked(false);
                                    }
                                });
                            }
                        }
                    }
                }
            });

            if (DateTimeComparator.getDateOnlyInstance().compare(dateTime, new DateTime()) == 0) {
                todaySelectedPos = i;
                dayContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        dayContainer.performClick();
                    }
                });
            } else {
                textDate.setChecked(false);
            }
            dateTime = dateTime.plusDays(1);
        }

        // if non of date is selected then make default selection
        if (todaySelectedPos == -1) {
            LinearLayout dayContainer = (LinearLayout) containerWeekDays.getChildAt(0);
            dayContainer.post(new Runnable() {
                @Override
                public void run() {
                    dayContainer.performClick();
                }
            });
        }
    }

    public HWeekCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnWeekCalendarClick(OnWeekCalendarClick mOnWeekCalendarClick) {
        this.mOnWeekCalendarClick = mOnWeekCalendarClick;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth() / 7;
        for (int i = 0; i < containerWeekDays.getChildCount(); i++) {
            LinearLayout dayContainer = (LinearLayout) containerWeekDays.getChildAt(i);
            dayContainer.getLayoutParams().width = width;
        }
    }


}
