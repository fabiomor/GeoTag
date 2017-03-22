package com.fabio.gis.geotag.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fabio.gis.geotag.R;
import com.fabio.gis.geotag.model.helper.ServerManager;
import com.fabio.gis.geotag.model.data.DataModel;
import com.fabio.gis.geotag.model.helper.Constants;
import com.fabio.gis.geotag.model.helper.JsonHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fabio on 01/03/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG =  LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;
    private SharedPreferences sharedPreferences;
    private int responseCode;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        /*
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        */
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: 16/03/2017 opzione rimani registrato

        new LoginValidatorTask().execute(Constants.TOMAP.HTTP.BASE_URL + "/users?username=" + email + "&pwd=" + password);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                //sharedPreferences.edit().putString();
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
    */

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 2 || password.length() > 10) {
            _passwordText.setError("between 2 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class LoginValidatorTask  extends AsyncTask<String, Void, Integer> {


        private static final int RESULT_OK = 0;
        private static final int RESULT_FAILED = 1;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.authenticating_dialog));
            progressDialog.show();
        }

        protected Integer doInBackground(String... urls) {
            try {
                HttpURLConnection connection = ServerManager.httpGetConnection(urls[0]);
                DataModel.TomapSample tomapSample = null;
                BufferedReader br = null;
                String line;
                StringBuilder json = new StringBuilder();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        json.append(line);
                    }
                    Gson gson = JsonHandler.getInstance()
                            .getGsonBuilder()
                            .create();
                    Type collectionType = new TypeToken<Collection<DataModel.TomapSample>>(){}.getType();
                    Collection<DataModel.TomapSample> enums = gson.fromJson(json.toString(), collectionType);
                    Iterator<DataModel.TomapSample> it = enums.iterator();
                    if(it.hasNext()){
                        tomapSample = it.next();
                        if(tomapSample.getId_utente_rilevatore() != null)
                            sharedPreferences.edit().putInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_UTENTE,tomapSample.getId_utente_rilevatore()).apply();
                        if(tomapSample.getCognome_rilevatore() != null)
                            sharedPreferences.edit().putString(Constants.TOMAP.PREFERENCES.TOMAP_GOGNOME,tomapSample.getCognome_rilevatore()).apply();
                        if(tomapSample.getId_gruppo_rilevatori() != null)
                            sharedPreferences.edit().putInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_GRUPPO,tomapSample.getId_gruppo_rilevatori()).apply();
                        if(tomapSample.getId_squadra() != null)
                            sharedPreferences.edit().putInt(Constants.TOMAP.PREFERENCES.TOMAP_ID_SQUADRA,tomapSample.getId_squadra()).apply();
                    }
                    else{
                        // TODO: 16/03/2017 lista utenti vuota ma login passato, come gestirlo?
                    }
                    return RESULT_OK;
                }
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
            return RESULT_FAILED;
        }

        protected void onPostExecute(Integer resultCode) {
            switch (resultCode) {
                case  RESULT_OK:
                    // Dismiss the ProgressDialog.
                    progressDialog.dismiss();
                    // Do something here if login succeeded.
                    onLoginSuccess();
                    break;
                case RESULT_FAILED:
                    // Dismiss the ProgressDialog.
                    progressDialog.dismiss();
                    // Do something here if login failed.
                    onLoginFailed();
                    break;
                default:
                    // Do nothing..
            }
        }
    }
}
