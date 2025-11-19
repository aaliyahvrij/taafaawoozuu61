package com.voteU.election.java;

import java.util.*;

public class MapInterfaceTest {
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

    private static Integer[] iterationLengthList = {100000, 1000000, 2000000, 4000000};

    private static HashMap<Integer, Integer> putDataIn(Map<Integer, Integer> theMap, int index) {
        long start; // = System.nanoTime();
        long end; // = System.nanoTime();
        int difference;
        HashMap<Integer, Integer> resultHashMap = new HashMap<>();
        start = System.nanoTime();
        if (theMap instanceof WeakHashMap<Integer, Integer>) {
            /*WeakHashMap<Integer, Integer> weakHashMap = new WeakHashMap<>();
            Integer[] weakKeyList = new Integer[iterationLengthList[index]];
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
        for (int i = 0; i < iterationLengthList[index]; i++) {
            theMap.put(i, i);
        }
        end = System.nanoTime();
        difference = (int) (end - start);
        resultHashMap.put(iterationLengthList[index], difference);
        return resultHashMap;
    }

    private static void showResultsOf(Map<Integer, Integer> theMap) {
        Map<Integer, Integer> resultHashMap;
        Integer key = 0;
        Integer value = 0;
        System.out.println(theMap.getClass().getSimpleName());
        for (int i = 0; i < iterationLengthList.length; i++) {
            resultHashMap = putDataIn(theMap, i);
            for (Map.Entry<Integer, Integer> entry : resultHashMap.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
            }
            System.out.println(key + " iterations --> " + value / 1_000_000.0 + "ms");
        }
        System.out.println("\n--------------------\n");
    }
}
