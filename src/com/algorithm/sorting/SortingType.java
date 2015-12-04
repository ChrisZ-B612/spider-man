package com.algorithm.sorting;

/**
 * @author biao.zhang@hp.com
 * @date Aug 23, 2012 3:06:18 PM
 */
public enum SortingType {
	/**
	 * 1、选择排序
	 */
	SELECTION(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			for (int i = 0; i < array.length - 1; i++) {
				int selected = i;
				for (int j = i + 1; j < array.length; j++) {
					int cs = array[j].compareTo(array[selected]);
					if (cs != 0 && cs < 0 == ascend) {
						selected = j;
					}
				}
//				printDetail(array, i, selected, i + 1);
				exchange(array, selected, i);
			}
		}
		
	}),
	
	/**
	 * 2、插入排序
	 */
	INSERTION(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			for (int i = 1; i < array.length; i++) {
				T tmp = array[i];
				int j;
				for (j = i - 1; j >= 0; j--) {
					int cs = array[j].compareTo(tmp);
					if (cs == 0 || cs < 0 == ascend) break;
					array[j + 1] = array[j];
				}
				array[j + 1] = tmp;
			}
		}
		
	}),
	
	/**
	 * 3、冒泡排序
	 */
	BUBBLE(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			int pointer = Integer.MAX_VALUE;//优化一
			int num = 1;
			for (int i = array.length - 1; i > 0; i--) {
				boolean isChanged = false;//优化二
				int max = pointer < i ? pointer : i;
				for (int j = 0; j < max; j++) {
					int cs = array[j + 1].compareTo(array[j]);
					if (cs != 0 && cs < 0 == ascend) {
//						printDetail(array, j, j + 1, num++);
						exchange(array, j + 1, j);
						pointer = j;
						isChanged = true;
					}
				}
				/**
				 * 如果某次遍历发现没有数据进行过交换，那么说明该数组已经有序。
				 */
				if (!isChanged) break;
			}
//			printDetail(array, -1, -1, num++);
		}
		
	}),
	
	/**
	 * 4、希尔排序
	 */
	SHELL(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			int gap = 1;
			while (gap < array.length / 3) {
				gap = gap * 3 + 1;
			}
			while (gap >= 1) {
				for (int i = gap; i < array.length; i++) {
					T tmp = array[i];
					int j = i;
					while (j >= gap) {
						int cs = array[j - gap].compareTo(tmp);
						if (cs == 0 || cs < 0 == ascend ) break;
						array[j] = array[j - gap];
						j -= gap;
					}
					if (j != i) array[j] = tmp;
				}
				gap /= 3;
			}
		}
		
	}),
	
	/**
	 * 5、归并排序
	 */
	MERGE(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			sort(array, ascend, 0, array.length - 1);
		}
		
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend, int low, int high) {
			int length = high - low + 1;
			//个数小于20时使用插入排序
			if (length < 20) {
				for (int i = low + 1; i <= high; i++) {
					T tmp = array[i];
					int j;
					for (j = i - 1; j >= low; j--) {
						int cs = array[j].compareTo(tmp);
						if (cs == 0 || cs < 0 == ascend) break;
						array[j + 1] = array[j];
					}
					array[j + 1] = tmp;
				}
				return;
			}
			int mid = (low + high) / 2;
			sort(array, ascend, low, mid);
			sort(array, ascend, mid + 1, high);
			merge(array, ascend, low, mid, high);
		}
		
		private <T extends Comparable<T>> void merge(T[] array, boolean ascend, int low, int mid, int high) {
			
			int leftEndCompareToRightStart = array[mid].compareTo(array[mid + 1]);
			if (leftEndCompareToRightStart == 0 || leftEndCompareToRightStart < 0 == ascend) return;
			
			int length = high - low + 1;
			@SuppressWarnings("unchecked")
			T[] copy = (T[])new Comparable[length];
			System.arraycopy(array, low, copy, 0, length);
			
			int lowIndex = 0;
			int highIndex = mid - low + 1;
			for (int i = low; i <= high; i++) {
				if (lowIndex > mid - low) {
					array[i] = copy[highIndex++];
				} else if (highIndex > high - low) {
					array[i] = copy[lowIndex++];
				} else if (copy[lowIndex].compareTo(copy[highIndex]) < 0 == ascend) {
					array[i] = copy[lowIndex++];
				} else {
					array[i] = copy[highIndex++];
				}
			}
			
		}
	}),
	
	/**
	 * 6、快速排序
	 */
	QUICK(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			sort(array, ascend, 0, array.length - 1);
		}
		
		private <T extends Comparable<T>> void sort(T[] array, boolean ascend, int low, int high) {
			if (low >= high) return;
			int left = low + 1;
			int right = high;
			int hole = low;
			boolean flag = false;
			T tmp = array[low];
			while (left <= right) {
				if (flag) {
					while (left <= right) {
						int cs = array[left].compareTo(tmp);
						if (cs != 0 && cs > 0 == ascend) {
							array[hole] = array[left];
							hole = left++;
							break;
						} else left++;
					}
				} else {
					while (left <= right) {
						int cs = array[right].compareTo(tmp);
						if (cs != 0 && cs < 0 == ascend) {
							array[hole] = array[right];
							hole = right--;
							break;
						} else right--;
					}
				}
				flag = !flag;
			}
			array[hole] = tmp;
			sort(array, ascend, low, hole - 1);
			sort(array, ascend, hole + 1, high);
		}
		
	}),
	
	/**
	 * 7、堆排序
	 */
	HEAP2(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 2);
		}
		
	}),
	
	HEAP3(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 3);
		}
		
	}),
	
	HEAP4(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 4);
		}
		
	}),
	
	HEAP5(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 5);
		}
		
	}),
	
	HEAP6(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 6);
		}
		
	}),
	
	HEAP7(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 7);
		}
		
	}),
	
	HEAP8(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 8);
		}
		
	}),
	
	HEAP9(new Sortable() {
		
		@Override
		public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
			heapSort(array, ascend, 9);
		}
		
	});
	
	private SortingType(Sortable s) {
		this.s = s;
	}
	
	private Sortable s;
	
	public <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
		s.sort(array, ascend);
	}
	
	private static <T extends Comparable<T>> void exchange(T[] array,int i,int j) {
		if (i == j) return;
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	/**
	 * N叉堆的入口方法
	 * @param array
	 * @param ascend
	 */
	private static <T extends Comparable<T>> void heapSort(T[] array, boolean ascend, int size) {
		build(array, ascend, size);//构建二叉堆
//		System.out.println(Arrays.toString(array));
		for (int i = array.length - 1; i > 0; i--) {
			exchange(array, 0, i);
			sink(array, i, 0, ascend, size);//重构二叉堆
		}
	}
	
	private static <T extends Comparable<T>> void build(T[] array, boolean ascend, int size) {
		for (int i = (array.length - 2) / size; i >= 0; i--) {
			sink(array, array.length, i, ascend, size);
		}
	}
	
	/**
	 * 对array[start]节点执行二叉堆重构
	 * @param array
	 * @param length
	 * @param start
	 * @param ascend
	 */
	private static <T extends Comparable<T>> void sink(T[] array, int length, int start, boolean ascend, int size) {
		T tmp = array[start];
		int hole = start;
		int index = start * size + 1;
		while (index < length) {
			int rightChild = index + size - 1;
			for (int i = index + 1; i < length && i <= rightChild; i++) {
				int cs = array[i].compareTo(array[index]);
				if (cs > 0 == ascend) index = i;
			}
//			if (index + 1 < length) {
//				int cs = array[index + 1].compareTo(array[index]);
//				if (cs > 0 == ascend) index++;
//			}
			int cs = array[index].compareTo(tmp);
			if (cs > 0 == ascend) {
				array[hole] = array[index];
				hole = index;
				index = index * size + 1;
			} else break;
		}
		array[hole] = tmp;
	}
	
	/**
	 * 打印数组，用于测试
	 * @param array
	 * @param src
	 * @param dest
	 */
	public static <T extends Comparable<T>> void printDetail(T[] array, int src, int dest, int num) {
		System.out.print(num + " - [");
		for (int k = 0; k < array.length; k++) {
			if (k > 0) System.out.print(",");
			if (k == src) {
				System.out.printf("%4s", "*" + array[src]);
			} else if (k == dest) {
				System.out.printf("%4s", "@" + array[k]);
			} else {
				System.out.printf("%4s", array[k]);
			}
		}
		System.out.println("]");
	}
	
	/**
	 * 打印数组，用于测试
	 * @param array
	 * @param src
	 * @param dest
	 */
	public static <T extends Comparable<T>> void printDetail(T[] array) {
		System.out.print("R - [");
		for (int k = 0; k < array.length; k++) {
			if (k > 0) System.out.print(",");
			System.out.printf("%4s", array[k]);
		}
		System.out.println("]");
	}
}
