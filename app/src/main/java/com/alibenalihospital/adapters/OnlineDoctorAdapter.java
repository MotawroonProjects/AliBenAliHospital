package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_reserve_clinic.ReserveClinicActivity;
import com.alibenalihospital.activities_fragments.activity_reserve_online.ReserveOnlineActivity;
import com.alibenalihospital.databinding.ClinicDoctorRowBinding;
import com.alibenalihospital.databinding.OnlineDoctorRowBinding;

import java.util.List;

public class OnlineDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private ReserveOnlineActivity activity;

    public OnlineDoctorAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ReserveOnlineActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OnlineDoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.online_doctor_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        //myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(v -> {
           // activity.setItemData(list.get(holder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OnlineDoctorRowBinding binding;

        public MyHolder(@NonNull OnlineDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
