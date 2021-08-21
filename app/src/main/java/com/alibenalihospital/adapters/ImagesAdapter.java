package com.alibenalihospital.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.alibenalihospital.R;
import com.alibenalihospital.databinding.ImageRowBinding;
import com.alibenalihospital.databinding.SliderRowBinding;
import com.alibenalihospital.models.SliderModel;
import com.alibenalihospital.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends PagerAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public ImagesAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.image_row, container, false);
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+list.get(position))).into(binding.image);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
