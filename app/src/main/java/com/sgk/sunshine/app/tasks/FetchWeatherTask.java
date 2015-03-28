package com.sgk.sunshine.app.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
    public interface OnTaskCompleted {
        public void taskCompleted(String[] resultSet);
    }

    public FetchWeatherTask(OnTaskCompleted eventTrigger) {
        this.eventTrigger = eventTrigger;
    }

    private OnTaskCompleted eventTrigger;
    private final String LOG_NAME = FetchWeatherTask.class.getSimpleName();

    private String getReadableDateString(long time) {
        return new SimpleDateFormat("EEE MMM dd").format(time);
    }

    private String formatHighLows(double h, double l) {
        return Math.round(h) + "/" + Math.round(l);
    }

    private String[] getWeatherFromJSON(String forecastStr, int numDays) throws JSONException {
        final String OWM_LST = "list";
        final String OWM_WTH = "weather";
        final String OWM_TMP = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DSC = "main";

        JSONArray weatherArray = new JSONObject(forecastStr).getJSONArray(OWM_LST);

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        String[] result = new String[numDays];

        for (int i = 0; i < weatherArray.length(); ++i) {
            String day;
            String dsc;
            String hnl;

            JSONObject obj = weatherArray.getJSONObject(i);

            c.add(Calendar.DATE, 1);
            day = getReadableDateString(c.getTimeInMillis());

            dsc = obj.getJSONArray(OWM_WTH).getJSONObject(0).getString(OWM_DSC);

            JSONObject tmp = obj.getJSONObject(OWM_TMP);
            hnl = formatHighLows(tmp.getDouble(OWM_MAX), tmp.getDouble(OWM_MIN));

            result[i] = day + "-" + dsc + "-" + hnl;
        }

        return result;
    }

    private String makeHttpRequest(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily").buildUpon()
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", params[1])
                    .appendQueryParameter("cnt", "7").build();

            URL url = new URL(uri.toString());
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
            Log.e(LOG_NAME, "Error", e);
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
    protected String[] doInBackground(String... objects) {
        try {
            return getWeatherFromJSON(makeHttpRequest(objects), 7);
        } catch (Exception e) {
            Log.e(LOG_NAME, "Error", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if(result != null) {
            eventTrigger.taskCompleted(result);
        }
    }
}

