package com.hurricanedevelopmentstudios.android.jobsender;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockInActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mClockTextView;
    private TextView mClockInTextView;
    private TextView mClockOutTextView;
    private CardView cvt;

    double clockInTime = 0;
    double clockOutTime= 0;
    double totalTime = 0;
    String clockInLoc = null;
    String clockOutLoc = null;
    boolean clockedIn = false;
    boolean clockedOut = false;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        // enable toolbar action items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mClockTextView = (TextView)findViewById(R.id.timeTextView);
        mClockInTextView = (TextView)findViewById(R.id.clockInTextView);
        mClockOutTextView = (TextView)findViewById(R.id.clockInTextView);

        //
        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3 states: before clock in, clocked in, clocked out
                if (!clockedIn){
                    //Find time
                    clockInTime = System.nanoTime();
                    Log.d(String.valueOf(clockInTime), "clockin ns");
                    String inTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    Log.d(inTimestamp, "Timestamp in");
                    //Find location
                    clockInLoc = "http://maps.google.com/maps?z=128t=m&q=loc:" + location.getLatitude() + "+" + location.getLongitude();
                    Log.d(clockInLoc, "Location In");
                    //Know you clocked in
                    clockedIn = true;
                    //Add time to clock in UI
                    //mClockInTextView.setText(R.string.);
                    //At this point - change button to "Clock Out"
                    cvt.setCardBackgroundColor(R.color.red);
                    mClockTextView.setText(R.string.clock_out_text);
                } else if (!clockedOut){
                    clockOutTime = System.nanoTime();
                    Log.d(String.valueOf(clockOutTime), "clockout ns");
                    String outTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    Log.d(outTimestamp, "Timestamp out");
                    clockOutLoc = "http://maps.google.com/maps?z=128t=m&q=loc:" + location.getLatitude() + "+" + location.getLongitude();
                    Log.d(clockOutLoc, "Location Out");
                    clockedOut = true;
                    totalTime = clockOutTime - clockInTime;
                    Log.d(String.valueOf(totalTime), "Total time");
                    //change clock out button to be inactive
                    cvt.setCardBackgroundColor(R.color.material_grey_600);
                    mClockTextView.setText(R.string.done_button);
                } else if (clockedIn && clockedOut){
                    //This all may not be necessary - we'll see.
//                    AlertDialog.Builder builder = new AlertDialog().Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
//                    builder.setTitle("Job Summary");
//                    builder.setMessage("You worked a total of " + totalTime + " hours. \n Press the SEND button to send this job to your employer.");
//                    builder.setPositiveButton("YES", null);
//                    builder.show();
                }
            }
        });

        CardView cvc = (CardView)findViewById(R.id.clear_card_view);
        cvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Clear Confirmation");
                    builder.setMessage("Are you sure you want to delete your clock in data?" +
                            " You will not be able to retrieve any of the information from this job.");
                    builder.setPositiveButton("YES", null);
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
            }
        });

        CardView cvs = (CardView)findViewById(R.id.send_card_view);
        cvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Send Confirmation");
                builder.setMessage("Are you sure you want to send these times and locations?");
                builder.setPositiveButton("YES", null);
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
