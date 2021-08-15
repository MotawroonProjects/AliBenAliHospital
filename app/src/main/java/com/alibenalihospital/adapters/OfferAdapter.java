package com.alibenalihospital.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_notification.NotificationActivity;
import com.alibenalihospital.activities_fragments.activity_offers.OffersActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.databinding.NotificationRowBinding;
import com.alibenalihospital.databinding.OfferRowBinding;
import com.alibenalihospital.models.AllOfferModel;
import com.alibenalihospital.models.NotificationModel;
import com.alibenalihospital.models.OfferDataModel;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OfferDataModel.OfferData> list;
    private Context context;
    private LayoutInflater inflater;
    private NotificationActivity activity;

    public OfferAdapter(List<OfferDataModel.OfferData> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }
    public OfferAdapter( Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OfferRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.offer_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvoldprice.setPaintFlags(myHolder.binding.tvoldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof ServiceProcessActivity){
        ServiceProcessActivity serviceProcessActivity=(ServiceProcessActivity)context;
        serviceProcessActivity.show(list.get(holder.getLayoutPosition()));}
        else {
            OffersActivity serviceProcessActivity=(OffersActivity) context;
            serviceProcessActivity.show();
        }
    }
});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OfferRowBinding binding;

        public MyHolder(@NonNull OfferRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
