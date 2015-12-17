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
	HEAP(new Sortable() {

		@Override
		public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
			heapSort(arr, ascending, 3);
		}

        /**
         * N叉堆的入口方法
         * @param arr
         * @param ascending
         * @param N
         */
        private <T extends Comparable<T>> void heapSort(T[] arr, boolean ascending, int N) {
            build(arr, ascending, N);// 构建N叉堆
            for (int i = arr.length - 1; i > 0; i--) {
                swap(arr, 0, i);
                sink(arr, i, 0, ascending, N);// 重构N叉堆
            }
        }

        /**
         * 数组元素从0顺序开始，和N叉堆从上到下从左到右的树节点一一对应，所以N叉堆其实是完全N叉堆
         * @param arr
         * @param ascending
         * @param N
         * @param <T>
         */
        private <T extends Comparable<T>> void build(T[] arr, boolean ascending, int N) {
            for (int i = (arr.length - 2) / N; i >= 0; i--) {
                sink(arr, arr.length, i, ascending, N);
            }
        }

        /**
         * 以arr[start]节点为根节点开始执行N叉堆重构
         * @param arr
         * @param length
         * @param start
         * @param ascending
         * @param N
         */
        private <T extends Comparable<T>> void sink(T[] arr, int length, int start, boolean ascending, int N) {
            T holeVal = arr[start];
            int holeIdx = start;
            int index = start * N + 1;// start的左节点index
            while (index < length) {
                int rightChild = index + N - 1;// start的右节点index
                for (int i = index + 1; i < length && i <= rightChild; i++) {// 选择排序法找出start的叶子节点中的最X值
                    int cs = arr[i].compareTo(arr[index]);
                    if (cs > 0 == ascending) index = i;
                }
                int cs = arr[index].compareTo(holeVal);
                if (cs > 0 == ascending) {
                    arr[holeIdx] = arr[index];
                    holeIdx = index;
                    index = index * N + 1;// 找到新坑洞，继续填坑
                } else break;
            }
            arr[holeIdx] = holeVal;
        }
		
	});

	SortingType(Sortable s) {
		this.s = s;
	}

	private Sortable s;

	public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
		s.sort(arr, ascending);
	}
	
	private static <T extends Comparable<T>> void swap(T[] arr, int i, int j) {
		if (i == j) return;
		T tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}