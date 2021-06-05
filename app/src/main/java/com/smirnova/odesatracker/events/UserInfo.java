package com.smirnova.odesatracker.events;

public class UserInfo {
    private static String name;
    private static String mail;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserInfo.name = name;
    }

    public static String getMail() {
        return mail;
    }

    public static void setMail(String mail) {
        UserInfo.mail = mail;
    }
}
