package com.cyanshift.popularmovies;

import java.util.HashMap;

/**
 * Created by michael on Aug/26/2015.
 */
public class DataHolder {
    private static DataHolder ourInstance = new DataHolder();
    private HashMap<String, Object> valueMap;

    public static DataHolder getInstance() {
        return ourInstance;
    }

    private DataHolder() {
        this.valueMap = new HashMap<String, Object>();
    }

    public Object valueForKey(String key) {
        return this.valueMap.get(key);
    }

    public void setValueForKey(Object obj, String key) {
        this.valueMap.put(key, obj);
    }
}
