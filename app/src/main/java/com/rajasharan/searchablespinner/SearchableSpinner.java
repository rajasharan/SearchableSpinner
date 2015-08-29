package com.rajasharan.searchablespinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rajasharan on 8/28/15.
 */
public class SearchableSpinner extends ViewGroup {
    private static final String TAG = "Searchable_Spinner";
    public static final String LIST = "list";
    public static final String SELECTION = "data";

    private Intent mIntent;
    private CharSequence [] mList;
    private TextView mText;
    private TextView mDropdownArrow;

    public SearchableSpinner(Context context) {
        this(context, null);
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        mList = a.getTextArray(R.styleable.SearchableSpinner_list);
        a.recycle();

        if (mList == null) {
            throw new UnsupportedOperationException("app:list attr of SearchableSpinner is null; should point string-array resource");
        }
        mIntent = new Intent(context, FilterDialog.class);
        mIntent.putExtra(LIST, mList);

        mText = new TextView(context);
        mText.setText(mList[0]);
        mText.setTextColor(Color.BLACK);
        mText.setLayoutParams(generateDefaultLayoutParams());

        mDropdownArrow = new TextView(context);
        mDropdownArrow.setText("\u25BC");
        mDropdownArrow.setLayoutParams(generateDefaultLayoutParams());

        int [] paddingAttrs = new int[] {
                android.R.attr.padding,
                android.R.attr.paddingLeft, android.R.attr.paddingTop,
                android.R.attr.paddingRight, android.R.attr.paddingBottom};

        a = context.obtainStyledAttributes(attrs, paddingAttrs);
        for (int i=0; i<paddingAttrs.length; i++) {
            int temp = a.hasValue(i) ? setPadding(a.getDimensionPixelSize(i, -1), i) : -1;
        }
    }

    private int setPadding(int p, int sideIndex) {
        if (p != -1) {
            switch (sideIndex) {
                case 0:
                    mText.setPadding(p, p, p, p);
                    mDropdownArrow.setPadding(p, p, p, p);
                    break;
                case 1:
                    mText.setPadding(p, getPaddingTop(), getPaddingRight(), getPaddingBottom());
                    mDropdownArrow.setPadding(p, getPaddingTop(), getPaddingRight(), getPaddingBottom());
                    break;
                case 2:
                    mText.setPadding(getPaddingLeft(), p, getPaddingRight(), getPaddingBottom());
                    mDropdownArrow.setPadding(getPaddingLeft(), p, getPaddingRight(), getPaddingBottom());
                    break;
                case 3:
                    mText.setPadding(getPaddingLeft(), getPaddingTop(), p, getPaddingBottom());
                    mDropdownArrow.setPadding(getPaddingLeft(), getPaddingTop(), p, getPaddingBottom());
                    break;
                case 4:
                    mText.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), p);
                    mDropdownArrow.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), p);
                    break;
            }
        }
        return -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mText.measure(0, 0);
        mDropdownArrow.measure(0, 0);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int desiredWidth = Math.min(mText.getMeasuredWidth() + mDropdownArrow.getMeasuredWidth(), w);
        int desiredHeight = Math.min(mText.getMeasuredHeight(), h);

        //Log.d(TAG, String.format("onMeasure: (%s, %s)", desiredWidth, desiredHeight));

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mText.layout(l, t, l + mText.getMeasuredWidth(), b);
        mDropdownArrow.layout(l + mText.getMeasuredWidth(), t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mText.draw(canvas);
        int save = canvas.save();
        canvas.translate(mText.getWidth(), 0);
        mDropdownArrow.draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                ((Activity)getContext()).startActivityForResult(mIntent, 1);
        }
        return true;
    }

    public void updateSelection(CharSequence str) {
        mText.setText(str);
        requestLayout();
        invalidate();
    }

    public CharSequence getCurrentSelection() {
        return mText.getText();
    }
}
