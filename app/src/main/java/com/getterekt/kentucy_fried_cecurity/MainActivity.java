package com.getterekt.kentucy_fried_cecurity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;
import com.getterekt.kentucy_fried_cecurity.Java.FileAccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity {

    RunLogcatInBackground logcat;
    RunLogcatInBackground task;
    //Contains a List of logging messages from apps. No duplicates exist in the List
    private static List<String> listViewContent = new ArrayList<String>();
    private static List<String> listViewAccess = new ArrayList<String>();
    private static List<String> listViewTime = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.v("CS702", "OnCreate");
        task = new RunLogcatInBackground();
        task.execute();
        loadList();
    }

    protected void onRestart() {
        super.onRestart();
        task = new RunLogcatInBackground();
        task.execute();
    }

    protected void onResume() {
        super.onResume();
        System.out.println("Has reseumde");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Start the full list view activity
    public void fullListViewLoad(View v) {
        Log.v("CS702", "Full list view button pushed");
        Intent i = new Intent(this, FullListView.class);
        startActivity(i);

    }

    //Start the permissions view activity
    public void permissionsViewLoad(View v) {
        Log.v("CS702", "Permissions button pushed");
        Intent i = new Intent(this, PermissionsView.class);
        startActivity(i);
    }

    public void loadList() {
        ListView listView = (ListView) findViewById(R.id.summaryList);

        //Reverse the list so that the newest Log message is at the top
        Collections.reverse(listViewContent);

        //Set up the ListView using a basic ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        listView.setAdapter(adapter);

//        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewTime);
//        listView3.setAdapter(adapterTime);
    }

    //A inner class that runs a process in the background
    public class RunLogcatInBackground extends AsyncTask<Void, String, Void> {

        private static final String TAG = "BackgroundProcessTag";
        private static final int DELAY = 5000; // 5 seconds
        private static final int RANDOM_MULTIPLIER = 10;

        //Log execution methods
        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Starting the background task");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.v(TAG, "reporting back from the Random Number Task");
//            updateResults(values[0].toString());
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void result) {
            Log.v(TAG, "Cancelled the background task");
            super.onCancelled(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v(TAG, "Doing work in the background task");
            try {
                //Execute this command in the background
                Process process = Runtime.getRuntime().exec("su logcat CS702");
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                //While there is another line available for the bufferedReader to read
                while((line = br.readLine()) != null)
                {

                    if (line.contains("CS702")) {
                        int index = line.indexOf("CS702");
                        String tag = line.substring(index);
                        String message = tag.substring(tag.indexOf(":")+1);

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String currentDate = dateFormat.format(date);

                        if (!(listViewContent.contains(message))) {
                            listViewContent.add(message);
//                            listViewTime.add(currentDate);
                        } else {
                            listViewContent.remove(message);
//                            listViewTime.remove(currentDate);
                            listViewContent.add(message);
//                            listViewTime.add(currentDate);

                        }
                    }

//                    int index = line.indexOf("CS702");
//                    //If the line contains CS702
//                    if (index != -1) {
//                        //Format the String and add it to the List of messages being displayed
//                        String tag = line.substring(index);
//                        String message = tag.substring(tag.indexOf(":")+1);
//
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                        Date date = new Date();
//                        String currentDate = dateFormat.format(date);
//
//                        if (!(listViewContent.contains(message))) {
//                            listViewContent.add(message);
////                            listViewTime.add(currentDate);
//                        } else {
//                            listViewContent.remove(message);
////                            listViewTime.remove(currentDate);
//                            listViewContent.add(message);
////                            listViewTime.add(currentDate);
//
//                        }
//                    }
                }
            }
            catch(IOException e) {
            }

            return null;
        }

    }

}