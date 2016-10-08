package com.apps.jpablo.virtualguidemanager.Classes;

/**
 * Created by JuanPablo on 23/03/2016.
 */
public enum File_type {
    IMAGE(1), VIDEO(2),AUDIO(3), PDF(4),UNKNOWN(0);

    private final int value;

    File_type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

