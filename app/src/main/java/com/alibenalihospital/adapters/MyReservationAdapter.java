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
import com.alibenalihospital.databinding.ReservationRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.ReservationModel;

import java.util.List;

public class MyReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReservationModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.ReservationListener listener;

    public MyReservationAdapter(List<ReservationModel> list, Context context,Listeners.ReservationListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ReservationRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.reservation_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.flShow.setOnClickListener(v -> {
            listener.setReservationData(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition(),1);
        });

        myHolder.binding.flUpdate.setOnClickListener(v -> {
            listener.setReservationData(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition(),2);
        });

        myHolder.binding.flCancel.setOnClickListener(v -> {
            ReservationModel model = list.get(myHolder.getAdapterPosition());
            if (model.getIs_deleted().equals("no")){
                listener.setReservationData(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition(),3);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public ReservationRowBinding binding;

        public MyHolder(@NonNull ReservationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }


}
