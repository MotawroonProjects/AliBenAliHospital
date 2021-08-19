package com.alibenalihospital.adapters;

import android.annotation.SuppressLint;
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
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.AvailableDateModel;
import com.alibenalihospital.models.DateModel;
import com.alibenalihospital.models.RateModel;

import java.util.Date;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DateModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int pos=-1;
    private int oldPos = pos;
    private Listeners.DateListener listener;


    public DayAdapter(List<DateModel> list, Context context,Listeners.DateListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;


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
        DateModel model = list.get(position);
        myHolder.binding.setModel(model);

        myHolder.itemView.setOnClickListener(v -> {
            if (oldPos!=-1){
                DateModel dateModel = list.get(oldPos);
                dateModel.setSelected(false);
                list.set(oldPos,dateModel);
                notifyItemChanged(oldPos);
            }
            pos = myHolder.getAdapterPosition();
            DateModel dateModel = list.get(position);
            dateModel.setSelected(true);
            list.set(pos,dateModel);
            notifyItemChanged(pos);
            oldPos = pos;
            listener.setDate(dateModel,myHolder.getAdapterPosition());
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public DayRowBinding binding;

        public MyHolder(@NonNull DayRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updatePos(int pos){
        this.pos = pos;
        this.oldPos = pos;
    }


}
