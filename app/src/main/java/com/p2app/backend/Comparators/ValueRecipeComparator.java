package com.p2app.backend.Comparators;

import java.util.Comparator;
import java.util.HashMap;

public class ValueRecipeComparator implements Comparator<Integer> {

    private HashMap<Integer, Double> map = new HashMap<>();

    public ValueRecipeComparator(HashMap<Integer, Double> map) {
        this.map.putAll(map);
    }

    @Override
    public int compare(Integer i1, Integer i2) {
        if (map.get(i1) >= map.get(i2)) {
            return -1;
        } else {
            return 1;
        }
    }

}