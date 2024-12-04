package com.example.myattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * {@code MyAdapter} is a custom adapter used to bind data (attendance dates) to a RecyclerView.
 * This adapter takes a list of {@link Date} objects and displays each date in a corresponding view within the RecyclerView.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    // Context and list of dates to be displayed in the RecyclerView
    private Context context;
    private List<Date> dates;

    /**
     * Constructor to initialize the adapter with the context and the list of dates.
     * @param context The context of the activity or application.
     * @param dates The list of {@link Date} objects to display.
     */
    public MyAdapter(Context context, List<Date> dates) {
        this.context = context;
        this.dates = dates;
    }

    /**
     * Creates a new ViewHolder object and inflates the item layout.
     * This method is called when a new view is needed for the RecyclerView.
     * @param parent The ViewGroup that the new View will be added to.
     * @param viewType The view type of the new view (not used here, but required by the adapter).
     * @return A new instance of {@link MyViewHolder}, which holds a reference to the view for a single item.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Binds data to the ViewHolder. This method is called to display the data at the specified position in the RecyclerView.
     * @param holder The ViewHolder that holds the reference to the view for a single item.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the Date object at the current position and set the text of the TextView
        holder.dataView.setText(dates.get(position).getDate());
    }

    /**
     * Returns the total number of items in the data set.
     * @return The size of the dates list.
     */
    @Override
    public int getItemCount() {
        return dates.size();
    }
}
