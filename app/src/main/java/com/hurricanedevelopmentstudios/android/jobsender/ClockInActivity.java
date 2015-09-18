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
import java.util.Calendar;
import java.util.Date;


public class ClockInActivity extends AppCompatActivity {

    // Member variables
    private Toolbar mToolbar;
    private TextView mClockTextView;
    private TextView mClockInTimeTV;
    private TextView mClockOutTimeTV;
    private EditText mClient;
    private EditText mDescription;
    private CardView cvt;

    // non-persistent variables
    Date clockInDate;
    Date clockOutDate;
    String inTimeStamp;
    String outTimeStamp;
    long clockInDateMilis;
    long clockOutDateMilis;

    MyLocation clockInLoc;
    MyLocation clockOutLoc;

    String firstLocation = "";
    String finalLocation = "";

    boolean clockedIn = false;
    boolean clockedOut = false;

    String emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        // enable toolbar action items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // References to inflated widgets
        mClockTextView = (TextView)findViewById(R.id.timeTextView);
        mClockInTimeTV = (TextView)findViewById(R.id.clockInTextView);
        mClockOutTimeTV = (TextView)findViewById(R.id.clockOutTextView);
        mClient = (EditText) findViewById(R.id.client_etext_field);
        mDescription = (EditText) findViewById(R.id.desc_etext_field);

        // Populate data if user is still currently on a job but the activity died


        clockInDateMilis = Preferences.getStoredClockInDate(getApplicationContext());
        clockOutDateMilis = Preferences.getStoredClockOutDate(getApplicationContext());

        if ( clockOutDateMilis != 0 ) {

            clockedIn = true;
            clockedOut = true;

            clockInDate = new Date(clockInDateMilis);
            inTimeStamp = new SimpleDateFormat("hh:mm a").format(clockInDate);
            mClockInTimeTV.setText(inTimeStamp);
            // pull clock in location because one must be saved

            clockOutDate = new Date(clockOutDateMilis);
            outTimeStamp = new SimpleDateFormat("hh:mm a").format(clockOutDate);
            mClockOutTimeTV.setText(outTimeStamp);
            // pull clock out location because one must be saved

            setButtonToDone();

        } else if ( clockInDateMilis != 0 ) {

            clockedIn = true;

            clockInDate = new Date(clockInDateMilis);
            inTimeStamp = new SimpleDateFormat("hh:mm a").format(clockInDate);
            mClockInTimeTV.setText(inTimeStamp);
            // pull clock in location because one must be saved

            setButtonToClockOut();


        }


        // Clock In and Out
        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!clockedIn){

                    clockedIn = true;

                    // Set clock in time
                    clockInDate = new Date();
                    // save to shared preferences
                    clockInDateMilis = clockInDate.getTime();
                    Preferences.setStoredClockInDate(getApplicationContext(), clockInDateMilis);
                    // update UI
                    inTimeStamp = new SimpleDateFormat("hh:mm a").format(clockInDate);
                    mClockInTimeTV.setText(inTimeStamp);

                    //Find location
                    //Create Class object
                    clockInLoc = new MyLocation(ClockInActivity.this);
                    //check if GPS enabled
                    if(clockInLoc.canGetLocation()){
                        double latitude = clockInLoc.getLatitude();
                        double longitude = clockInLoc.getLongitude();
                        //append to global string location variable
                        firstLocation = "http://maps.google.com?q=" + latitude + "," + longitude;
                    } else {
                        clockInLoc.showSettingsAlert();
                    }

                    setButtonToClockOut();

                    // Change button
                    cvt.setCardBackgroundColor(Color.parseColor("#FF5252"));
                    mClockTextView.setText(R.string.clock_out_button);

                } else if (!clockedOut){

                    clockedOut = true;

                    // Set clock out time
                    clockOutDate  = new Date();
                    // save to shared preferences
                    clockOutDateMilis = clockOutDate.getTime();
                    Preferences.setStoredClockOutDate(getApplicationContext(), clockOutDateMilis);
                    // update UI
                    outTimeStamp = new SimpleDateFormat("hh:mm a").format(clockOutDate);
                    mClockOutTimeTV.setText(outTimeStamp);

                    // Set clock out location
                    // pull location info
                    // append string of google maps url with lat and long info
                    // save to shared preferences as current job clock out location
                    clockOutLoc = new MyLocation(ClockInActivity.this);
                    //check if GPS enabled
                    if(clockOutLoc.canGetLocation()){
                        double latitude = clockOutLoc.getLatitude();
                        double longitude = clockOutLoc.getLongitude();
                        //append to global string location variable
                        finalLocation = "http://maps.google.com?q=" + latitude + "," + longitude;
                    } else {
                        clockOutLoc.showSettingsAlert();
                    }

                    setButtonToDone();

                    // Remind user to hit Send to actually send the job
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClockInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Job Summary");
                    builder.setMessage("You worked a total of " +
                            getElapsedTime(clockInDate, clockOutDate) +
                            "\n Press the SEND button to send this job to your employer.");
                    builder.setPositiveButton("Ok", null);
                    builder.show();

                } else if (clockedIn && clockedOut){
                    // This all may not be necessary - we'll see.
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
                            clearCurrentJob();

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

    @Override
    public void onPause() {
        super.onPause();

        Preferences.setStoredClient(getApplicationContext(), mClient.getText().toString());
        Preferences.setStoredDescription(getApplicationContext(), mDescription.getText().toString());

    }

    @Override
    public void onResume() {
        super.onResume();

        String clientText = Preferences.getStoredClient(getApplicationContext());
        if ( clientText != null && clientText != "" ) {
            mClient.setText(clientText);
        }

        String descText = Preferences.getStoredDescription(getApplicationContext());
        if ( descText != null && descText != "" ) {
            mDescription.setText(descText);
        }

    }

    public String buildEmailBody() {

        String body = "";

        body += Preferences.getStoredUserName(getApplicationContext()) + "\n";

        body += "Punch In: \t";
        DateFormat calDateFormat = DateFormat.getDateInstance();
        body += calDateFormat.format(clockInDate) + "\t";
        body += inTimeStamp + "\t@\t";
        body += "\n"; // append location in string

        body += "Punch Out: \t";
        body += calDateFormat.format(clockOutDate) + "\t";
        body += outTimeStamp + "\t@\t";
        body += "\n"; // append location out string

        body += "Total time spent: " +  getElapsedTime(clockInDate, clockOutDate) + " \n";

        body += "Client: \n" + mClient.getText().toString() + "\n";
        body += "Description: \n" + mDescription.getText().toString();

        Log.d("body", body);
        return body;

    }

    private String getElapsedTime (Date clockInDate, Date clockOutDate) {

        Calendar calIn = Calendar.getInstance();
        Calendar calOut = Calendar.getInstance();
        calIn.setTime(clockInDate);
        calOut.setTime(clockOutDate);
        long milisIn = calIn.getTimeInMillis();
        long milisOut = calOut.getTimeInMillis();
        long diff = milisOut - milisIn;

        long elapsedTime = diff / 1000;
        String format = String.format("%%0%dd", 2);
        String minutes = String.format(format, ((elapsedTime % 3600) / 60));
        String hours = String.format(format, (elapsedTime / 3600));

        return hours + " hour(s) and " + minutes + " minutes.";

    }


    private void setButtonToClockIn () {

        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setCardBackgroundColor(Color.parseColor("#4CAF50"));
        mClockTextView.setText(R.string.clock_in_button);
        return;
    }

    private void setButtonToClockOut () {

        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setCardBackgroundColor(Color.parseColor("#FF5252"));
        mClockTextView.setText(R.string.clock_out_button);
        return;

    }

    private void setButtonToDone () {

        cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setCardBackgroundColor(R.color.material_grey_600);
        mClockTextView.setText(R.string.done_button);
        return;

    }


    private void clearCurrentJob () {

        // CLEAR JOB DATA
        Preferences.setStoredClockInDate(getApplicationContext(), 0);
        Preferences.setStoredClockOutDate(getApplicationContext(), 0);
        Preferences.setStoredInLocation(getApplicationContext(), null);
        Preferences.setStoredOutLocation(getApplicationContext(), null);
        Preferences.setStoredClient(getApplicationContext(), null);
        Preferences.setStoredDescription(getApplicationContext(), null);
        // RESET UI
        setButtonToClockIn();
        mClockInTimeTV.setText("N/A");
        mClockOutTimeTV.setText("N/A");
        mClient.setText("");
        mDescription.setText("");
        // set correct states
        clockedIn = false;
        clockedOut = false;

    }
}
