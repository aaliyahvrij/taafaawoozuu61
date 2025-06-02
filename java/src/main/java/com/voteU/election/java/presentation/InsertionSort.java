package com.voteU.election.java.presentation;

import com.voteU.election.java.model.Party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertionSort {

    public static void sort(List<Party> list) {
        long start = System.nanoTime();

        for (int i = 1; i < list.size(); i++) {
            Party key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).getVotes() < key.getVotes()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        long end = System.nanoTime();
        System.out.println("InsertionSort time: " + (end - start) + "ns");
    }
}

