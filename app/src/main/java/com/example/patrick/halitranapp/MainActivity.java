package com.example.patrick.halitranapp;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private HalitranApplication mApp;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
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
        getWindow().setExitTransition(new Fade());
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent,
                ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
    }

    public void openGetMessagesActivity(View view) {
        getWindow().setExitTransition(new Fade());
        Intent intent = new Intent(this,GetMessageActivity.class);
        startActivity(intent,
                ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
    }

}
