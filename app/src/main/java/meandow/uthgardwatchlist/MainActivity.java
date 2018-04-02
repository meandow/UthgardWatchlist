package meandow.uthgardwatchlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {
    PlayerData instance;
    private PlayerRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = PlayerData.getInstance(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new PlayerRecyclerAdapter(this, instance.Players);
        recyclerView.setAdapter(adapter);
        Button addPlayer = (Button) findViewById(R.id.add);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlayer();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    instance.removePlayer(viewHolder.getAdapterPosition());
                    instance.save(MainActivity.this);
                    adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                return false;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        instance.save(MainActivity.this);
    }

    public void refresh() {
        for (int i = 0; i < instance.Players.size(); i++) {
            new JsonTask().execute(i);
        }
    }

    public void createNewPlayer() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View dialogView = li.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(dialogView);
        final EditText edittext = (EditText) dialogView.findViewById(R.id.editText);
        alert.setTitle("Add new player");
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Player p1 = new Player(edittext.getText().toString(), "null", "null", "null", "null", 0, 0, 0, 0);
                instance.addPlayer(p1);
                instance.save(MainActivity.this);
                refresh();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    private class JsonTask extends AsyncTask<Integer, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Integer... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                String url_String = "https://www2.uthgard.net/herald/api/player/" + instance.Players.get(params[0]).getName();
                URL url = new URL(url_String);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    if (i == 1) {
                        buffer.append(" \"Position\": " + params[0] + ",\n");
                    }
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                    i++;
                }
                String final_string = buffer.toString();
                return final_string;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject payload;
            try {
                if (result != null) {
                    payload = new JSONObject(result);
                    int position = payload.getInt("Position");
                    instance.Players.get(position).setName(payload.getString("Name"));
                    instance.Players.get(position).setGuild(payload.getString("Guild"));
                    instance.Players.get(position).setRace(payload.getString("Race"));
                    instance.Players.get(position).setClass(payload.getString("Class"));
                    instance.Players.get(position).setRealm(payload.getString("Realm"));
                    instance.Players.get(position).setXp(0);
                    instance.Players.get(position).setRp(payload.getInt("Rp"));
                    instance.Players.get(position).setLvl(payload.getInt("Level"));
                    instance.Players.get(position).setRR(payload.getInt("RealmRank"));
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        }
    }
}
