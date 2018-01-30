package com.example.libbys.homepokertournement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;

import java.util.ArrayList;

/**
 * Created by Libby's on 1/29/2018.
 */

public class TournamentProgress extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentprogress);
        ListView listView = findViewById(R.id.inTournament);
        ArrayList<TournamentPlayer> players = new ArrayList<>();
        players.add(new TournamentPlayer("Joel", 1500));
        players.add(new TournamentPlayer("Josh", 1500));
        players.add(new TournamentPlayer("Jason", 1500));
        players.add(new TournamentPlayer("Laurin", 1500));
        players.add(new TournamentPlayer("Nick", 1500));
        players.add(new TournamentPlayer("Robert", 1500));
        TournamentPlayerAdapter adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView.setAdapter(adapter);
    }
}
