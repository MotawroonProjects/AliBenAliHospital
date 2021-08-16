package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.databinding.ReservationOfferRowBinding;
import com.alibenalihospital.databinding.ReservationRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.ReservationModel;
import com.alibenalihospital.models.ReservationOfferModel;

import java.util.List;

public class MyReservationOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReservationOfferModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.ReservationOfferListener listener;

    public MyReservationOfferAdapter(List<ReservationOfferModel> list, Context context,Listeners.ReservationOfferListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ReservationOfferRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.reservation_offer_row, parent, false);
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
            ReservationOfferModel model = list.get(myHolder.getAdapterPosition());
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
        public ReservationOfferRowBinding binding;

        public MyHolder(@NonNull ReservationOfferRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
