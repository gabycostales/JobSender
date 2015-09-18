package com.hurricanedevelopmentstudios.android.jobsender;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.*;
import android.support.v4.app.FragmentActivity;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Object;


public class ClockInActivity extends AppCompatActivity {

    // UI OBJECTS
    private Toolbar mToolbar;
    private TextView mClockTextView;
    private TextView mClockInTimeTV;
    private TextView mClockOutTimeTV;
    private EditText mClient;
    private EditText mDescription;
    private CardView cvt;

    // APP DATA
    Date clockInDate;
    Date clockOutDate;
    String inTimeStamp;
    String outTimeStamp;
    double clockInTime = 0;
    double clockOutTime = 0;
    double totalTime = 0;
    String clockInLoc = null;
    String clockOutLoc = null;
    boolean clockedIn = false;
    boolean clockedOut = false;
    Location location;
    String emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        // enable toolbar action items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Text View objects
        mClockTextView = (TextView)findViewById(R.id.timeTextView);
        mClockInTimeTV = (TextView)findViewById(R.id.clockInTimeTextView);
        mClockOutTimeTV = (TextView)findViewById(R.id.clockOutTimeTextView);

        // Clock In and Out
        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!clockedIn){
                    clockedIn = true;

                    //Find time
                    clockInTime = System.nanoTime();
                    Log.d(String.valueOf(clockInTime), "clockin ns");
                    clockInDate = new Date();
                    inTimeStamp = new SimpleDateFormat("hh:mm a").format(clockInDate);

                    // Show user time in text view
                    mClockInTimeTV.setText(inTimeStamp);

                    //Find location
                    //clockInLoc = "http://maps.google.com/maps?z=128t=m&q=loc:" + location.getLatitude() + "+" + locatioxdon.getLongitude();
                    //Log.d(clockInLoc, "Location In");

                    // Change button
                    cvt.setCardBackgroundColor(Color.parseColor("#FF5252"));
                    mClockTextView.setText(R.string.clock_out_button);

                } else if (!clockedOut){

                    clockedOut = true;

                    // Get Time
                    clockOutTime = System.nanoTime();
                    Log.d(String.valueOf(clockOutTime), "clockout ns");
                    clockOutDate  = new Date();
                    outTimeStamp = new SimpleDateFormat("hh:mm a").format(clockOutDate);
                    Log.d(outTimeStamp, "Timestamp out");

                    // Show user time in text view
                    mClockOutTimeTV.setText(outTimeStamp);

                    // Get Location
                    //clockOutLoc = "http://maps.google.com/maps?z=128t=m&q=loc:" + location.getLatitude() + "+" + location.getLongitude();
                    //Log.d(clockOutLoc, "Location Out");

                    totalTime = clockOutTime - clockInTime;
                    Log.d(String.valueOf(totalTime), "Total time");

                    // change clock out button to be inactive
                    cvt.setCardBackgroundColor(R.color.material_grey_600);
                    mClockTextView.setText(R.string.done_button);

                    // Alert user to hit send to send email
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Job Summary");
                    builder.setMessage("You worked a total of " + totalTime + " hours." +
                            "\n Press the SEND button to send this job to your employer.");
                    builder.setPositiveButton("Ok", null);
                    builder.show();

                } else if (clockedIn && clockedOut){
                    // This all may not be necessary - we'll see.
                    // Button should do nothing
                }
            }
        });

        // Delete clock in and out information
        CardView cvc = (CardView)findViewById(R.id.clear_card_view);
        cvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Clear Confirmation");
                    builder.setMessage("Are you sure you want to delete your clock in data?" +
                            " You will not be able to retrieve any of the information from this job.");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // CLEAR CLOCK IN DATA

                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
            }
        });

        // SEND
        CardView cvs = (CardView)findViewById(R.id.send_card_view);
        cvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Send Confirmation");
                builder.setMessage("Are you sure you want to send these times and locations?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Grab information
                        mClient = (EditText) findViewById(R.id.client_etext_field);
                        String clientName = mClient.getText().toString();
                        mDescription = (EditText) findViewById(R.id.desc_etext_field);
                        String jobDesc = mDescription.getText().toString();
                        // Compile email body
                        emailBody = buildEmailBody();
                        // Send email
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender("jobsendernoreply@gmail.com",
                                            "g33k$r00l");
                                    sender.sendMail(Preferences.getStoredSubjectLine(getApplicationContext()),
                                            emailBody,
                                            "jobsendernoreply@gmail.com",
                                            Preferences.getStoredRecipients(getApplicationContext()));
                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage(), e);
                                }
                            }
                        }).start();
                    }
                });
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

    public String buildEmailBody() {
        String body = "";

        body += Preferences.getStoredUserName(getApplicationContext()) + "\n";

        body += "Punch In: \t";
        DateFormat calDateFormat = DateFormat.getDateInstance();
        body += calDateFormat.format(clockInDate) + "\t";
        body += inTimeStamp + "\n";

        body += "Punch Out: \t";
        body += calDateFormat.format(clockOutDate) + "\t";
        body += outTimeStamp + "\n";

        body += "Punch In Location: \n\n";
        body += "Punch Out Location: \n\n";

        body += "Client: \n" + mClient.getText().toString() + "\n";
        body += "Description: \n" + mDescription.getText().toString();

        Log.d("body", body);
        return body;
    }
}
