package com.sgk.sunshine.app.frags;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgk.sunshine.app.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastFragment extends Fragment {
    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask {

        private String makeHttpRequest() {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=11416&mode=json&units=metric&cnt=7");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) buffer.append(line + "\n");

                if (buffer.length() == 0) return null;
                forecastJsonStr = buffer.toString();

            } catch (Exception e) {
                Log.e("ForecastFragment", "Error", e);
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return forecastJsonStr;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
