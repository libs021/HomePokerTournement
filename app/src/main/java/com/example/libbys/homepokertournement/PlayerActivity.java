package com.example.libbys.homepokertournement;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libbys.homepokertournement.DataBaseFiles.PlayerContract;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Button AddplayerButton = findViewById(R.id.button);
        final EditText PlayerName = findViewById(R.id.nameEditText);
        AddplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addaPlayer(PlayerName);
            }
        });
    }

    private void addaPlayer(EditText nameEditText) {
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            //Show user an error message and then do nothing
            Toast.makeText(this, getString(R.string.PlayerNameError), Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(PlayerContract.PlayerEntry.NAME, name);
        getContentResolver().insert(PlayerContract.PlayerEntry.CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.AddedPlayer, name), Toast.LENGTH_LONG).show();
        finish();
    }

}
