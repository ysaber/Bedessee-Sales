package com.thebedesseegroup.sales.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.main.MainActivity;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.utilities.Utilities;

/**
 * Login screen.
 */
public class Login extends Activity {

    final private static int FILE_CODE = 23;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        MixPanelManager.trackScreenView(this, "Login screen");

        final SharedPrefsManager sharedPrefs = new SharedPrefsManager(this);

        if(sharedPrefs.getSugarSyncDir() == null) {

            final Intent intent = new Intent(Login.this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
            intent.putExtra(FilePickerActivity.EXTRA_TITLE, "PLEASE SELECT SUGAR SYNC FOLDER...");
            startActivityForResult(intent, FILE_CODE);
        } else {

            GoogleApiHelper.init(getApplicationContext(), this);

            mLoginButton = (SignInButton) findViewById(R.id.sign_in_button);
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utilities.isInternetPresent(Login.this)) {
                        mLoginButton.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        GoogleApiHelper.getOnClickListener().onClick(v);
                    } else {
                        Utilities.longToast(Login.this, "No internet connection found!");
                    }
                }
            });

            mGoogleApiClient = GoogleApiHelper.getGoogleApiClient();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);

        String loggedInUserEmail = sharedPrefsManager.getLoggedInUser();

        if(!loggedInUserEmail.isEmpty()) {
            mLoginButton.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            MixPanelManager.trackSuccessfulLogin(this, true);

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == GoogleApiHelper.RC_SIGN_IN) {
            GoogleApiHelper.sIntentInProgress = false;
            if (responseCode != RESULT_OK) {
                GoogleApiHelper.sSignInClicked = false;
            }

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        if (requestCode == FILE_CODE && responseCode == Activity.RESULT_OK) {
            final Uri uri = intent.getData();
            final String path = uri.getPath();
            final SharedPrefsManager sharedPrefs = new SharedPrefsManager(this);
            sharedPrefs.setSugarSyncDir(path);

            onCreate(null);

        } else if (requestCode == FILE_CODE && responseCode == Activity.RESULT_CANCELED) {
            finish();
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
