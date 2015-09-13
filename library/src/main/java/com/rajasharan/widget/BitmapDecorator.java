package com.rajasharan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rajasharan on 9/13/15.
 */
class BitmapDecorator extends RecyclerView.ItemDecoration {
    private Paint mWhitePaint;
    private GradientDrawable mD;

    public BitmapDecorator(Context context) {
        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setColor(Color.WHITE);
        mD = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, null);
        mD.setColor(Color.argb(128, 128, 128, 128));
        mD.setGradientType(GradientDrawable.SWEEP_GRADIENT);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        transformChildren(c, parent);
    }

    private void transformChildren(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int middlePos = (childCount - 1) / 2;

        canvas.drawARGB(255, 255, 255, 255);

        for (int i = 0; i < childCount; i++) {
            if (i == middlePos) {
                continue;
            }
            View child = parent.getChildAt(i);
            if (child != null) {
                Bitmap b = child.getDrawingCache();
                if (b != null) {
                    canvas.drawBitmap(b, child.getLeft(), child.getTop(), null);
                }
            }
        }

        View child = parent.getChildAt(middlePos);
        if (child != null) {
            Rect r = new Rect();
            child.getHitRect(r);
            Bitmap b = child.getDrawingCache();
            if (b != null) {
                canvas.save();
                mD.setColor(Color.argb(32, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getTop() - 6, child.getRight(), child.getTop() - 4);
                mD.draw(canvas);

                mD.setColor(Color.argb(64, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getTop() - 4, child.getRight(), child.getTop() - 2);
                mD.draw(canvas);

                mD.setColor(Color.argb(128, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getTop() - 2, child.getRight(), child.getTop());
                mD.draw(canvas);

                canvas.scale(2.0f, 2.0f, r.centerX(), r.centerY());
                canvas.drawBitmap(b, child.getLeft(), child.getTop(), null);

                mD.setColor(Color.argb(128, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + 2);
                mD.draw(canvas);

                mD.setColor(Color.argb(64, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getBottom() + 2, child.getRight(), child.getBottom() + 4);
                mD.draw(canvas);

                mD.setColor(Color.argb(32, 128, 128, 128));
                mD.setBounds(child.getLeft(), child.getBottom() + 4, child.getRight(), child.getBottom() + 6);
                mD.draw(canvas);
                canvas.restore();
            }
        }
    }
}
