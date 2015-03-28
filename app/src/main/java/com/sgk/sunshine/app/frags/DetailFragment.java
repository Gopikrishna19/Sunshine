package com.sgk.sunshine.app.frags;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sgk.sunshine.app.R;

public class DetailFragment extends Fragment {
    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    private String forecastStr;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        forecastStr = getActivity().getIntent().getStringExtra(ForecastFragment.INTENT_DETAIL_WEATHER);
        Toast.makeText(getActivity(), forecastStr, Toast.LENGTH_LONG).show();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        ShareActionProvider sap = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        if (sap != null) sap.setShareIntent(createShareIntent());
        else
            Toast.makeText(getActivity(), "Sorry! No sharable app found", Toast.LENGTH_SHORT);
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, forecastStr + " #SunShineApp");
        return shareIntent;
    }
}
