package com.example.patrick.halitranapp;

/**
 * Created by Patrick on 11/10/2016.
 */

public class Util {

    public static String ServerAdress = "http://132.227.201.129:8280/HALIFA_TRAN2/";

    public static String INSCRIPTION = "CreateUser";
    public static final String LOGIN = "Login";
    public static final String DISPLAY_TWEET = "DisplayTweet";
    public static final String REMOVE_TWEET = "Tweet/Remove";

    public static String addFirstParameter(String url, String parameter, String value) {
        return url+"?"+parameter+"="+value;
    }

    public static  String addParameter(String url, String parameter, String value) {
        return url+"&"+parameter+"="+value;
    }

}
