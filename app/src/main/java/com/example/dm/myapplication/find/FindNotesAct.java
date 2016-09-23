package com.example.dm.myapplication.find;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dm.myapplication.R;

/**
 * FindNotesAct
 * Created by dm on 16-9-23.
 */
public class FindNotesAct extends Activity implements View.OnClickListener {
    private static final String TAG = "FindNotesAct";
    private ImageButton titleBackImv;
    private FloatingActionButton mAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_notes_layout);

        initView();
        eventDeals();
    }

    private void initView() {
        titleBackImv = (ImageButton) findViewById(R.id.title_notes_imv);
        mAddFab = (FloatingActionButton) findViewById(R.id.find_notes_fab);
    }

    private void eventDeals() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_notes_imv:
                FindNotesAct.this.finish();
                break;
            case R.id.find_notes_fab:
                Toast.makeText(FindNotesAct.this, "here", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
