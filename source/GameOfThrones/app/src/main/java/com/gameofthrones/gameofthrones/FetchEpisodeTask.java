package com.gameofthrones.gameofthrones;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class FetchEpisodeTask extends AsyncTask<String, Void, String[]> {


    private ArrayAdapter<String> gThronesadapter;
    private DetailActivityFragment detailFragment;

    private String[] episodeTitle, episodeInfo = null;

    public FetchEpisodeTask(ArrayAdapter adapter) {
        this.gThronesadapter = adapter;
    }
    public FetchEpisodeTask(DetailActivityFragment detailFragment){
        this.detailFragment = detailFragment;

    }

    public String[] getEpisodeDataFromJson(JSONObject episodeObject) throws JSONException {

        JSONArray episodes = episodeObject.getJSONArray("Episodes");
        String[] episodeTitle = new String[episodes.length()];

        for (int i = 0; i < episodes.length(); i++) {
            JSONObject episode = episodes.getJSONObject(i);
            episodeTitle[i]= (String) episode.get("Title");
            GOTData.addToEpisodeList(episodeTitle[i], episode);
        }

        return episodeTitle;
    }

    @Override
    protected String[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String episodeJsonStr = null;

        try {

            URL url = new URL(params[0]);
            Log.v("URL is ", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            try {
                urlConnection.getResponseCode();
            } catch (IOException e) {
                Log.e("SOM", "Error: URL connection error", e);
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            episodeJsonStr = buffer.toString();
            //Log.v("SOM", "Episode string: " + episodeJsonStr + "-----"+params[1]);

            JSONObject GOTEpisode = new JSONObject(episodeJsonStr);

            if(params[1].equals("season")) {
                episodeTitle = getEpisodeDataFromJson(GOTEpisode);
                return episodeTitle;
                //Log.v("SOM", "List of episodes" + Arrays.toString(episodeTitle));
            }
            else if(params[1].equals("episode")) {
                GOTData.addToEpisodeList(GOTEpisode.getString("Title"),GOTEpisode);
                return null;
            }

        } catch (IOException e) {
            Log.e("SOM", "Error: Didn't successfully get data", e);

        } catch (JSONException e) {
            Log.e("SOM", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("SOM", "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result){
        if(result!=null){
            gThronesadapter.clear();
            for(String episodeTitle :result ){
                gThronesadapter.add(episodeTitle);
            }
        }
        else{
            try {
                detailFragment.addRows();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
