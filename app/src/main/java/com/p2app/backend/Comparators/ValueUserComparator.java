package com.p2app.backend.Comparators;

import java.util.Comparator;
import java.util.HashMap;

public class ValueUserComparator implements Comparator<String> {

    private HashMap<String, Double> map = new HashMap<>();

    public ValueUserComparator(HashMap<String, Double> map) {
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if (map.get(s1) >= map.get(s2)) {
            return -1;
        } else {
            return 1;
        }
    }

}
