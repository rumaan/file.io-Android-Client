package com.thecoolguy.rumaan.fileio.data;

import java.util.ArrayList;

/**
 * Created by rumaankhalander on 18/01/18.
 */

public class DataSingleton {
    private static DataSingleton sINSTANCE;

    private ArrayList<String> strings = new ArrayList<>();



    private DataSingleton() {

    }

    public static DataSingleton getInstance() {
            if (sINSTANCE == null) {
                sINSTANCE = new DataSingleton();
            }
            return sINSTANCE;

    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }
}
