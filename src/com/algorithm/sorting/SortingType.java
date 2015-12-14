package com.algorithm.sorting;

/**
 * @author biao.zhang@hp.com
 * @date Aug 23, 2012 3:06:18 PM
 */
public enum SortingType {

	/**
	 * 1、冒泡排序
	 */
	BUBBLE(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			int lastSwappedIndex = Integer.MAX_VALUE;// 优化一
			for (int i = arr.length - 1; i > 0; i--) {
				boolean alreadyOrdered = true;// 优化二
				int endPoint = lastSwappedIndex < i ? lastSwappedIndex : i;
				for (int j = 0; j < endPoint; j++) {
					if (arr[j + 1].compareTo(arr[j]) < 0 == ascending) {
						swap(arr, j, j + 1);
						lastSwappedIndex = j;
						alreadyOrdered = false;
					}
				}
				if (alreadyOrdered) break;// 如果某次遍历发现没有进行过交换操作，那么说明该数组已经有序可以结束了。
			}
		}

	}),

	/**
	 * 2、选择排序
	 */
	SELECT(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			for (int i = 0; i < arr.length - 1; i++) {
				int selected = i;
				for (int j = i + 1; j < arr.length; j++) {
					if (arr[j].compareTo(arr[selected]) < 0 == ascending) {
						selected = j;
					}
				}
				swap(arr, selected, i);
			}
		}
		
	}),
	
	/**
	 * 3、插入排序
	 */
	INSERT(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			for (int i = 1; i < arr.length; i++) {
				T tmp = arr[i];
				int j;
				for (j = i - 1; j >= 0; j--) {
					int res = arr[j].compareTo(tmp);
					if (res == 0 || res < 0 == ascending) break;
					arr[j + 1] = arr[j];
				}
				arr[j + 1] = tmp;
			}
		}
		
	}),

	/**
	 * 4、希尔排序
	 */
	SHELL(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			int gap = 1, divider = 3;
			while (gap < arr.length / divider) {
				gap = gap * divider + 1;
			}
			while (gap >= 1) {
				for (int i = gap; i < arr.length; i++) {
					T tmp = arr[i];
					int j;
					for (j = i; j >= gap; j -= gap) {
						int res = arr[j - gap].compareTo(tmp);
						if (res == 0 || res < 0 == ascending) break;
						arr[j] = arr[j - gap];
					}
					if (j != i) arr[j] = tmp;
				}
				gap /= divider;
			}
		}
		
	}),
	
	/**
	 * 5、归并排序
	 */
	MERGE(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			sort(arr, 0, arr.length, 50, ascending);
		}
		
		public <T extends Comparable<T>> void sort(T[] arr, int start, int end, int threshold, boolean ascending) {
			int length = end - start;
			if (length <= threshold) {
				for (int i = start + 1; i < end; i++) {
					T tmp = arr[i];
					int j;
					for (j = i; j > start; j--) {
						int res = arr[j - 1].compareTo(tmp);
						if (res == 0 || res < 0 == ascending) break;
						arr[j] = arr[j - 1];
					}
					if (j != i) arr[j] = tmp;
				}
				return;
			}
			int middle = (start + end) / 2;
			sort(arr, start, middle + 1, threshold, ascending);
			sort(arr, middle + 1, end, threshold, ascending);
			merge(arr, start, middle, end, ascending);
		}
		
		private <T extends Comparable<T>> void merge(T[] arr, int start, int middle, int end, boolean ascending) {
			int leftEndCompareToRightStart = arr[middle].compareTo(arr[middle + 1]);
			if (leftEndCompareToRightStart == 0 || leftEndCompareToRightStart < 0 == ascending) return;
			
			int length = end - start;
			T[] kakashi = (T[])new Comparable[length];
			System.arraycopy(arr, start, kakashi, 0, length);

			int leftIndex = 0, splitIndex = middle + 1 - start, rightIndex = splitIndex, index = start;
			while (leftIndex < splitIndex && rightIndex < length) {
				if (kakashi[leftIndex].compareTo(kakashi[rightIndex]) == 0) {
					arr[index++] = kakashi[leftIndex++];
					arr[index++] = kakashi[rightIndex++];
				} else if (kakashi[leftIndex].compareTo(kakashi[rightIndex]) < 0 == ascending) {
					arr[index++] = kakashi[leftIndex++];
				} else if (kakashi[leftIndex].compareTo(kakashi[rightIndex]) > 0 == ascending) {
					arr[index++] = kakashi[rightIndex++];
				}
			}
			if (leftIndex == splitIndex) {
				System.arraycopy(kakashi, rightIndex, arr, index, length - rightIndex);
			} else if (rightIndex == length) {
				System.arraycopy(kakashi, leftIndex, arr, index, splitIndex - leftIndex);
			}
		}
	}),
	
	/**
	 * 6、快速排序
	 */
	QUICK(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			sort(arr, 0, arr.length, ascending);
		}

		private <T extends Comparable<T>> void sort(T[] arr, int start, int end, boolean ascending) {
			if (start >= end - 1) return;
			int holeIndex = start, leftIndex = start + 1, rightIndex = end - 1;
			boolean leftToRight = false;
			T holeValue = arr[holeIndex];
			while (leftIndex <= rightIndex) {
				if (leftToRight) {
					while (leftIndex <= rightIndex && holeValue.compareTo(arr[leftIndex]) < 0 != ascending) leftIndex++;
					if (leftIndex <= rightIndex) {
						arr[holeIndex] = arr[leftIndex];
						holeIndex = leftIndex++;
						leftToRight = false;
					}
				} else {
					while (leftIndex <= rightIndex && arr[rightIndex].compareTo(holeValue) < 0 != ascending) rightIndex--;
					if (leftIndex <= rightIndex) {
						arr[holeIndex] = arr[rightIndex];
						holeIndex = rightIndex--;
						leftToRight = true;
					}
				}
			}
			arr[holeIndex] = holeValue;
			sort(arr, start, holeIndex, ascending);
			sort(arr, holeIndex + 1, end, ascending);
		}
		
	}),
	
	/**
	 * 7、堆排序
	 */
	HEAP2(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 2);
		}
		
	}),
	
	HEAP3(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 3);
		}
		
	}),
	
	HEAP4(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 4);
		}
		
	}),
	
	HEAP5(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 5);
		}
		
	}),
	
	HEAP6(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 6);
		}
		
	}),
	
	HEAP7(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 7);
		}
		
	}),
	
	HEAP8(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 8);
		}
		
	}),
	
	HEAP9(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
			heapSort(arr, ascend, 9);
		}
		
	});
	
	SortingType(Sortable s) {
		this.s = s;
	}
	
	private Sortable s;
	
	public <T extends Comparable<T>> void sort(T[] arr, boolean ascend) {
		s.sort(arr, ascend);
	}
	
	private static <T extends Comparable<T>> void swap(T[] arr, int i, int j) {
		if (i == j) return;
		T tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	/**
	 * N叉堆的入口方法
	 * @param arr
	 * @param ascend
	 */
	private static <T extends Comparable<T>> void heapSort(T[] arr, boolean ascend, int size) {
		build(arr, ascend, size);//构建二叉堆
//		System.out.println(Arrays.toString(srcArr));
		for (int i = arr.length - 1; i > 0; i--) {
			swap(arr, 0, i);
			sink(arr, i, 0, ascend, size);//重构二叉堆
		}
	}
	
	private static <T extends Comparable<T>> void build(T[] arr, boolean ascend, int size) {
		for (int i = (arr.length - 2) / size; i >= 0; i--) {
			sink(arr, arr.length, i, ascend, size);
		}
	}
	
	/**
	 * 对arr[start]节点执行二叉堆重构
	 * @param arr
	 * @param length
	 * @param start
	 * @param ascend
	 */
	private static <T extends Comparable<T>> void sink(T[] arr, int length, int start, boolean ascend, int size) {
		T tmp = arr[start];
		int hole = start;
		int index = start * size + 1;
		while (index < length) {
			int rightChild = index + size - 1;
			for (int i = index + 1; i < length && i <= rightChild; i++) {
				int cs = arr[i].compareTo(arr[index]);
				if (cs > 0 == ascend) index = i;
			}
//			if (index + 1 < length) {
//				int cs = srcArr[index + 1].compareTo(srcArr[index]);
//				if (cs > 0 == ascend) index++;
//			}
			int cs = arr[index].compareTo(tmp);
			if (cs > 0 == ascend) {
				arr[hole] = arr[index];
				hole = index;
				index = index * size + 1;
			} else break;
		}
		arr[hole] = tmp;
	}
	
	/**
	 * 打印数组，用于测试
	 * @param arr
	 * @param src
	 * @param dest
	 */
	public static <T extends Comparable<T>> void printDetail(T[] arr, int src, int dest, int num) {
		System.out.print(num + " - [");
		for (int k = 0; k < arr.length; k++) {
			if (k > 0) System.out.print(",");
			if (k == src) {
				System.out.printf("%4s", "*" + arr[src]);
			} else if (k == dest) {
				System.out.printf("%4s", "@" + arr[k]);
			} else {
				System.out.printf("%4s", arr[k]);
			}
		}
		System.out.println("]");
	}
	
	/**
	 * 打印数组，用于测试
	 * @param arr
	 */
	public static <T extends Comparable<T>> void printDetail(T[] arr) {
		System.out.print("R - [");
		for (int k = 0; k < arr.length; k++) {
			if (k > 0) System.out.print(",");
			System.out.printf("%4s", arr[k]);
		}
		System.out.println("]");
	}
}
