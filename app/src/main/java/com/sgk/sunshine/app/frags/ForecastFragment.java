package com.sgk.sunshine.app.frags;

import android.content.Intent;
import android.os.Bundle;
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

import com.sgk.sunshine.app.DetailActivity;
import com.sgk.sunshine.app.R;
import com.sgk.sunshine.app.common.Fixed;
import com.sgk.sunshine.app.tasks.FetchWeatherTask;

import java.util.ArrayList;
import java.util.Arrays;

public class ForecastFragment extends Fragment {
    ListView lvForecast;
    ArrayAdapter<String> aaForecast;

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
                detailIntent.putExtra(Fixed.INTENT_DETAIL_WEATHER, aaForecast.getItem(i));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new FetchWeatherTask(new FetchWeatherTask.OnTaskCompleted() {
                    @Override
                    public void taskCompleted(String[] resultSet) {
                        aaForecast.clear();
                        for(String r: resultSet) {
                            aaForecast.add(r);
                        }
                    }
                }).execute("11416");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
