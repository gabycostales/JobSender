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

public class ClockInActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);

        // enable toolbar action items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //
        CardView cvt = (CardView)findViewById(R.id.time_card_view);
        cvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
