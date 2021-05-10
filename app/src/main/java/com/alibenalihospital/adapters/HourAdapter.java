package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_departments.DepartmentsActivity;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.databinding.DayRowBinding;
import com.alibenalihospital.databinding.HourRowBinding;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private OfferDetialsActivity activity;
    private int i = -1;

    public HourAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (OfferDetialsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        HourRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.hour_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData();
            i = position;
            notifyDataSetChanged();
        });
        if (i == position) {

            ((HourAdapter.MyHolder) holder).binding.fl.setBackground(context.getResources().getDrawable(R.drawable.rounded_primary));
            ((MyHolder) holder).binding.tvhour.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            ((MyHolder) holder).binding.tvhour.setTextColor(context.getResources().getColor(R.color.gray1));

            ((HourAdapter.MyHolder) holder).binding.fl.setBackground(context.getResources().getDrawable(R.drawable.small_rounded_stroke_gray2));

        }

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HourRowBinding binding;

        public MyHolder(@NonNull HourRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
