package com.rajasharan.searchablespinner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.rajasharan.widget.SearchableSpinner;

public class MainActivity extends ActionBarActivity implements SearchableSpinner.OnSelectionChangeListener {

    private SearchableSpinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (SearchableSpinner) findViewById(R.id.search);
        mSpinner.setOnSelectionChangeListener(this);
    }

    @Override
    public void onSelectionChanged(String selection) {
        Toast.makeText(this, selection + " selected", Toast.LENGTH_SHORT).show();
    }
}
