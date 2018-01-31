package com.example.libbys.homepokertournement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;

import java.util.Calendar;


public class TournamentActivity extends AppCompatActivity {
    private int myear;
    private int mmonth;
    private int mday;
    private int mHour;
    private int mMinute;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        EditText startDate = findViewById(R.id.StartDate);
        EditText startTime = findViewById(R.id.StartTime);
        updateCalendar();
        setUpCalenderPicker(startDate);
        setUpTimePicker(startTime);
        Button button = findViewById(R.id.NewTournament);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creates a new Tournament with the selected Values
                createNewTournament();
            }
        });
    }

    //sets on touchLister t
    private void setUpCalenderPicker(final EditText editText) {
        final DatePickerDialog.OnDateSetListener DataListener = (new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String stringMonth = String.valueOf(month);
                if (stringMonth.length() == 1) stringMonth = "0" + stringMonth;
                String stringDay = String.valueOf(day);
                if (stringDay.length() == 1) stringDay = "0" + stringDay;
                editText.setText(year + "-" + stringMonth + "-" + stringDay);
            }
        });
        final DatePickerDialog datePicker = new DatePickerDialog(this, DataListener, myear, mmonth, mday);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                datePicker.show();
                return true;
            }
        });

    }

    private void setUpTimePicker(final EditText editText) {
        final TimePickerDialog.OnTimeSetListener DataListener = (new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                String stringMinute = String.valueOf(minute);
                if (stringMinute.length() == 1) stringMinute = "0" + stringMinute;
                String stringHour = String.valueOf(hour);
                if (stringHour.length() == 1) stringHour = "0" + stringHour;
                editText.setText(stringHour + ":" + stringMinute + ":00");

            }
        });
        final TimePickerDialog timePicker = new TimePickerDialog(TournamentActivity.this, AlertDialog.THEME_TRADITIONAL, DataListener, mHour, mMinute, true);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                timePicker.show();
                return true;
            }


        });

    }

    public void updateCalendar() {
        Calendar calendar = Calendar.getInstance();
        myear = calendar.get(Calendar.YEAR);
        mmonth = calendar.get(Calendar.MONTH);
        mday = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE) + 10;
    }

    private void createNewTournament() {
        //Start by getting all of the information from the user Editable Fields.
        Spinner gameEditText = findViewById(R.id.GameEditText);
        EditText costEditText = findViewById(R.id.costEditText);
        EditText startDate = findViewById(R.id.StartDate);
        EditText startTime = findViewById(R.id.StartTime);


        String game = (String) gameEditText.getSelectedItem();
        String date = startDate.getText().toString().trim();
        String time = startTime.getText().toString().trim();
        String costString = costEditText.getText().toString().trim();
        int cost = Integer.parseInt(costString);
        String dateTime = date + " " + time;
        ContentValues newTournament = new ContentValues();
        newTournament.put(PokerContract.TournamentEntry.GAME, game);
        newTournament.put(PokerContract.TournamentEntry.STARTTIME, dateTime);
        newTournament.put(PokerContract.TournamentEntry.COST, cost);
        Uri uri = getContentResolver().insert(PokerContract.TournamentEntry.CONTENT_URI, newTournament);
        String id = uri.getLastPathSegment();
        finish();
        if (!id.equals("-1")) {
            Toast.makeText(this, "Your entered in Tournament with ID " + id, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TournamentActivity.this, PlayerListActivity.class);
            intent.setData(uri);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Tournament must start on a future date", Toast.LENGTH_LONG).show();

        }
    }
}
