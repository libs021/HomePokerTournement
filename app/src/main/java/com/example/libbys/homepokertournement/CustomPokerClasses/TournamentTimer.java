package com.example.libbys.homepokertournement.CustomPokerClasses;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages a timer used to move onto the next round
 */

public class TournamentTimer {
    private TextView mBlindsTextView, mTimeTextView, mRoundTextView;
    private Context mContext;
    private int mRound, seconds, minutes;
    private boolean onBreak;
    private Timer timer;

    public TournamentTimer(Context context, View rootView) {
        mRound = 1;
        seconds = 0;
        minutes = 20;
        mBlindsTextView = rootView.findViewById(R.id.tv_blinds);
        mTimeTextView = rootView.findViewById(R.id.tv_timer);
        mRoundTextView = rootView.findViewById(R.id.tv_round);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateSeconds();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
        mContext = context;
    }

    public void update(TextView blinds, TextView timer, TextView round, Context context) {
        mBlindsTextView = blinds;
        mTimeTextView = timer;
        mRoundTextView = round;
        mContext = context;
        updateRoundTextView();
    }

    private void updateSeconds() {
        if (seconds != 0) seconds--;
        else {
            updateMinutes();
            seconds = 59;
        }
        updateTimeView();
    }

    private void updateMinutes() {
        if (minutes != 0) minutes--;
        else {
            minutes = 19;
            updateRound();
        }
    }

    private void updateRound() {
        if (mRound % 3 == 0 && !onBreak) {
            mRoundTextView.setText(R.string.Break);
            onBreak = true;
        } else {
            if (mRound == Blinds.DEFAULT_BLINDS1500.length) {
                stopTimer();
                return;
            }
            mRound++;
            onBreak = false;
            updateRoundTextView();
        }
    }

    private void updateTimeView() {
        if (seconds < 10)
            mTimeTextView.setText(mContext.getString(R.string.TimeLeftWithPadding, minutes, seconds));
        else mTimeTextView.setText(mContext.getString(R.string.TimeLeft, minutes, seconds));
    }

    private void stopTimer() {
        timer.cancel();
        mRoundTextView.setText(R.string.Overtime);
    }

    private void updateRoundTextView() {
        if (onBreak) {
            mRoundTextView.setText(R.string.Break);
            return;
        }
        mRoundTextView.setText(mContext.getString(R.string.Round, mRound));
        int smallBlind = Blinds.DEFAULT_BLINDS1500[mRound - 1];
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, smallBlind, smallBlind * 2));
    }
}
