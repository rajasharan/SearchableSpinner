package com.rajasharan.searchablespinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajasharan on 8/28/15.
 */
public class RecyclerDropdown extends RecyclerView {
    private static final CharSequence [] EMPTY_LIST = new CharSequence[0];

    private CharSequence [] mList;
    private CharSequence [] mOriginalList;
    private Adapter mAdapter;

    public RecyclerDropdown(Context context) {
        this(context, null);
    }

    public RecyclerDropdown(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerDropdown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setDropdownList(null, null);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        addItemDecoration(new Divider(getContext()));
    }

    public void setDropdownList(CharSequence[] list, OnClickListener listener) {
        if (list == null) {
            mList = EMPTY_LIST;
        } else {
            mList = list;
        }
        setAdapter(new Adapter(mList, listener));
    }

    public void filter(String str, OnClickListener listener) {
        if (mOriginalList == null) {
            mOriginalList = mList.clone();
        }
        List<CharSequence> list = new ArrayList<>();
        for (CharSequence cs: mOriginalList) {
            if (cs.toString().toUpperCase().contains(str.toUpperCase())) {
                list.add(cs);
            }
        }
        mList = list.toArray(new CharSequence[0]);
        setDropdownList(mList, listener);
    }

    private static class Adapter extends RecyclerView.Adapter<Holder> {
        private CharSequence [] mList;
        private OnClickListener mListener;
        private ViewGroup.LayoutParams mLayoutParams;
        public Adapter(CharSequence [] list, OnClickListener listener) {
            mList = list;
            mListener = listener;
            mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView view = new TextView(viewGroup.getContext());
            view.setPadding(10, 15, 15, 10);
            view.setLayoutParams(mLayoutParams);
            view.setOnClickListener(mListener);
            Holder holder = new Holder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(Holder holder, int i) {
            holder.mTextView.setText(mList[i]);
        }
        @Override
        public int getItemCount() {
            return mList.length;
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public Holder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }

    private static class Divider extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        public Divider(Context context) {
            TypedArray a = context.obtainStyledAttributes(new int[] {android.R.attr.listDivider});
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            for (int i=0; i<parent.getChildCount()-1; i++) {
                View child = parent.getChildAt(i);
                mDivider.setBounds(parent.getLeft(), child.getBottom(),
                        parent.getRight(), child.getBottom()+mDivider.getIntrinsicHeight());
                mDivider.draw(c);
            }
        }
    }
}
