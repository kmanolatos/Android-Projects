package com.manolatostech.apppromoter;

import java.io.Serializable;

/**
 * Created by kmanolatos on 16/1/2018.
 */

public class UserModel implements Serializable {
    long id;
    String name;
    String surname;
    String email;
    String user;
    String pass;
    String version;
    long points;
    public UserModel() {

    }
}
