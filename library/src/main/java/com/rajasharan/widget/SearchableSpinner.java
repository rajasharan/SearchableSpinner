package com.rajasharan.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rajasharan on 8/28/15.
 */
public class SearchableSpinner extends ViewGroup implements TextWatcher, View.OnClickListener {
    private static final String TAG = "Searchable_Spinner";

    private CharSequence [] mList;
    private TextView mText;
    private TextView mDropdownArrow;
    private AlertDialog mDialog;
    private RecyclerDropdown mRecycler;
    private OnSelectionChangeListener mListener;

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
        mText = new TextView(context);
        mText.setTextColor(Color.BLACK);
        mText.setLayoutParams(generateDefaultLayoutParams());

        mDropdownArrow = new TextView(context);
        mDropdownArrow.setText("\u25BC");
        mDropdownArrow.setLayoutParams(generateDefaultLayoutParams());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        setList(a.getTextArray(R.styleable.SearchableSpinner_list));
        a.recycle();

        int [] paddingAttrs = new int[] {
                android.R.attr.padding,
                android.R.attr.paddingLeft, android.R.attr.paddingTop,
                android.R.attr.paddingRight, android.R.attr.paddingBottom};

        a = context.obtainStyledAttributes(attrs, paddingAttrs);
        for (int i=0; i<paddingAttrs.length; i++) {
            int temp = a.hasValue(i) ? setPadding(a.getDimensionPixelSize(i, -1), i) : -1;
        }
    }

    public void setList(CharSequence [] list) {
        mList = list;
        if (mList != null && mList.length > 0) {
            mText.setText(mList[0]);
            mDialog = createDialog(getContext());
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

    private AlertDialog createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_dropdown, null);

        EditText filter = (EditText) view.findViewById(R.id.filter);
        filter.setHint("\uD83D\uDD0D search");
        filter.addTextChangedListener(this);

        mRecycler = (RecyclerDropdown) view.findViewById(R.id.list);
        mRecycler.setOnClickListener(this);
        mRecycler.setDropdownList(mList);
        mRecycler.scrollToPosition(mList.length/2);

        builder.setView(view);
        return builder.create();
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
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
        }
        return true;
    }

    private void updateSelection(CharSequence str) {
        mText.setText(str);
        requestLayout();
        invalidate();
        if (mListener != null) {
            mListener.onSelectionChanged(str.toString());
        }
    }

    public String getSelectedItem() {
        if (mText != null) {
            return mText.getText().toString();
        }
        return null;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mRecycler.filter(s.toString());
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        updateSelection(((TextView)v).getText());
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    public interface OnSelectionChangeListener {
        public void onSelectionChanged(String selection);
    }
}
