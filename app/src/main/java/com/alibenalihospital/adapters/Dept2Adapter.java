package com.alibenalihospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibenalihospital.R;
import com.alibenalihospital.activities_fragments.activity_departments.DepartmentsActivity;
import com.alibenalihospital.activities_fragments.activity_service_process.ServiceProcessActivity;
import com.alibenalihospital.databinding.Department2RowBinding;
import com.alibenalihospital.databinding.DepartmentRowBinding;
import com.alibenalihospital.models.AllDepartmentModel;

import java.util.List;

public class Dept2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllDepartmentModel.DepartmentData> list;
    private Context context;
    private LayoutInflater inflater;
    private ServiceProcessActivity activity;

    public Dept2Adapter(List<AllDepartmentModel.DepartmentData> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ServiceProcessActivity) context ;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Department2RowBinding binding = DataBindingUtil.inflate(inflater, R.layout.department2_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(list.get(holder.getLayoutPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public Department2RowBinding binding;

        public MyHolder(@NonNull Department2RowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
