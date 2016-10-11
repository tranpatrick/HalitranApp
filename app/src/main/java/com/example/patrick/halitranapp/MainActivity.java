package com.example.patrick.halitranapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class MainActivity extends AppCompatActivity {

    HalitranApplication mApp;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApp = (HalitranApplication) getApplication();
        textView = (TextView) findViewById(R.id.textView);

        if (mApp.getKey()!= null) {
            textView.setText("");
            textView.append(mApp.getId()+"\n");
            textView.append(mApp.getKey()+"\n");
            textView.append(mApp.getLogin()+"\n");
        }
    }

    public void openLoginActivity(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void test(){
        RequestQueue rq = newRequestQueue(this);
    }
}
