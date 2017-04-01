package meandow.uthgardwatchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


 class PlayerData {
    public List<Player> Players;
    public final String PrefKey = "PlayerData";
    public final String PlayerKey = "PlayerData.Players";

    private PlayerData(Context context) {
        //TODO: Load from shared prefs
        SharedPreferences prefs = context.getSharedPreferences(PrefKey, 0);
        String zones = prefs.getString(PlayerKey, null);
        // First time?
        if (zones == null) {
            Players = getDefaultPlayers();
            return;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Player>>() {
        }.getType();
        try {
            Players = gson.fromJson(zones, listType);
        } catch (Throwable tr) {
            Log.e("ZoneData", "Failed to parse json, due to " + tr, tr);
            Players = getDefaultPlayers();
        }
    }

    public void save(Context context) {
        Gson gson = new Gson();
        String zonesString = gson.toJson(Players);
        SharedPreferences prefs = context.getSharedPreferences(PrefKey, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PlayerKey, zonesString);
        editor.commit();
    }

    private static PlayerData mInstance;

    public static PlayerData getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PlayerData.class) {
                if (mInstance == null) {
                    mInstance = new PlayerData(context);
                }
            }
        }
        return mInstance;
    }

    public void addPlayer(Player playur){
        Players.add(playur);
    }
    private List<Player> getDefaultPlayers(){
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("Meandaw","Bleach Cabal","Saracen","Cabalist","ALBION",0,22983,50,24);
        players.add(p1);
        return players;
    }

}
