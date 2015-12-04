package com.algorithm.sorting;

public interface Sortable {

	/**
	 * Sorts the given array which consists of {@link Comparable}<br/>
	 * 
	 * @param array
	 * @param ascend
	 */
	<T extends Comparable<T>> void sort(T[] array, boolean ascend);
}
