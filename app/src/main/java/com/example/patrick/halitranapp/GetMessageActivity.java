package com.example.patrick.halitranapp;


import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class GetMessageActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    private ListView listView;
    private ArrayAdapter<Message> adapter;
    private HalitranApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_messages);
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    }



}
