package com.voteU.election.java.presentation;


import com.voteU.election.java.model.Party;

import java.util.Collections;
import java.util.List;

public class BubbleSort {
    public static void sort(List<Party> list) {
        long start = System.nanoTime();
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (list.get(j).getVotes() < list.get(j + 1).getVotes()) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
        long end = System.nanoTime();
        System.out.println("BubbleSort time: " + (end - start) + "ns");
    }
}

