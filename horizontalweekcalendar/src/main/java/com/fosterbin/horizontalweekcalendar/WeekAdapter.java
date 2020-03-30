package com.fosterbin.horizontalweekcalendar;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class WeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = WeekAdapter.class.getSimpleName();
    List<WeekModel> mData = new ArrayList<>();
    OnRecyclerViewClick mOnRecyclerViewClick;

    public WeekAdapter(OnRecyclerViewClick onRecyclerViewClick) {
        mOnRecyclerViewClick = onRecyclerViewClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_week_view,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        WeekModel data = mData.get(position);
        DateTimeFormatter weekFormat = DateTimeFormat.forPattern("MMM dd");

        if (data != null) {
            if (data.isSelected()) {
                viewHolder.textWeek.setChecked(true);
                viewHolder.textWeek.setTypeface(viewHolder.textWeek.getTypeface(), Typeface.BOLD);
                viewHolder.viewHr.setVisibility(View.VISIBLE);
                Log.i(TAG, "onWeekSelected: selected");
            } else {
                viewHolder.textWeek.setTypeface(viewHolder.textWeek.getTypeface(), Typeface.NORMAL);
                viewHolder.textWeek.setChecked(false);
                viewHolder.viewHr.setVisibility(View.GONE);
            }
            DateTime startOfWeek = data.getDateTime().withDayOfWeek(DateTimeConstants.MONDAY);
            DateTime endOfWeek = data.getDateTime().withDayOfWeek(DateTimeConstants.SUNDAY);
            viewHolder.textWeek.setText(weekFormat.print(startOfWeek) + " - " + weekFormat.print(endOfWeek));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public WeekModel getItem(int position) {
        return mData.get(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }


    public void addItem(WeekModel item, int index) {
        mData.add(index, item);
        notifyItemInserted(index);
    }

    public void addItem(List<WeekModel> item) {
        mData.addAll(item);
        notifyDataSetChanged();
    }

    public List<WeekModel> getList() {
        return mData;
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        CheckedTextView textWeek;
        View viewHr;

        public ViewHolder(final View itemView) {
            super(itemView);

            textWeek = itemView.findViewById(R.id.textWeek);
            viewHr = itemView.findViewById(R.id.hr);

            rootView = itemView.findViewById(R.id.rootView);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewClick.onWeekSelected(view, getLayoutPosition());
                }
            });
        }
    }
}
