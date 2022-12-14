package com.cst2335.mealdatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.mealdatabase.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityMealDB extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<String> mealList;
    private Handler mainHandler = new Handler();
    private EditText input;
    private Button btnSearch;
    private ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata");  //Type 1


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        input = (EditText) findViewById(R.id.txtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        simpleList = (ListView) findViewById(R.id.txtResults);

        binding.btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.favourites);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString()==null || input.getText().toString().trim().equals("")){
                }
                else {
                    //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), mealList);
                    //simpleList.setAdapter(adapter);
                }
            }
        });
    }

    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        static private final String TAG = "MyHTTPRequest";

        //Type3                Type1
        public String doInBackground(String... args) {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                int numEntries = uvReport.getInt("count");

                publishProgress(25);
                Thread.sleep(1000);
                publishProgress(50);
                Log.i(TAG, "Num of entries: " + numEntries);

            } catch (Exception e) {

            }

            return "Done";
        }

        String data = "";

        class getMeals extends Thread {
            @Override
            public void run() {


                try {
                    URL url = new URL("https://www.themealdb.com/api/json/v1/1/search.php?f=a%22");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream iS = httpURLConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(iS));
                    String line;

                    while ((line = buffer.readLine()) != null) {
                        data = data + line;
                    }

                    if (!data.isEmpty()) {
                        JSONObject object = new JSONObject(data);
                        JSONArray meals = object.getJSONArray("meals");
                        mealList.clear();
                        for (int i = 0; i < meals.length(); i++) {

                            JSONObject mealNames = meals.getJSONObject(i);
                            String meal = mealNames.getString("meal");
                            mealList.add(meal);
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }


        //Type 2
        public void onProgressUpdate(Integer... args) {
            Log.i(TAG, "onProgressUpdate");
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            Log.i(TAG, fromDoInBackground);
        }
    }
}