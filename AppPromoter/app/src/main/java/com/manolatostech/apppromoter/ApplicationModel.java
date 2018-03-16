package com.manolatostech.apppromoter;


import java.io.Serializable;

/**
 * Created by kmanolatos on 11/1/2018.
 */

public class ApplicationModel implements Serializable{
    long appId;
    String appName;
    String appLink;
    String email;
    long downloads;
    long userId;
    public ApplicationModel() {

    }
}
