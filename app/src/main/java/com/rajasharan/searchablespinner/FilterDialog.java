package com.rajasharan.searchablespinner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rajasharan on 8/28/15.
 */
public class FilterDialog extends Activity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "Searchable_Spinner";

    private RecyclerDropdown mRecycler;
    private Intent mOutIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dropdown);

        Intent intent = getIntent();
        if (intent == null) {
            throw new UnsupportedOperationException("Intent cannot be null");
        }
        CharSequence[] list = intent.getCharSequenceArrayExtra(SearchableSpinner.LIST);
        if (list == null) {
            throw new UnsupportedOperationException("SearchableSpinner.LIST intentExtras cannot be null");
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Point screen = new Point();
        getWindowManager().getDefaultDisplay().getSize(screen);
        LinearLayout l = (LinearLayout) findViewById(R.id.layout);
        int H = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, dm);
        int inset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, dm);
        int desiredHeight = screen.y - inset;

        H = H < desiredHeight? H: desiredHeight;
        l.getLayoutParams().height = H;

        EditText filter = (EditText) findViewById(R.id.filter);
        filter.setHint("\uD83D\uDD0D search");
        filter.addTextChangedListener(this);

        mRecycler = (RecyclerDropdown) findViewById(R.id.list);
        mRecycler.setDropdownList(list);

        mOutIntent = new Intent();
    }

    @Override
    public void onClick(View v) {
        mOutIntent.putExtra(SearchableSpinner.SELECTION, ((TextView)v).getText());
        setResult(RESULT_OK, mOutIntent);
        finish();
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
}
