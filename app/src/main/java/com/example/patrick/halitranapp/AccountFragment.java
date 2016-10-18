package com.example.patrick.halitranapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Patrick on 18/10/2016.
 */

public class AccountFragment extends Fragment {

    EditText username;
    EditText mailAddress;
    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button mailSaveButton;
    Button pwdSaveButton;

    HalitranApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        application = (HalitranApplication) getActivity().getApplication();

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        username = (EditText) view.findViewById(R.id.account_username);
        mailAddress = (EditText) view.findViewById(R.id.account_mailaddress);
        currentPassword = (EditText) view.findViewById(R.id.account_currentpassword);
        newPassword = (EditText) view.findViewById(R.id.account_newpassword);
        confirmPassword = (EditText) view.findViewById(R.id.account_confirmpassword);

        mailSaveButton = (Button) view.findViewById(R.id.mailsavechanges);
        pwdSaveButton = (Button) view.findViewById(R.id.pwdsavechanges);

        username.setEnabled(false);
        username.setText(application.getLogin());

        /* listeners */
        


        // Inflate the layout for this fragment
        return view;
    }

}
