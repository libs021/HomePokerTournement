package com.example.libbys.homepokertournement;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.libbys.homepokertournement.DataBaseFiles.PlayerContract;
import com.example.libbys.homepokertournement.DataBaseFiles.PokerCursorAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] toSelect = {PlayerContract.PlayerEntry._ID, PlayerContract.PlayerEntry.NAME,
                PlayerContract.PlayerEntry.BUYIN, PlayerContract.PlayerEntry.CASHOUT, PlayerContract.PlayerEntry.WIN};
        ContentValues values = new ContentValues();
        values.put(PlayerContract.PlayerEntry.NAME, "Joel the magnificent");
        getContentResolver().insert(PlayerContract.PlayerEntry.CONTENT_URI, values);
        Cursor cursor = getContentResolver().query(PlayerContract.PlayerEntry.CONTENT_URI, toSelect, null, null, null);
        ListView listView = findViewById(R.id.PlayerListView);
        PokerCursorAdapter cursorAdapter = new PokerCursorAdapter(this, cursor, 0);
        listView.setAdapter(cursorAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
}
