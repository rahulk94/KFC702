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

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class MainActivity extends ActionBarActivity {

    RunLogcatInBackground logcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RunLogcatInBackground task = new RunLogcatInBackground();
        task.execute();
        loadList();
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

    public void fullListViewLoad(View v) {
        Log.v("CS702", "Full list view button pushed");
        Intent i = new Intent(this, FullListView.class);
        startActivity(i);

    }

    public void permissionsViewLoad(View v) {
        Log.v("CS702", "Permissions button pushed");
        Intent i = new Intent(this, PermissionsView.class);
        startActivity(i);
    }

    public void updateResults (String results) {
        TextView tv = (TextView) findViewById(R.id.logCatTextView);
        tv.append(results);
    }

    public void loadList() {
//        Log.v("CS702", "List loaded");
//        ListView listView = (ListView) findViewById(R.id.summaryList);
//        List<FileAccess> fileAccessList = new ArrayList<FileAccess>();
//        fileAccessList.add(new FileAccess("App", "Accessed", "Time") {
//            public String toString() {
//                return "App\t\t  Accessed\t\t  Time";
//            }
//        });
//
//        fileAccessList.add(new FileAccess("Facebook", "usr/folders/photos", "2min ago"));
//        fileAccessList.add(new FileAccess("Instagram", "usr/folders/videos", "12pm"));
//        fileAccessList.add(new FileAccess("Firedroid", "usr/folders/contacts", "10am"));
//
//        ArrayAdapter<FileAccess> adapter = new ArrayAdapter<FileAccess>(this, android.R.layout.simple_list_item_1, fileAccessList);
//        listView.setAdapter(adapter);

    }

    public class RunLogcatInBackground extends AsyncTask<Void, String, Void> {

        private static final String TAG = "DoSomethingTask";
        private static final int DELAY = 5000; // 5 seconds
        private static final int RANDOM_MULTIPLIER = 10;

        @Override
        protected void onPreExecute() {
            Log.v(TAG, "starting the Random Number Task");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.v(TAG, "reporting back from the Random Number Task");
            updateResults(values[0].toString());
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void result) {
            Log.v(TAG, "cancelled the Random Number Task");
            super.onCancelled(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v(TAG, "doing work in Random Number Task");
            try {
                Process process = Runtime.getRuntime().exec("logcat -s CS702");
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

                //int character = 0;
                String line;

                while((line = br.readLine()) != null)
                {
                    //tv.append("" + character);
                    int index = line.indexOf("CS702");
                    if (index != -1) {
                        String tag = line.substring(index);
                        String message = tag.substring(tag.indexOf(":"));
                        publishProgress("LINE - " + tag);
                    }
                }
            }
            catch(IOException e) {

            }
            return null;
        }

    }

}