package com.apps.jpablo.virtualguidemanager.Classes;

import android.content.res.Resources;

import com.apps.jpablo.virtualguidemanager.R;

/**
 * Created by JuanPablo on 23/03/2016.
 */
public enum User_type {
    USER(1, R.string.Administrator),ADMIN(0, R.string.Other);
    private final int value;
    private final int id_string;

    User_type(int value,int id_string) {
        this.value = value;
        this.id_string=id_string;
    }

    public int getValue() {
        return value;
    }
    public String getName(Resources r){ return r.getString(id_string); }
}
