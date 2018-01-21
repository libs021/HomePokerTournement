package com.example.libbys.homepokertournement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Libby's on 1/21/2018.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Button addPlayerButton = findViewById(R.id.AddPlayer);
        Button viewPlayerButton = findViewById(R.id.ViewPlayer);
        Button createTournamentButton = findViewById(R.id.NewTournament);

        //Set buttons to be clickable. They all just switch activities
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });

        viewPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlayerListActivity.class);
                startActivity(intent);
            }
        });

        createTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TournamentActivity.class);
                startActivity(intent);
            }
        });


    }
}
