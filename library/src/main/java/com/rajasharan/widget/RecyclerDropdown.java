package com.rajasharan.widget;

import android.content.Context;
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
    private OnClickListener mListener;

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
        setDropdownList(null);
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //addItemDecoration(new BitmapDecorator(getContext()));
        addItemDecoration(new ShadowDecorator(getContext()));
    }

    public void setDropdownList(CharSequence [] list) {
        if (list == null) {
            mList = EMPTY_LIST;
        } else {
            mList = list;
        }
        mAdapter.init(mList);
        mAdapter.notifyDataSetChanged();
        //scrollToPosition(mList.length / 2);
    }

    public void filter(String str) {
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
            } else {
                mList = list.toArray(new CharSequence[list.size()]);
            }
        }
        else {
            mList = mOriginalList;
        }
        setDropdownList(mList);
    }

    private static class Adapter extends RecyclerView.Adapter<Holder> {
        private static final int NORMAL_TYPE = 1;
        private static final int EMPTY_TYPE = 2;
        private static final int BUFFER_TYPE = 3;

        private static final String EMPTY_TEXT = "NO ITEMS";
        private static final String BUFFER_ITEM = "BUFFER ITEM";

        private CharSequence [] mList;

        public void init(CharSequence [] list) {
            mList = new CharSequence[list.length+4];
            mList[0] = BUFFER_ITEM;
            mList[1] = BUFFER_ITEM;
            System.arraycopy(list, 0, mList, 2, list.length);
            mList[mList.length-1] = BUFFER_ITEM;
            mList[mList.length-2] = BUFFER_ITEM;
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
                case EMPTY_TYPE: {
                    view = inflater.inflate(R.layout.recycler_itemviews, viewGroup, false);
                    Holder holder = new Holder(view);
                    holder.mTextView.setGravity(Gravity.CENTER);
                    view.setTag(R.id.text, EMPTY_TEXT);
                    return holder;
                }
                default /*BUFFER_TYPE*/ : {
                    view = inflater.inflate(R.layout.recycler_buffer_item, viewGroup, false);
                    Holder holder = new Holder(view);
                    return holder;
                }
            }
            Holder holder = new Holder(view);
            holder.mTextView.setGravity(Gravity.CENTER);
            return holder;
        }
        @Override
        public void onBindViewHolder(Holder holder, int pos) {
            if (mList[pos % mList.length].toString().equals(BUFFER_ITEM)) {
                return;
            }
            holder.mTextView.setText(mList[pos % mList.length]);
        }
        @Override
        public int getItemCount() {
            return mList.length;
        }

        @Override
        public int getItemViewType(int position) {
            if (mList[position % mList.length].toString().equals(EMPTY_TEXT)) {
                return EMPTY_TYPE;
            }
            if (mList[position % mList.length].toString().equals(BUFFER_ITEM)) {
                return BUFFER_TYPE;
            }
            return NORMAL_TYPE;
        }
    }

    private static class Holder extends ViewHolder {
        private TextView mTextView;
        public Holder(View itemView) {
            super(itemView);
            if (itemView instanceof TextView) {
                mTextView = (TextView) itemView;
            }
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return mListener;
    }

}
