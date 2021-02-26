/* Alec Harb
   January 25
   Purpose of this class is to implement 4 different sorting methods
 */

package sort;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.HashMap;

public class Sorts {

   // maintains a count of comparisons performed by this Sorts object
  private int comparisonCount;

  public int getComparisonCount() {
    return comparisonCount;
  }

  public void resetComparisonCount() {
    comparisonCount = 0;
  }

  /** Sorts A[start..end] in place using insertion sort
    * Precondition: 0 <= start <= end <= A.length */
  public void insertionSort(int[] A, int start, int end) {
    int i = start;
    while (i < end) {
      comparisonCount++;
        int j = i;
        while (j > start && A[j] < A[j-1]) {
            comparisonCount++;
            int temp = A[j];
            A[j] = A[j-1];
            A[j-1] = temp;
            j--;
          }
          i++;
    }
  }

  /** Partitions A[start..end] around the pivot A[pivIndex]; returns the
   *  pivot's new index.
   *  Precondition: start <= pivIndex < end
   *  Postcondition: If partition returns i, then
   *  A[start..i] <= A[i] <= A[i+1..end]
   **/
  public int partition(int[] A, int start, int end, int pivIndex) {
    int i = start - 1;
    int j = start;
    int pivValue = A[pivIndex];

    swap(A,pivIndex, end-1);

    while (j < end) {
        comparisonCount++;
        if (A[j] > pivValue) {
          comparisonCount++;
          j++;
        } else {
            i++;
            swap(A,i,j);
            j++;
          }
    }
    return i;
  }

  /** use quicksort to sort the subarray A[start..end] */
  public void quickSort(int[] A, int start, int end) {
    if ((end-start) > 2) {

      comparisonCount++;
      int mid = partition(A, start, end, end-1);

      quickSort(A, start, mid);
      quickSort(A, mid, end);
    }
  }

  /** merge the sorted subarrays A[start..mid] and A[mid..end] into
   *  a single sorted array in A. */
  public void merge(int[] A, int start, int mid, int end) {
    int[] B = new int[A.length];

      for (int i = start; i < end; i++) {
          B[i] = A[i]; //populating array B with values in A
      }

      int i = start;
      int j = mid;
      int k = start;

      while (i < mid && j < end) {
          if (B[i] <= B[j]) {
              comparisonCount++;
              A[k] = B[i];
              i++;
          } else {
              A[k] = B[j];
              j++;
          }
          k++;
      }

      //dumping rest of elements in first half
      while (i < mid) {
          comparisonCount++;
          A[k] = B[i];
          i++;
          k++;
      }

      //dumping rest of elements in second half
      while (j < end) {
          comparisonCount++;
          A[k] = B[j];
          j++;
          k++;
      }
  }

  /** use mergesort to sort the subarray A[start..end] */
  public void mergeSort(int[] A, int start, int end) {
    if ((end-start) < 2) {
            return;
        } else {
            comparisonCount++;
            int mid = (end + start) / 2; //middle index

            mergeSort(A, start, mid); //sort left half
            mergeSort(A, mid, end); //sort right half

            merge(A, start, mid, end); //merge both halves
          }
  }

  /** Sort A using LSD radix sort. Uses counting sort to sort on each digit*/
  public void radixSort(int[] A) {
    int[] B = new int[A.length+1];
    int max = A[0];
    int min = A[0];

    //HashMap where the key is the n and the value is how many times n appears
    HashMap<Integer, Integer> maxDigits = new HashMap<>();
    for (int i = 0; i < A.length; i++) {
      if (maxDigits.containsKey(A[i])) {
        maxDigits.put(A[i], maxDigits.get(A[i]) + 1);
        } else {
            maxDigits.put(A[i], 1);
        }
    }

        //get range for array
    for (int i = 0; i < A.length; i++) {
    if (A[i] > max) {
          max = A[i];
      } else if (A[i] < min) {
          min = A[i];
      }
    }
    int range = max - min + 1;

    //getting offset for array so a negative number will not be out of bounds (i.e. < 0)
    int absMin = Math.abs(min);

    //initialize counting array
    int[] sum = new int[range];

    for (int i = 0; i < range; i++) {
        sum[i] = 0;
    }

    for (int i : maxDigits.keySet()) {
        if (min < 0) {
            sum[i + absMin] = maxDigits.get(i);
        } else {
            sum[i-absMin] = maxDigits.get(i);
        }
    }

    for (int i = 1; i < range; i++) {
        sum[i] = sum[i] + sum[i-1];
    }

    if (min > 0) { // for positive numbers only (subtracting absMin absMIn)
      for (int i = A.length - 1; i >= 0; i--) {
            B[sum[A[i] - absMin]] = A[i];
            sum[A[i] - absMin] = sum[A[i] - absMin] - 1;
        }

      for (int i = 0; i < B.length - 1; i++) {
            A[i] = B[i + 1];
        }
    } else { //for positive and negative numbers combined (adding absMin)
        for (int i = A.length-1; i >= 0; i--) {
            B[sum[A[i]+absMin]] = A[i];
            sum[A[i]+absMin] = sum[A[i]+absMin] - 1;
        }

        for (int i = 0; i < B.length-1; i++) {
            A[i] = B[i+1];
        }
    }
  }

  /* return the 10^d's place digit of n */
  private int getDigit(int n, int d) {
    return (n / ((int)Math.pow(10, d))) % 10;
  }

  /** swap a[i] and a[j]
   *  pre: 0 < i, j < a.size
   *  post: values in a[i] and a[j] are swapped */
  public void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }

}
