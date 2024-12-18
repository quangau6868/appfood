package com.app.appfood.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.app.appfood.R;
import com.app.appfood.model.Image;

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    private List<Image> imageList;

    public ImageAdapter(List<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        Image image = imageList.get(position);
        imgPhoto.setImageResource(image.getResouredID());

        //add View
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(imageList != null){
            return imageList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}

