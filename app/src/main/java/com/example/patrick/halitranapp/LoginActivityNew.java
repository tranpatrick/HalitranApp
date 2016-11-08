package com.example.patrick.halitranapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Patrick on 06/11/2016.
 */

public class LoginActivityNew extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.loginEditText) EditText _loginText;
    @InjectView(R.id.passwordEditText) EditText _passwordText;
    @InjectView(R.id.loginButton) Button _loginButton;
    @InjectView(R.id.createUserTextView) TextView _signupLink;
    @InjectView(R.id.forgotPasswordTextView) TextView _forgotLink;
    HalitranApplication mApp;
    boolean isLoginValid;
    boolean isPasswordValid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.inject(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mApp = (HalitranApplication) getApplication();
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _loginButton.setAlpha(.5f);

        mApp.clearUsersId();

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivityNew.class);
                startActivity(intent);
            }
        });

        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgotMdpActivity.class);
                startActivity(intent);
            }
        });


        _loginText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isLoginValid = _loginText.getText().length() > 0;
                refreshButtonsStates();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        _passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPasswordValid = _passwordText.getText().length() >= 6;
                refreshButtonsStates();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    protected void refreshButtonsStates() {
        if(isLoginValid && isPasswordValid) {
            _loginButton.setAlpha(1);
            _loginButton.setEnabled(true);
        }else {
            _loginButton.setEnabled(false);
            _loginButton.setAlpha(.5f);
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        /*if (!validate()) {
            return;
        }*/

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivityNew.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // onLoginSuccess();
                        // onLoginFailed();
                        connexionDuplicata();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    public void connexionDuplicata() {
        final String login;
        final String password;
        if(isUserLogged()) {
            login = mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Login", "");
            password = mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Password", "");
        }else{
            login = _loginText.getText().toString();
            password = _passwordText.getText().toString();
        }
        String loginRequestUrl = Util.ServerAdress + Util.LOGIN;
        loginRequestUrl = Util.addFirstParameter(loginRequestUrl, "login", login);
        loginRequestUrl = Util.addParameter(loginRequestUrl, "password", password);
        Log.i("URL", loginRequestUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("REPONSE", response.toString());
                        try {
                            if (!response.has("erreur")) {
                                mApp.setId(response.getInt("id"));
                                mApp.setKey(response.getString("key"));
                                mApp.setLogin(response.getString("login"));
                                Intent intent = new Intent(mApp, MainActivity.class);
                                /* Enregistrer les id utilisateurs dans les preferences */
                                mApp.saveUsersId(login, password);
                                startActivity(intent);
                                finish();
                            } else {
                                _loginButton.setEnabled(true);
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.i("LoginActivity", "onResponse - " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LoginActivity", "onErrorResponse - " + error.getMessage().toString());
                        _loginButton.setEnabled(true);
                        Toast.makeText(mApp, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(jsonObjectRequest);
    }

    public void connexion(View view) {
        connexionDuplicata();
    }

    /* méthode pour vérifier si un uilisateur est loggé */
    private boolean isUserLogged() {
        Log.i("isUserLogged", "je passe dans isUserLogged");
        if (getSharedPreferences("user", MODE_PRIVATE).contains("Login")
                && getSharedPreferences("user", MODE_PRIVATE).contains("Password")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        /* start main activity */
        Intent intent = new Intent(mApp, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    /*public boolean validate() {
        boolean valid = true;
        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();

        if (login.isEmpty()) {
            _loginText.setError("invalid username");
            valid = false;
        } else {
            _loginText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            _passwordText.setError("must contain more than 6 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
   }*/

}
