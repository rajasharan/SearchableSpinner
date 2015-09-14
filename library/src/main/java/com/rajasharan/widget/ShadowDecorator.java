package com.rajasharan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by rajasharan on 9/13/15.
 */
public class ShadowDecorator extends RecyclerView.ItemDecoration {
    private static final String TAG = "ShadowDecorator";
    private static final int SHADOW_INSET = 2;

    private Context mContext;
    private Paint mCoreShadow;
    private Paint mOvertone;
    private Paint mHighlight;
    private int mHeight;
    private int mInset;

    public ShadowDecorator(Context context) {
        mContext = context;

        mCoreShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoreShadow.setColor(Color.argb(128, 128, 128, 128));
        mCoreShadow.setStyle(Paint.Style.FILL);

        mOvertone = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOvertone.setColor(Color.argb(64, 128, 128, 128));
        mOvertone.setStyle(Paint.Style.FILL);

        mHighlight = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlight.setColor(Color.argb(32, 128, 128, 128));
        mHighlight.setStyle(Paint.Style.FILL);

        mHeight = (int) (mContext.getResources().getDimension(R.dimen.item_height));
        mInset = (int) mContext.getResources().getDimension(R.dimen.item_inset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        int middlePos = (count - 1)/2;

        int t = 2 * mHeight - mInset;
        int b = 3 * mHeight + mInset;

        /* TOP SHADOW */
        c.drawRect(parent.getLeft(), t, parent.getRight(), t + SHADOW_INSET, mHighlight);
        c.drawRect(parent.getLeft(), t + SHADOW_INSET, parent.getRight(), t + 2 * SHADOW_INSET, mOvertone);
        c.drawRect(parent.getLeft(), t + 2 * SHADOW_INSET, parent.getRight(), t + 3 * SHADOW_INSET, mCoreShadow);

        /* BOTTOM SHADOW */
        c.drawRect(parent.getLeft(), b - 3 * SHADOW_INSET, parent.getRight(), b - 2 * SHADOW_INSET, mCoreShadow);
        c.drawRect(parent.getLeft(), b - 2 * SHADOW_INSET, parent.getRight(), b - SHADOW_INSET, mOvertone);
        c.drawRect(parent.getLeft(), b - SHADOW_INSET, parent.getRight(), b, mHighlight);

        for (int i=0; i<count; i++) {
            View child = parent.getChildAt(i);
            child.setScaleX(1.0f);
            child.setScaleY(1.0f);
            child.setRotationX(0);
            child.setAlpha(1.0f);
            child.setOnClickListener(null);
        }

        for (int i=middlePos-1, j=1; i>=0; i--,j++) {
            View child = parent.getChildAt(i);
            if (child != null) {
                child.setRotationX(25 * j);
                child.setAlpha(1.0f/(count - i));
            }
        }

        {
            View child = parent.getChildAt(middlePos);
            if (child != null) {
                child.setAlpha(0.5f);
                int childMidPoint = (child.getTop() + child.getBottom())/2;
                if (childMidPoint >= t && childMidPoint <= b) {
                    if (child.getTag(R.id.text) == null) {
                        child.setScaleX(2.2f);
                        child.setScaleY(2.2f);
                        child.setOnClickListener(((RecyclerDropdown) parent).getOnClickListener());
                        child.setAlpha(1.0f);
                    }
                }
            }
        }

        for (int i=middlePos+1, j=1; i<count; i++,j++) {
            View child = parent.getChildAt(i);
            if (child != null) {
                child.setRotationX(-25 * j);
                child.setAlpha(1.0f / i);
            }
        }
    }
}
