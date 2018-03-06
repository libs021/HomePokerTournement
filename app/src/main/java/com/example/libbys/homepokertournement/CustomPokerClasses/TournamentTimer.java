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
    private TextView mBlindsTextView, mTimeTextView, mRoundTextView;
    private Context mContext;
    private int mRound, numberOfBreaks = 0;
    private long millsRemain;

    public TournamentTimer(Context context, long timeLimit, int round, View rootView) {
        super(timeLimit, 1000);
        mContext = context;
        mRound = round;
        numberOfBreaks = mRound / 4;
        mBlindsTextView = rootView.findViewById(R.id.tv_blinds);
        mTimeTextView = rootView.findViewById(R.id.tv_timer);
        mRoundTextView = rootView.findViewById(R.id.tv_round);
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound));
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, Blinds.DEFAULT_BLINDS1500[mRound - 1], Blinds.DEFAULT_BLINDS1500[mRound - 1] * 2));
        if (mRound - numberOfBreaks + 2 > Blinds.DEFAULT_BLINDS1500.length) {
            mTimeTextView.setText("OverTime");
            return;
        }

    }


    @Override
    public void onTick(long millisUntilFinished) {
        long minutesLeft = millisUntilFinished / 60000;
        long secondsLeft = (millisUntilFinished % 60000) / 1000;
        millsRemain = millisUntilFinished;
        String timeLeft;
        if (secondsLeft >= 10)
            timeLeft = mContext.getString(R.string.TimeLeft, minutesLeft, secondsLeft);
        else timeLeft = mContext.getString(R.string.TimeLeftWithPadding, minutesLeft, secondsLeft);
        mTimeTextView.setText(timeLeft);
    }

    public void onFinish() {
        mRoundTextView.setText(String.format(mContext.getApplicationContext().getString(R.string.Round), mRound - numberOfBreaks + 1));
        mBlindsTextView.setText(mContext.getString(R.string.Blinds, Blinds.DEFAULT_BLINDS1500[mRound - numberOfBreaks], Blinds.DEFAULT_BLINDS1500[mRound - numberOfBreaks] * 2));
        //Will Keep cycling through until round 15 where the blinds will not keep rising.
        if (mRound - numberOfBreaks + 2 > Blinds.DEFAULT_BLINDS1500.length) {
            mTimeTextView.setText("OverTime");
            return;
        }
        mRound++;
        if (mRound % 4 == 0) {
            mRoundTextView.setText(R.string.Break);
            numberOfBreaks++;

        }
        this.start();
    }

    public long getMillsRemain() {
        return millsRemain;
    }

    public int getRound() {
        return mRound;
    }
}
