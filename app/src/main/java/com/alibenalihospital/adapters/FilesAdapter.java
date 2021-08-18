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
import com.alibenalihospital.databinding.FileRowBinding;
import com.alibenalihospital.interfaces.Listeners;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.DeleteDiseaseListener listener;

    public FilesAdapter(List<String> list, Context context,Listeners.DeleteDiseaseListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        FileRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.file_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        Picasso.get().load(new File(list.get(position))).fit().into(myHolder.binding.image);
        myHolder.binding.imageClose.setOnClickListener(v -> {
            listener.deleteImage(myHolder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public FileRowBinding binding;

        public MyHolder(@NonNull FileRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
