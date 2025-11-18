package com.voteU.election.java;

public class Esther_LinearyBinaryTest {
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-20s %-20s\n", "Size", "Linear Avg (ms)", "Binary Avg (ms)");
        for (int size = 10; size <= 1_000_000; size *= 10) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = i * 2;
            }

            long totalLinear = 0;
            long totalBinary = 0;
            int runs = 100;

            for (int i = 0; i < runs; i++) {
                int target = array[(int) (Math.random() * size)];

                // Linear Search timing
                long startL = System.nanoTime();
                linearSearch(array, target);
                totalLinear += System.nanoTime() - startL;

                // Binary Search timing
                long startB = System.nanoTime();
                binarySearch(array, target);
                totalBinary += System.nanoTime() - startB;
            }

            double avgLinearMs = totalLinear / runs / 1_000_000.0;
            double avgBinaryMs = totalBinary / runs / 1_000_000.0;

            System.out.printf("%-10d %-20.5f %-20.5f\n", size, avgLinearMs, avgBinaryMs);
        }
    }
}
