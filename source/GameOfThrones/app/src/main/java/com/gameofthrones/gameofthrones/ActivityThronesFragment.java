package com.gameofthrones.gameofthrones;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.AdapterView.*;


/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityThronesFragment extends Fragment {

    private ArrayAdapter<String> gThronesAdapter;

    private int seasonValue = 1;

    public ActivityThronesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_thrones, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String menuName = (String) item.getTitle();

        if(!menuName.isEmpty()){
            char idStr = menuName.charAt(menuName.length() - 1);
            seasonValue = Character.getNumericValue(idStr);
            updateEpisodeList(Character.toString(idStr));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        //TODO - Seasons to be passed as params
        updateEpisodeList(seasonValue + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gThronesAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_episode,
                R.id.list_item_episode_textview,
                new ArrayList<String>()
        );

        View rootView = inflater.inflate(R.layout.fragment_activity_thrones, container,false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_episodes);
        listView.setAdapter(gThronesAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpt, View view, int position, long l) {
                String episode = gThronesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("episode", episode);
                startActivity(intent);

            }
        });

       // updateEpisodeList("1");
        return rootView;
    }

    private void updateEpisodeList(String season) {

        FetchEpisodeTask episodeTask = new FetchEpisodeTask(gThronesAdapter);
        String episodeUrl = "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=";
        episodeTask.execute(episodeUrl + season, "season");
    }



}
