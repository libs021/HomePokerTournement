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
    private int mRound;
    private TextView mBlindsTextView;
    private TextView mTimeTextView;
    private TextView mRoundTextView;
    private Context mContext;
    private View mrootView;
    private int numberOfBreaks = 0;

    public TournamentTimer(Context context, long timeLimit, int round, View rootView) {
        super(timeLimit, 1000);
        mContext = context;
        mRound = round;
        mBlindsTextView = rootView.findViewById(R.id.blinds);
        mTimeTextView = rootView.findViewById(R.id.timer);
        mRoundTextView = rootView.findViewById(R.id.roundTracker);
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound));
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, Blinds.DEFAULT_BLINDS1500[mRound - 1], Blinds.DEFAULT_BLINDS1500[mRound - 1] * 2));
        mrootView = rootView;
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
        mRound = mRound + 1;
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound - numberOfBreaks));
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, Blinds.DEFAULT_BLINDS1500[mRound - 1 - numberOfBreaks], Blinds.DEFAULT_BLINDS1500[mRound - 1 - numberOfBreaks] * 2));
        //Will Keep cycling through rounds until the tournament is manually ended.
        if (mRound % 4 == 0) {
            mRoundTextView.setText(R.string.Break);
            numberOfBreaks++;

        }
        this.start();
    }
}
