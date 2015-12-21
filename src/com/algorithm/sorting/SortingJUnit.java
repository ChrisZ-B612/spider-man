package com.algorithm.sorting;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * @author Chris, Z
 * @date Aug 23, 2012 3:29:43 PM
 */
public class SortingJUnit {

    private static Integer[] srcArr;
    private static boolean ascending = true;
    private static Map<String, Long> map = new HashMap<>();

    @BeforeClass
    public static void beforeStart() {
        int size = 1000000;
		System.out.printf("Size  : %d\n", size);
        srcArr = generateArray(size);
//		srcArr = new Integer[] {3, 1, 5, 8, 1, 2};
        map = new HashMap<>();
    }

    @AfterClass
    public static void afterFinish() {
        List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(map.entrySet());
        Collections.sort(list, Comparator.comparingLong(Map.Entry::getValue));
        Iterator<Map.Entry<String, Long>> it = list.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> next = it.next();
            System.out.printf("%-6s: [%-3sms]\n", next.getKey(), next.getValue());
        }
    }

    private void sort(SortingType type) {
        Integer[] myArr = srcArr.clone();
        Assert.assertFalse(isOrdered(myArr, ascending));
        long start = Calendar.getInstance().getTimeInMillis();
        type.sort(myArr, ascending);
        map.put(type.name(), Calendar.getInstance().getTimeInMillis() - start);
        Assert.assertTrue(isOrdered(myArr, ascending));
    }

    private static Integer[] generateArray(int length) {
        Integer[] arr = new Integer[length];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(arr.length * 10);
        }
        return arr;
    }

    private static boolean isOrdered(Comparable[] arr, boolean ascending) {
        boolean isOrdered = true;
        for (int i = 1; i < arr.length; i++) {
            int res = arr[i - 1].compareTo(arr[i]);
            if (res != 0 && res > 0 == ascending) {
                isOrdered = false;
                break;
            }
        }
        return isOrdered;
    }

//	@Test
    public void bubble() {
        sort(SortingType.BUBBLE);
    }

//	@Test
    public void select() {
        sort(SortingType.SELECT);
    }

//	@Test
    public void insert() {
        sort(SortingType.INSERT);
    }

//	@Test
    public void shell() {
        sort(SortingType.SHELL);
    }

//	@Test
    public void merge() {
        sort(SortingType.MERGE);
    }

	@Test
    public void quick() {
        sort(SortingType.QUICK);
    }

//  @Test
    public void heap() {
        sort(SortingType.HEAP);
    }

}