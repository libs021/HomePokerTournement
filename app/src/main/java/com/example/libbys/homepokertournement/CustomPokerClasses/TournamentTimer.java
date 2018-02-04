package com.example.libbys.homepokertournement.CustomPokerClasses;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.libbys.homepokertournement.R;

/**
 * Created by Libby's on 2/1/2018.
 */

public class TournamentTimer extends CountDownTimer {
    long mTimeLimit;
    int mSmallBlind;
    int mRound;
    View mRoot;
    TextView mBlindsTextView;
    TextView mTimeTextView;
    TextView mRoundTextView;
    Context mContext;

    public TournamentTimer(Context context, long timeLimit, int round, int smallBlind, View rootView) {
        super(timeLimit, 1000);
        mContext = context;
        mRound = round;
        mSmallBlind = smallBlind;
        mRoot = rootView;
        mBlindsTextView = rootView.findViewById(R.id.blinds);
        mTimeTextView = rootView.findViewById(R.id.timer);
        mRoundTextView = rootView.findViewById(R.id.roundTracker);
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
        mRound = mRound + 1;
        mRoundTextView.setText("Round: " + mRound);
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, mSmallBlind, mSmallBlind * 2));
        mSmallBlind = mSmallBlind * 2;
        //Will Keep cycling through rounds until the tournament is manually ended.
        this.start();
    }
}
