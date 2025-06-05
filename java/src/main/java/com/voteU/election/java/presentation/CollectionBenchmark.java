package com.voteU.election.java.presentation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class CollectionBenchmark {
    // 250, 25k 250k
    private static final long ELEMENT_COUNT = 2500000;

    public static void main(String[] args) {
        benchmarkAdd();
        //benchmarkGet();
        //removeFirstElement();
    }

    private static void benchmarkAdd() {
        System.out.println("== Toevoegen ==");

        // ArrayList
        long start = System.currentTimeMillis();
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
        }
        long end = System.currentTimeMillis();
        printDuration("ArrayList add", start, end);

        // LinkedList
        start = System.currentTimeMillis();
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            linkedList.add(i);
        }
        end = System.currentTimeMillis();
        printDuration("LinkedList add", start, end);

        // Stack (extending Vector)
        start = System.currentTimeMillis();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            stack.push(i);
        }
        end = System.currentTimeMillis();
        printDuration("Stack push", start, end);
    }

    private static void benchmarkGet() {
        System.out.println("\n== Toegang (get) ==");

        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
            linkedList.add(i);
            stack.push(i);
        }

        // ArrayList
        long start = System.currentTimeMillis();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.get(i);
        }
        long end = System.currentTimeMillis();
        printDuration("ArrayList get", start, end);

        // LinkedList
        start = System.currentTimeMillis();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            linkedList.get(i);
        }
        end = System.currentTimeMillis();
        printDuration("LinkedList get", start, end);

        // Stack
        start = System.currentTimeMillis();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            stack.get(i);
        }
        end = System.currentTimeMillis();
        printDuration("Stack get", start, end);
    }

    private static void removeFirstElement() {
        System.out.println("\n== Verwijderen ==");

        // LinkedList verwijderen van begin
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            linkedList.add(i);
        }
        long start = System.currentTimeMillis();
        while (!linkedList.isEmpty()) {
            linkedList.removeFirst();
        }
        long end = System.currentTimeMillis();
        printDuration("LinkedList removeFirst", start, end);

        // Stack pop (verwijderen laatste)
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            stack.push(i);
        }
        start = System.currentTimeMillis();
        while (!stack.isEmpty()) {
            stack.pop();
        }
        end = System.currentTimeMillis();
        printDuration("Stack pop", start, end);

        // ArrayList verwijderen van begin
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
        }
        start = System.currentTimeMillis();
        while (!arrayList.isEmpty()) {
            arrayList.remove(0);
        }
        end = System.currentTimeMillis();
        printDuration("ArrayList remove(0)", start, end);
    }

    private static void printDuration(String label, long startMillis, long endMillis) {
        long durationMilliseconds = endMillis - startMillis;
        double durationSeconds = durationMilliseconds / 1000.0;
        System.out.printf("%s: %d ms / %.3f seconds%n", label, durationMilliseconds, durationSeconds);
    }
}
