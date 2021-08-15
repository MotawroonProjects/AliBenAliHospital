package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.databinding.ClinicDoctorRowBinding;
import com.alibenalihospital.databinding.FavoriteDoctorRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.DoctorModel;

import java.util.List;

public class FavoriteDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DoctorModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.FavListener listener;


    public FavoriteDoctorAdapter(List<DoctorModel> list, Context context, Listeners.FavListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        FavoriteDoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.favorite_doctor_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            listener.setDoctorData(list.get(myHolder.getAdapterPosition()));
        });

        myHolder.binding.imageFav.setOnClickListener(v -> {
            listener.unFav(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public FavoriteDoctorRowBinding binding;

        public MyHolder(@NonNull FavoriteDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
