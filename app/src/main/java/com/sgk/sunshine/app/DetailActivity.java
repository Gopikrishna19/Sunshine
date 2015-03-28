package com.sgk.sunshine.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.sgk.sunshine.app.frags.DetailFragment;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailFragment())
                    .commit();
        }
    }
}
