package com.algorithm.sorting;

public interface ISorting {

	/**
	 * Sorts the given array which consists of {@link Comparable}<br/>
	 * 
	 * @param arr
	 * @param ascending
	 */
	<T extends Comparable<T>> void sort(T[] arr, boolean ascending);
}
