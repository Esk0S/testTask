package testTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.Collections.swap;

public class MergeSort {
    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> arr, int left, int right, boolean mode) {
        if (right > left) {
            if (right - left == 1) {
                int comparable = arr.get(right).compareTo(arr.get(left));
                if (comparable != 0 && ((comparable < 0) ^ mode)) {
                    swap(arr, right, left);
                }
            } else {
                mergeSort(arr, left, left + (right - left) / 2, mode);
                mergeSort(arr, left + (right - left) / 2 + 1, right, mode);
                merge(arr, left, right, mode);
            }
        }
    }

    private static <T extends Comparable<T>> void merge(ArrayList<T> arr, int begin, int end, boolean mode) {
        int mid = begin + (end - begin) / 2;
        int i = begin;
        int j = mid + 1;
        ArrayList<T> d = new ArrayList<>(); // Deque LinkedList

        while (i <= mid && j <= end) {
            if ((arr.get(i).compareTo(arr.get(j)) <= 0) ^ mode) {
                d.add(arr.get(i));
                i++;
            } else {
                d.add(arr.get(j));
                j++;
            }
        }
        while (i <= mid) {
            d.add(arr.get(i));
            i++;
        }
        while (j <= end) {
            d.add(arr.get(j));
            j++;
        }

        for (int t = 0; t < d.size(); t++) {
            arr.set(begin + t, d.get(t)); //
        }
    }

    public static <T extends Comparable<T>> ArrayList<T> mergeFiles(ArrayList<T> arr1, ArrayList<T> arr2, boolean mode) {
        int i = 0;
        int j = 0;
        ArrayList<T> d = new ArrayList<>(); // Deque LinkedList

        while (i < arr1.size() && j < arr2.size()) {
            if ((arr1.get(i).compareTo(arr2.get(j)) <= 0) ^ mode) {
                d.add(arr1.get(i));
                i++;
            } else {
                d.add(arr2.get(j));
                j++;
            }
        }
        while (i < arr1.size()) {
            d.add(arr1.get(i));
            i++;
        }
        while (j < arr2.size()) {
            d.add(arr2.get(j));
            j++;
        }

        return new ArrayList<>(d);
    }
}