package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_departments.DepartmentsActivity;
import com.alibenalihospital.activities_fragments.activity_offer_detials.OfferDetialsActivity;
import com.alibenalihospital.databinding.DayRowBinding;
import com.alibenalihospital.databinding.HourRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.alibenalihospital.models.DateModel;
import com.alibenalihospital.models.HourModel;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HourModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int pos=-1;
    private int oldPos = pos;
    private Listeners.HourListener listener;
    public HourAdapter(Context context,List<HourModel> list,Listeners.HourListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;

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
        HourModel model = list.get(position);
        myHolder.binding.setModel(model);
        if (position==pos){
            myHolder.binding.flHour.setBackgroundResource(R.drawable.rounded_primary);
            myHolder.binding.tvHour.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else {
            myHolder.binding.flHour.setBackgroundResource(R.drawable.small_rounded_stroke_gray2);
            myHolder.binding.tvHour.setTextColor(ContextCompat.getColor(context,R.color.gray1));


        }

        myHolder.itemView.setOnClickListener(v -> {
            if (oldPos!=-1){
                HourModel hourModel = list.get(oldPos);
                hourModel.setSelected(false);
                list.set(oldPos,hourModel);
                notifyItemChanged(oldPos);
            }
            pos = myHolder.getAdapterPosition();
            HourModel hourModel = list.get(position);
            hourModel.setSelected(true);
            list.set(pos,hourModel);
            notifyItemChanged(pos);
            oldPos = pos;

            listener.setHour(hourModel);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HourRowBinding binding;

        public MyHolder(@NonNull HourRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
