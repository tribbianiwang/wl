package com.wl.radio.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;

import com.wl.radio.util.ImgUtils;

public class ThemeImageView extends AppCompatImageView {


    public ThemeImageView(Context context) {
            this(context, null);
        }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

        }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ImgUtils.INSTANCE.showColorIcon(this);
    }
}
