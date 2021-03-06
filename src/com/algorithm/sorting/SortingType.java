package com.algorithm.sorting;

import java.util.*;

/**
 * 排序速度排行榜：
 * 1、快速排序
 * 2、Java排序
 * 3、归并排序
 * 4、堆排序
 * 5、希尔排序
 * 6、选择排序
 * 7、插入排序
 * 8、冒泡排序
 * @author Chris, Z
 * @date Aug 23, 2012 3:06:18 PM
 */
public enum SortingType {

    /**
     * 1、冒泡排序
     */
    BUBBLE(new ISorting() {

        @Override
        public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
            for (int i = arr.length - 1, ceil = -1/* 优化一 */; i > 0; i--) {
                boolean isOrdered = true;// 优化二
                for (int j = 0; j < i; j++) {
                    if (arr[j + 1].compareTo(arr[j]) < 0 == ascending) {
                        swap(arr, j, ceil = j + 1);
                        isOrdered = false;
                    }
                }
                if (isOrdered) break;// 优化二：如果某次遍历发现没有进行过交换操作，那么说明该数组已经有序。
                i = ceil;// 优化一：该数组从ceil往后已经有序，所以只需要对ceil之前的部分进行排序即可
            }
        }

    }),

    /**
     * 2、选择排序
     */
    SELECT(new ISorting() {

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
    INSERT(new ISorting() {

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
    SHELL(new ISorting() {

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
                    for (j = i - gap; j >= 0; j -= gap) {
                        int res = arr[j].compareTo(tmp);
                        if (res == 0 || res < 0 == ascending) break;
                        arr[j + gap] = arr[j];
                    }
                    arr[j + gap] = tmp;
                }
                gap /= divider;
            }
        }

    }),

    /**
     * 5、堆排序
     */
    HEAP(new ISorting() {

        @Override
        public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
            sort(arr, 3, ascending);
        }

        private <T extends Comparable<T>> void sort(T[] arr, int N, boolean ascending) {
            buildNTree(arr, N, ascending);// 构建N叉堆
            for (int i = arr.length - 1; i > 0; i--) {
                swap(arr, 0, i);
                sink(arr, 0, i, N, ascending);
            }
        }

        /**
         * 数组元素从0开始依次和N叉堆从上到下从左到右的树节点一一对应，所以构建的N叉堆其实是完全N叉堆
         * @param arr
         * @param ascending
         * @param N
         * @param <T>
         */
        private <T extends Comparable<T>> void buildNTree(T[] arr, int N, boolean ascending) {
            for (int i = (arr.length - 2) / N; i >= 0; i--) {
                sink(arr, i, arr.length, N, ascending);
            }
        }

        /**
         * 以arr[start]节点为根节点开始执行N叉堆重构
         * @param arr
         * @param start
         * @param length
         * @param N
         * @param ascending
         */
        private <T extends Comparable<T>> void sink(T[] arr, int start, int length, int N, boolean ascending) {
            T holeValue = arr[start];
            int holeIndex = start, index = holeIndex * N + 1;
            while (index < length) {
                int rightChild = index + N - 1;
                for (int i = index + 1; i < length && i <= rightChild; i++) {
                    if (arr[i].compareTo(arr[index]) > 0 == ascending) index = i;
                }
                if (arr[index].compareTo(holeValue) > 0 == ascending) {// 挖到新坑洞
                    arr[holeIndex] = arr[index];
                    holeIndex = index;
                    index = index * N + 1;// 继续找新坑洞
                } else break;
            }
            arr[holeIndex] = holeValue;
        }

    }),

    /**
     * 6、归并排序
     */
    MERGE(new ISorting() {

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
            T[] kakashi = (T[]) new Comparable[length];
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
     * 7、Java默认排序
     */
    JAVA(new ISorting() {

        @Override
        public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
            Arrays.sort(arr, ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
        }

    }),

    /**
     * 8、快速排序
     */
    QUICK(new ISorting() {

        @Override
        public <T extends Comparable<T>> void sort(T[] arr, boolean ascending) {
            sort(arr, 0, arr.length, ascending);
        }

        private <T extends Comparable<T>> void sort(T[] arr, int start, int end, boolean ascending) {
            if (start >= end - 1) return;// 跳出递归
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

    });

    SortingType(ISorting s) {
        this.s = s;
    }

    private ISorting s;

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