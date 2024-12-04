package com.example.myattendance;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView attendanceView;
    TextView dataView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        attendanceView=itemView.findViewById(R.id.attendance);
        dataView=itemView.findViewById(R.id.date);
    }
}
