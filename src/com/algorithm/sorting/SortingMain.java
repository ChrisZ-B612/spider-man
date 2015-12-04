package com.algorithm.sorting;

import java.util.Arrays;

/**
 * @author biao.zhang@shuyun.com
 */
public class SortingMain {

    private static Integer[] arr = new Integer[]{3, 5, 8, 1, 1, 2};

    /**
     * @param args
     */
    public static void main(String[] args) {
        ;
        System.out.println(Arrays.toString(sortByBubble(arr.clone(), true)));
        System.out.println(Arrays.toString(sortByBubble(arr.clone(), false)));
    }

    private static Integer[] sortByBubble(Integer[] arr, boolean ascending) {// 冒泡排序
        for (int i = arr.length - 1; i >= 0; i--) {
            boolean alreadyOrdered = true;
            for (int j = 0; j < i; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0 == ascending) {
                    alreadyOrdered = false;
                    swap(arr, j, j + 1);
                }
            }
            System.out.println(Arrays.toString(arr));
            if (alreadyOrdered) break;
        }
        return arr;
    }

    private static void sortByInsert(boolean ascending) {// 插入排序
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i], j;
            for (j = i - 1; j >= 0; j--) {
                int res = Integer.compare(arr[j], tmp);
                if (res == 0 || res < 0 == ascending) break;
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = tmp;
        }
    }

    private static void sortBySelect(boolean ascending) {// 选择排序
        Integer selected;
        for (int i = 0; i < arr.length - 1; i++) {
            selected = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j].compareTo(arr[selected]) < 0 == ascending) {
                    selected = j;
                }
            }
            swap(arr, i, selected);
        }
    }

    private static void swap(Integer[] arr, int i, int j) {
        if (i == j) return;
        arr[i] = arr[i] + arr[j];
        arr[j] = arr[i] - arr[j];
        arr[i] = arr[i] - arr[j];
    }

}