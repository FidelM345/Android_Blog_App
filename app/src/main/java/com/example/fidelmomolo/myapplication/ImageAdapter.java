package com.example.fidelmomolo.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fidel M Omolo on 2/11/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter <ImageAdapter.ImageViewHolder>{

    private Context mContext;
    private List<Upload>mUploads;

    public ImageAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.cards,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
      Upload uploadCurrent=mUploads.get(position);
      holder.textView.setText(uploadCurrent.getmName());
        Picasso.with(mContext).load(uploadCurrent.getmImageUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.common_google_signin_btn_icon_light_focused)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        //show as many items a we have in our uploads list
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);


            textView=itemView.findViewById(R.id.text_upload);
            imageView=itemView.findViewById(R.id.image_upload);


        }
    }



}
