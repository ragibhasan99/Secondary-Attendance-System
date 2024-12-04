package com.example.myattendance;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@code MyViewHolder} is a custom ViewHolder used by the {@link MyAdapter} class.
 * It holds the views for a single item in the RecyclerView, which in this case are
 * the attendance status and the date corresponding to the attendance record.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {

    // UI components that represent attendance and date
    TextView attendanceView;
    TextView dataView;

    /**
     * Constructor that initializes the views for each item in the RecyclerView.
     * It finds the necessary views within the given itemView.
     * @param itemView The view representing a single item in the RecyclerView.
     */
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        // Find the TextViews from the inflated layout
        attendanceView = itemView.findViewById(R.id.attendance); // Attendance status TextView
        dataView = itemView.findViewById(R.id.date); // Date TextView
    }
}
