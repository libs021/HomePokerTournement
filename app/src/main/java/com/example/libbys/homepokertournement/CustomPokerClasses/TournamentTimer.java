package com.example.libbys.homepokertournement.CustomPokerClasses;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

/**
 * Manages a timer used to move onto the next round
 */

public class TournamentTimer extends CountDownTimer {
    private int mSmallBlind;
    private int mRound;
    private TextView mBlindsTextView;
    private TextView mTimeTextView;
    private TextView mRoundTextView;
    private Context mContext;

    public TournamentTimer(Context context, long timeLimit, int round, int smallBlind, View rootView) {
        super(timeLimit, 1000);
        mContext = context;
        mRound = round;
        mSmallBlind = smallBlind;
        mBlindsTextView = rootView.findViewById(R.id.blinds);
        mTimeTextView = rootView.findViewById(R.id.timer);
        mRoundTextView = rootView.findViewById(R.id.roundTracker);
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound));
        mBlindsTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Blinds), smallBlind, smallBlind * 2));
        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }


    @Override
    public void onTick(long millisUntilFinished) {
        long minutesLeft = millisUntilFinished / 60000;
        long secondsLeft = (millisUntilFinished % 60000) / 1000;
        String timeLeft;
        if (secondsLeft >= 10)
            timeLeft = mContext.getString(R.string.TimeLeft, minutesLeft, secondsLeft);
        else timeLeft = mContext.getString(R.string.TimeLeftWithPadding, minutesLeft, secondsLeft);
        mTimeTextView.setText(timeLeft);
    }

    public void onFinish() {
        mSmallBlind = mSmallBlind * 2;
        mRound = mRound + 1;
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound));
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, mSmallBlind, mSmallBlind * 2));
        //Will Keep cycling through rounds until the tournament is manually ended.
        this.start();
    }
}
