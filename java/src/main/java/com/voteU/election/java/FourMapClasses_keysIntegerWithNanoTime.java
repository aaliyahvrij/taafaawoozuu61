package com.voteU.election.java;

import java.util.*;

public class FourMapClasses_keysIntegerWithNanoTime {
    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        Map<Integer, Integer> weakHashMap = new WeakHashMap<>();
        showResultsOf(hashMap);
        showResultsOf(linkedHashMap);
        showResultsOf(treeMap);
        showResultsOf(weakHashMap);
    }

    private static HashMap<Integer, Integer> putDataIn(Map<Integer, Integer> theMap, int index) {
        long start;// = System.nanoTime();
        long end;// = System.nanoTime();
        int difference;
        HashMap<Integer, Integer> putStuffHashMap = new HashMap<>();
        start = System.nanoTime();
        Integer[] differentLengthList = {10, 100, 1000, 10000, 100000};
        if (theMap instanceof WeakHashMap<Integer, Integer>) {
            /*WeakHashMap<Integer, Integer> weakHashMap = new WeakHashMap<>();
            Integer[] weakKeyList = new Integer[differentLengthList[index]];
            for (int i = 0; i < weakKeyList.length; i++) {
                weakKeyList[i] = new Integer(keys[i]); // ensure distinct objects
                weakHashMap.put(weakKeyList[i], values[i]);
            }
            System.out.println("WeakHashMap before GC: " + weakHashMap);
            for (int i = 0; i < weakKeyList.length; i++) {
                weakKeyList[i] = null;
            }
            System.gc();
            try {
                Thread.sleep(1000); // Allow GC time
            } catch (InterruptedException e) {
            e.printStackTrace();
            }
            System.out.println("WeakHashMap after GC: " + weakHashMap);*/
        }
        for (int ii = 0; ii < differentLengthList[index]; ii++) {
            theMap.put(ii, ii);
        }
        end = System.nanoTime();
        difference = (int) (end - start);
        putStuffHashMap.put(differentLengthList[index], difference);
        return putStuffHashMap;
    }

    private static void showResultsOf(Map<Integer, Integer> theMap) {
        Map<Integer, Integer> stuff = null;
        Integer key = 0;
        Integer value = 0;
        for (int i = 0; i < 5; i++) {
            stuff = putDataIn(theMap, i);
            for (Map.Entry<Integer, Integer> entry : stuff.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
            }
            System.out.println(key + " - Time to fill " + theMap.getClass().getSimpleName() + ": " + value / 1_000_000.0 + "ms");
        }
        if (!theMap.getClass().getSimpleName().equals("WeakHashMap")) {
            System.out.println("--------------------");
        }
    }
}
