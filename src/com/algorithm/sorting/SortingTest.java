package com.algorithm.sorting;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import junit.framework.Assert;

/**
 * @author biao.zhang@hp.com
 * @date Aug 23, 2012 3:29:43 PM
 */
public class SortingTest {

	@Test
	public void selection() {
		sort(SortingType.SELECTION);
	}
	
//	@Test
	public void insertion() {
		sort(SortingType.INSERTION);
	}
	
//	@Test
	public void bubble() {
		sort(SortingType.BUBBLE);
	}
	
//	@Test
	public void shell() {
		sort(SortingType.SHELL);
	}
	
//	@Test
	public void merge() {
		sort(SortingType.MERGE);
	}
	
//	@Test
	public void quick() {
		sort(SortingType.QUICK);
	}
	
//	@Test
	public void heap2() {
		sort(SortingType.HEAP2);
	}
	
//	@Test
	public void heap3() {
		sort(SortingType.HEAP3);
	}
	
//	@Test
	public void heap4() {
		sort(SortingType.HEAP4);
	}
	
//	@Test
	public void heap5() {
		sort(SortingType.HEAP5);
	}
	
//	@Test
	public void heap6() {
		sort(SortingType.HEAP6);
	}
	
//	@Test
	public void heap7() {
		sort(SortingType.HEAP7);
	}
	
//	@Test
	public void heap8() {
		sort(SortingType.HEAP8);
	}
	
//	@Test
	public void heap9() {
		sort(SortingType.HEAP9);
	}
	
	private static void sort(SortingType type) {
//		Integer[] array = generateArray(5);
		Integer[] array = new Integer[]{2, 8, 1, 5, 3, 1};
		boolean ascend = true;
		System.out.println(type.name() + " - Before(" + isOrdered(array, ascend) + ") : " + Arrays.toString(array));
		type.sort(array, ascend);
		System.out.println(type.name() + " - After(" + isOrdered(array, ascend) + ") : " + Arrays.toString(array));
		Assert.assertTrue(isOrdered(array, ascend));
	}
	
	private static Integer[] generateArray(int length) {
		Integer[] array = new Integer[length];
		Random random = new Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt(array.length * 10);
		}
		return array;
	}
	
	private static boolean isOrdered(Comparable[] array, boolean ascend) {
		for (int i = 0; i < array.length - 1; i++) {
			int cs = array[i].compareTo(array[i + 1]);
			if (cs != 0 && cs < 0 != ascend) return false;
		}
		return true;
	}
	
}
