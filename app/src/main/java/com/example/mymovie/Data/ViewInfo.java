package com.example.mymovie.Data;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewInfo {

    LinearLayout linearLayout;
    ImageView imageView;
    TextView textView;

    public ViewInfo(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        this.linearLayout = linearLayout;
        this.imageView = imageView;
        this.textView = textView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
