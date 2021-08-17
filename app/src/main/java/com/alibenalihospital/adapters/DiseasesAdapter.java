package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_reserve_clinic.ReserveClinicActivity;
import com.alibenalihospital.databinding.ClinicDoctorRowBinding;
import com.alibenalihospital.databinding.DiseasesRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.DiseasesModel;

import java.util.List;

public class DiseasesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiseasesModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.DeleteDiseaseListener listener;

    public DiseasesAdapter(List<DiseasesModel> list, Context context,Listeners.DeleteDiseaseListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DiseasesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.diseases_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            listener.deleteDisease(myHolder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public DiseasesRowBinding binding;

        public MyHolder(@NonNull DiseasesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
