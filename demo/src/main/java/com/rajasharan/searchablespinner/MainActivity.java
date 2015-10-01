package com.rajasharan.searchablespinner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.rajasharan.widget.SearchableSpinner;

public class MainActivity extends ActionBarActivity implements SearchableSpinner.OnSelectionChangeListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchableSpinner spinner1 = (SearchableSpinner) findViewById(R.id.search1);
        spinner1.setOnSelectionChangeListener(this);

        SearchableSpinner spinner2 = (SearchableSpinner) findViewById(R.id.search2);
        String [] list = {"ONE", "TWO"};
        spinner2.setList(list);

        SearchableSpinner spinner3 = (SearchableSpinner) findViewById(R.id.search3);
        spinner3.setList(getResources().getStringArray(R.array.all_languages));
    }

    @Override
    public void onSelectionChanged(String selection) {
        Toast.makeText(this, selection + " selected", Toast.LENGTH_SHORT).show();
    }
}
