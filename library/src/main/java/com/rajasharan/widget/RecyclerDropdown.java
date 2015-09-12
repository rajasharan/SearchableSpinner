package com.rajasharan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajasharan on 8/28/15.
 */
public class RecyclerDropdown extends RecyclerView {
    private static final String TAG = "RecyclerDropdown";
    private static final CharSequence [] EMPTY_LIST = new CharSequence[] {Adapter.EMPTY_TEXT};

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
        mAdapter = new Adapter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setDropdownList(null, null, false);
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        addItemDecoration(new BitmapDecorator(getContext()));
    }

    public void setDropdownList(CharSequence [] list, OnClickListener listener, boolean repeatInLoop) {
        if (list == null) {
            mList = EMPTY_LIST;
        } else {
            mList = list;
        }
        mAdapter.init(mList, listener, repeatInLoop);
        mAdapter.notifyDataSetChanged();
        if (repeatInLoop) {
            scrollToPosition(Integer.MAX_VALUE / 2);
        }
    }

    public void filter(String str, OnClickListener listener) {
        boolean repeateInLoop = false;
        if (mOriginalList == null) {
            mOriginalList = mList.clone();
        }
        if (str != null && str.trim().length() != 0) {
            List<CharSequence> list = new ArrayList<>();
            for (CharSequence cs : mOriginalList) {
                if (cs.toString().toUpperCase().contains(str.toUpperCase())) {
                    list.add(cs);
                }
            }
            if (list.size() == 0) {
                mList = EMPTY_LIST;
                listener = null;
            } else {
                mList = list.toArray(new CharSequence[list.size()]);
            }
            repeateInLoop = false;
        }
        else {
            mList = mOriginalList;
            repeateInLoop = true;
        }
        setDropdownList(mList, listener, repeateInLoop);
    }

    private static class Adapter extends RecyclerView.Adapter<Holder> {
        private static final int FIRST_TYPE = 0;
        private static final int NORMAL_TYPE = 1;
        private static final int LAST_TYPE = 2;
        private static final int EMPTY_TYPE = 3;

        private static final String EMPTY_TEXT = "NO ITEMS";

        private CharSequence [] mList;
        private OnClickListener mListener;
        private boolean mLoop;

        public void init(CharSequence [] list, OnClickListener listener, boolean repeat) {
            mList = list;
            mListener = listener;
            mLoop = repeat;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup viewGroup, int type) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            switch (type) {
                case NORMAL_TYPE: {
                    view = inflater.inflate(R.layout.recycler_itemviews, viewGroup, false);
                    break;
                }
                case FIRST_TYPE: {
                    view = inflater.inflate(R.layout.recycler_itemviews_firsttype, viewGroup, false);
                    break;
                }
                case LAST_TYPE: {
                    view = inflater.inflate(R.layout.recycler_itemviews_lasttype, viewGroup, false);
                    break;
                }
                default /*EMPTY_TYPE*/: {
                    view = inflater.inflate(R.layout.recycler_itemviews_firsttype, viewGroup, false);
                    view.setDrawingCacheEnabled(true);
                    Holder holder = new Holder(view);
                    holder.mTextView.setGravity(Gravity.CENTER);
                    holder.mTextView.setOnClickListener(null);
                    return holder;
                }
            }
            view.setDrawingCacheEnabled(true);
            Holder holder = new Holder(view);
            holder.mTextView.setGravity(Gravity.CENTER);
            holder.mTextView.setOnClickListener(mListener);
            return holder;
        }
        @Override
        public void onBindViewHolder(Holder holder, int pos) {
            holder.mTextView.setText(mList[pos % mList.length]);
        }
        @Override
        public int getItemCount() {
            return mLoop? Integer.MAX_VALUE : mList.length;
        }

        @Override
        public int getItemViewType(int position) {
            if (mList[position % mList.length].toString().equals(EMPTY_TEXT)) {
                return EMPTY_TYPE;
            }

            if (position == 0) {
                return FIRST_TYPE;
            } else if (position == mList.length - 1) {
                return LAST_TYPE;
            } else {
                return NORMAL_TYPE;
            }
        }
    }

    private static class Holder extends ViewHolder {
        private TextView mTextView;
        public Holder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }

    private static class BitmapDecorator extends ItemDecoration {
        private Paint mWhitePaint;

        public BitmapDecorator(Context context) {
            mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mWhitePaint.setStyle(Paint.Style.FILL);
            mWhitePaint.setColor(Color.WHITE);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            transformChildren(c, parent);
        }

        private void transformChildren(Canvas canvas, RecyclerView parent) {
            int childCount = parent.getChildCount();
            int middlePos = (childCount - 1)/2;

            canvas.drawARGB(255, 255, 255, 255);

            for (int i=0; i < childCount; i++) {
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
                    canvas.scale(2.0f, 2.0f, r.centerX(), r.centerY());
                    canvas.drawBitmap(b, child.getLeft(), child.getTop(), null);
                    canvas.restore();
                }
            }
        }
    }
}
