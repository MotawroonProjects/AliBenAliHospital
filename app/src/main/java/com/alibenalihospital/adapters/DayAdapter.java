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
import com.alibenalihospital.databinding.Department2RowBinding;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private OfferDetialsActivity activity;
    private int i=-1;

    public DayAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (OfferDetialsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DayRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.day_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(null);
            i=position;
            notifyDataSetChanged();
        });
        if(i==position){
            ((MyHolder) holder).binding.fl.setBackground(context.getResources().getDrawable(R.drawable.small_rounded_stroke_primary2));
            ((MyHolder) holder).binding.tvdaynum.setTextColor(context.getResources().getColor(R.color.colorAccent));
            ((MyHolder) holder).binding.tvmonth.setTextColor(context.getResources().getColor(R.color.colorAccent));
            ((MyHolder) holder).binding.tvDay.setBackground(context.getResources().getDrawable(R.drawable.rounded_primary3));

        }
        else {
            ((MyHolder) holder).binding.fl.setBackground(context.getResources().getDrawable(R.drawable.small_rounded_stroke_gray2));
            ((MyHolder) holder).binding.tvdaynum.setTextColor(context.getResources().getColor(R.color.gray1));
            ((MyHolder) holder).binding.tvmonth.setTextColor(context.getResources().getColor(R.color.gray1));
            ((MyHolder) holder).binding.tvDay.setBackground(context.getResources().getDrawable(R.drawable.rounded_gray3));

        }

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public DayRowBinding binding;

        public MyHolder(@NonNull DayRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
