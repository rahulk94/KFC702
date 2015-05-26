package com.getterekt.kentucy_fried_cecurity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FullListView extends ActionBarActivity {

    private List<String> listViewContent = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list_view);
        loadListWithDummyData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_list_view, menu);
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

    public void loadListWithDummyData() {
        ListView listView = (ListView) findViewById(R.id.fullListView);
        //Populate using dummy data
        listViewContent.add("Instagram\t /usr/Photos\t 2 minutes ago\t 26/05/2015\t Background");
        listViewContent.add("Facebook\t /usr/Contacts\t 10 minutes ago\t 26/05/2015\t Foreground");

        //Reverse the list so that the newest Log message is at the top
        Collections.reverse(listViewContent);

        //Set up the ListView using a basic ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        listView.setAdapter(adapter);
    }
}