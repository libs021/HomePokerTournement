package com.example.libbys.homepokertournement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.libbys.homepokertournement.ArrayAdapters.TournamentPlayerAdapter;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayer;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentPlayerDialog;
import com.example.libbys.homepokertournement.CustomPokerClasses.TournamentTimer;

import java.util.ArrayList;
import java.util.Collections;

/**
 Activity to manage and display information related to the current Tournament that is in progress.
 Includes a time and creates an array of players to manage the tournament
 */

public class TournamentProgress extends AppCompatActivity implements TournamentPlayerDialog.TournamentPlayerInterface {
    private static final int GETPLAYERLOADER = 10;
    private static final int GETSTARTINGCHIPCOUNTLOADER = 17;
    ArrayList<TournamentPlayer> players;
    TournamentPlayerAdapter adapter;
    TournamentTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentprogress);
        ListView listView = findViewById(R.id.inTournament);
        players = new ArrayList<>();
        players.add(new TournamentPlayer("Joel", 1500));
        adapter = new TournamentPlayerAdapter(this, R.layout.playerintournamentlistview, players);
        listView.setAdapter(adapter);
        View rootView = findViewById(R.id.tournamentInProgressRootView);
        timer = new TournamentTimer(this, 15000, 1, 20, rootView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager manager = getSupportFragmentManager();
                TournamentPlayerDialog dialog = new TournamentPlayerDialog();
                Bundle args = new Bundle();
                args.putInt("PlayerToUpdate", i);
                dialog.setArguments(args);
                dialog.show(manager, "ThisDialog");
            }
        });


    }


    @Override
    public void bust(int playerPosition) {
        players.get(playerPosition).setmChipCount(0);
        Collections.sort(players);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void updatePlayer(int playerPosition, int count) {
        players.get(playerPosition).setmChipCount(count);
        Collections.sort(players);
        adapter.notifyDataSetChanged();

    }
}
