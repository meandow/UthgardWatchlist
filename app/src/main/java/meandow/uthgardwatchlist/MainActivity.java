package meandow.uthgardwatchlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private ImageView meandowRefresh;
    private TextView textView;
    private String meandowString;
    ProgressDialog pd;
    private SwipeMenuListView mListView;
    private List<Player> Players;
    PlayerListAdapter adapter;
    PlayerData instance;
    private Button addPlayer;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (SwipeMenuListView) findViewById(R.id.zone_list);
        instance = PlayerData.getInstance(this);
        adapter = new PlayerListAdapter(this,R.layout.listitem, instance.Players);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this);
                Drawable deleteDrawable = getResources().getDrawable(R.drawable.ic_delete_black_24dp);
                deleteItem.setWidth(300);
                deleteItem.setIcon(deleteDrawable);
                deleteItem.setTitleColor(Color.DKGRAY);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                return false;
            }
        });
        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
//
//        meandowRefresh = (ImageView) findViewById(R.id.meandowRefresh);
//        meandowRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new JsonTask().execute("https://www2.uthgard.net/herald/api/player/Meandow");
//            }
//        });
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refresh();

            }
        });

        addPlayer = (Button) findViewById(R.id.add);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlayer();
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        refresh();
    }

    @Override
    protected void onPause(){
        super.onPause();
        instance.save(MainActivity.this);
    }

    public void refresh(){
        for (int i=0; i<instance.Players.size();i++) {
            new JsonTask().execute(i);
        }
    }
    public void createNewPlayer(){
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View dialogView = li.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(dialogView);
        final EditText edittext = (EditText) dialogView.findViewById(R.id.editText);
        alert.setTitle("Add new player");
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Player p1 = new Player(edittext.getText().toString(),"null","null","null","null",0,0,0,0);
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
                        buffer.append(" \"Position\": "+params[0]+",\n");
                    }
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                    i++;
                }
                String final_string = buffer.toString();
                Log.i("ASYNCTASK", "FINAL STRING =====" + final_string);
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
            JSONObject payload = null;
            try {
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
                adapter.notifyDataSetChanged();
            }
            catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
