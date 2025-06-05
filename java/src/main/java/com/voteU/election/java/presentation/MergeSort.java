package com.voteU.election.java.presentation;

import com.voteU.election.java.model.Party;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public static void sort(List<Party> list) {
        long start = System.nanoTime ();
        if (list.size() <= 1) return;

        int mid = list.size() / 2;
        List<Party> left = new ArrayList<>(list.subList(0, mid));
        List<Party> right = new ArrayList<>(list.subList(mid, list.size()));

        sort(left);
        sort(right);

        merge(list, left, right);
        long end = System.nanoTime ();
        System.out.println("MergeSort time: " + (end - start) + " ns");
    }

    private static void merge(List<Party> result, List<Party> left, List<Party> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getVotes() >= right.get(j).getVotes()) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }
}

