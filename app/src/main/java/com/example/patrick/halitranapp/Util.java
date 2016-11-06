package com.example.patrick.halitranapp;

/**
 * Created by Patrick on 11/10/2016.
 */

public class Util {

    public static final String ServerAdress = "http://vps197081.ovh.net:8080/HALIFA_TRAN/";

    public static final String INSCRIPTION = "CreateUser";
    public static final String LOGIN = "Login";
    public static final String DISPLAY_TWEET = "DisplayTweet";
    public static final String REMOVE_TWEET = "Tweet/Remove";
    public static final String ADD_TWEET = "Tweet/Add";
    public static final String CHANGE_MAIL = "User/ChangeMail";
    public static final String CHANGE_PASSWORD = "User/ChangePassword";
    public static final String DISPLAY_PROFIL_INFO = "User/Profile/DisplayProfileInfo";
    public static final String DISPLAY_PROFIL_TWEETS = "User/Profile/DisplayProfileTweets";
    public static final String SEARCH = "Search";

    public static String addFirstParameter(String url, String parameter, String value) {
        return url+"?"+parameter+"="+value;
    }

    public static  String addParameter(String url, String parameter, String value) {
        return url+"&"+parameter+"="+value;
    }

}
