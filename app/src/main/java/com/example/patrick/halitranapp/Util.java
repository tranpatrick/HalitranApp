package com.example.patrick.halitranapp;

/**
 * Created by Patrick on 11/10/2016.
 */

public class Util {

    public static String ServerAdress = "http://vps197081.ovh.net:8080/HALIFA_TRAN/";

    public static String INSCRIPTION = "CreateUser";
    public static final String LOGIN = "Login";

    public static String addFirstParameter(String url, String parameter, String value) {
        return url+"?"+parameter+"="+value;
    }

    public static  String addParameter(String url, String parameter, String value) {
        return url+"&"+parameter+"="+value;
    }

}
