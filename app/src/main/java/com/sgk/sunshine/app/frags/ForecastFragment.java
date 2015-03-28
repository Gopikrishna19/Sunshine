package com.sgk.sunshine.app.frags;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sgk.sunshine.app.DetailActivity;
import com.sgk.sunshine.app.R;
import com.sgk.sunshine.app.SettingsActivity;
import com.sgk.sunshine.app.tasks.FetchWeatherTask;

import java.util.ArrayList;
import java.util.Arrays;

public class ForecastFragment extends Fragment {
    ListView lvForecast;
    ArrayAdapter<String> aaForecast;
    public static final String INTENT_DETAIL_WEATHER = "com.intent.detail.weatherString";

    public ForecastFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lvForecast = (ListView) rootView.findViewById(R.id.lvForecast);
        aaForecast = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listitem_forecast,
                R.id.liTvForecast,
                new ArrayList(Arrays.asList(new String[]{"Loading"})));
        lvForecast.setAdapter(aaForecast);
        lvForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(INTENT_DETAIL_WEATHER, aaForecast.getItem(i));
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateWeather();
                break;
            case R.id.action_map:
                openPreferredLocation();
                break;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocation() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String loc = sp.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Uri geo = Uri.parse("geo:0,0").buildUpon().appendQueryParameter("q", loc).build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geo);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(getActivity(), "Sorry! No Map application found", Toast.LENGTH_SHORT).show();
    }

    private void updateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String loc = sp.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String unit = sp.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_default));

        new FetchWeatherTask(new FetchWeatherTask.OnTaskCompleted() {
            @Override
            public void taskCompleted(String[] resultSet) {
                aaForecast.clear();
                for (String r : resultSet) {
                    aaForecast.add(r);
                }
            }
        }).execute(loc, unit);
    }
}
