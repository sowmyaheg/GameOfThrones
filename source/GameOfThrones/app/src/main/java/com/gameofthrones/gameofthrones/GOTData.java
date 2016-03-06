package com.gameofthrones.gameofthrones;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by harshabhat on 3/5/16.
 */
public class GOTData {

    public static HashMap<String, Object> allEpisodes = null;

    public static void addToEpisodeList(String key, JSONObject gotEpisode){
        if(allEpisodes == null)
        {
            allEpisodes = new HashMap<String, Object>();
        }

        allEpisodes.put(key, gotEpisode);
    }

    public static Object getFromEpisodeList(String season){
        if (allEpisodes == null){
            return null;
        }
        return allEpisodes.get(season);
    }


}
