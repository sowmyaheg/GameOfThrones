package com.gameofthrones.gameofthrones;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    TableLayout tableLayout;
    Button button;

    String episodeTitle = null;
    String[] rowFields = {"Year",
            "Rated",
            "Released",
            "Season",
            "Episode",
            "Runtime"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        tableLayout = (TableLayout) rootview.findViewById(R.id.episode_table);

        Intent intent = getActivity().getIntent();
        episodeTitle = intent.getStringExtra("episode");

        getActivity().setTitle(episodeTitle);

        addListenerOnButton(rootview);
        updateEpisodeInfo();

        return rootview;
    }

    public void addListenerOnButton(View rootview) {
        button = (Button) rootview.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showMoreInfo(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void addRows() throws JSONException {

        JSONObject json = (JSONObject) GOTData.getFromEpisodeList(episodeTitle);

        for (int i = 0; i < rowFields.length ; i++) {
            String key = rowFields[i];
            String value = json.getString(key);

            TextView tvKey = new TextView(getActivity());
            TextView tvValue = new TextView(getActivity());

            // set some properties of rowTextView or something
            tvKey.setText(key);
            tvKey.setTextColor(Color.WHITE);
            tvKey.setTextSize(18);
            tvKey.setTypeface(Typeface.DEFAULT_BOLD);

            tvValue.setText(value);
            tvValue.setTextColor(Color.WHITE);
            tvValue.setTextSize(18);

            // add the textview to the linearlayout
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            row.addView(tvKey);
            row.addView(tvValue);
            tableLayout.addView(row);
        }
    }


    public void showMoreInfo(View view) throws JSONException {
        JSONObject json = (JSONObject) GOTData.getFromEpisodeList(episodeTitle);

        button = (Button) view.findViewById(R.id.button);
        tableLayout.removeAllViews();
        if (button.getText().equals("More")) {

            button.setText("Less");
            Iterator<String> iter = json.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = (String) json.get(key);
                    TextView tvKey = new TextView(getActivity());
                    TextView tvValue = new TextView(getActivity());

                    // set some properties of rowTextView or something
                    tvKey.setText(key);
                    tvKey.setTextColor(Color.WHITE);
                    tvKey.setTextSize(18);
                    tvKey.setTypeface(Typeface.DEFAULT_BOLD);

                    tvValue.setText(value);
                    tvValue.setTextColor(Color.WHITE);
                    tvValue.setTextSize(18);

                    // add the textview to the linearlayout
                    TableRow row = new TableRow(getActivity());
                    row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    row.addView(tvKey);
                    row.addView(tvValue);
                    tableLayout.addView(row);

                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

        }
        else {
            button.setText("More");
            addRows();
        }
    }


    private void updateEpisodeInfo() {

        try {
            FetchEpisodeTask episodeInfoTask = new FetchEpisodeTask(this);
            JSONObject episode = (JSONObject) GOTData.getFromEpisodeList(episodeTitle);
            String imdbId = null;
            imdbId = episode.getString("imdbID");
            String episodeInfoUrl = "http://www.omdbapi.com/?plot=short&r=json&i=";
            episodeInfoTask.execute(episodeInfoUrl + imdbId, "episode");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
