package com.hurricanedevelopmentstudios.android.jobsender;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText mName;
    private EditText mSubject;
    private EditText mRecipients;

    private String name;
    private String subject;
    private String emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // enable toolbar action items
        mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mName = (EditText) findViewById(R.id.nameTextField);
        mSubject = (EditText) findViewById(R.id.subjectTextField);
        mRecipients = (EditText) findViewById(R.id.recipientsTextField);

        name = Preferences.getStoredUserName(getApplicationContext());
        if (name != null) {
            mName.setText(name);
            Log.d("tag", "got name from Preferences");
        }

        subject = Preferences.getStoredSubjectLine(getApplicationContext());
        if (subject != null) {
            mSubject.setText(subject);
        }

        emails = Preferences.getStoredRecipients(getApplicationContext());
        if (emails != null) {
            mRecipients.setText(emails);
        }

        CardView cvs = (CardView)findViewById(R.id.save_card_view);
        cvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getText().toString();
                subject = mSubject.getText().toString();
                emails = mRecipients.getText().toString();

                Preferences.setStoredUserName(getApplicationContext(), name);
                Preferences.setStoredSubjectLine(getApplicationContext(), subject);
                Preferences.setStoredRecipients(getApplicationContext(), emails);

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Success");
                builder.setMessage("Your preferences have been successfully updated");
                builder.setPositiveButton("Ok", null);
                builder.show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
