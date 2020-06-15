package com.example.signlangdict;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.format.TextStyle;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {

        super(itemView);

        mView = itemView;

    }

    public void setDetails(Context ctx, String title, String meaning, String image){
        TextView mTitleTV = mView.findViewById(R.id.rTitleTv);
        TextView mDetailTV = mView.findViewById(R.id.rMeaning);
        ImageView mImageIV = mView.findViewById(R.id.rImageView);

        mTitleTV.setText(title);
        mDetailTV.setText(meaning);
        Picasso.get().load(image).into(mImageIV);
    }

}
