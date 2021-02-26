/* Alec Harb
   January 25
   Purpose of this class is to implement a user interface to test the 4 different sorting methods in Sorts.java
 */
package sort;

import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class SortsDriver {

    // checking if the user selected option ("a")
    public static boolean ISALL = false;

    public static void main(String[] args) {

      Sorts sort = new Sorts();

      Scanner input = new Scanner(System.in);
      String s, quit;
      int n;
      boolean inRange = false;
      boolean flag = true;


    do {

      System.out.print("Enter sort (i[nsertion], q[uick], m[erge], r[adix], a[ll]): ");
      s = input.next();
      System.out.print("Enter n (size of array to sort): ");
      n = input.nextInt();

      if (n <= 20) {
        inRange = true;
      } else {
        inRange = false;
      }

      int[] A = new int[n];
      for (int i = 0; i < A.length; i++) {
        A[i] = new Random().nextInt((n + 2) + n) - n;
      }

      switch (s) {
        case "i":
          sort.resetComparisonCount();
          int[] sortI = A.clone();
          sort.insertionSort(sortI, 0, sortI.length);
          printArray(A, sortI, inRange, sort.getComparisonCount());
          break;

        case "q":
          sort.resetComparisonCount();
          int[] sortQ = A.clone();
          sort.quickSort(sortQ, 0, sortQ.length);
          printArray(A, sortQ, inRange, sort.getComparisonCount());
          break;

        case "m":
          sort.resetComparisonCount();
          int[] sortM = A.clone();
          sort.mergeSort(sortM, 0, sortM.length);
          printArray(A, sortM, inRange, sort.getComparisonCount());
          break;

        case "r":
          sort.resetComparisonCount();
          int[] sortR = A.clone();
          sort.radixSort(sortR);
          printArray(A, sortR, inRange, sort.getComparisonCount());
          break;

        case "a":

          ISALL = true;

          if (inRange) {
            System.out.println("Unsorted: " + Arrays.toString(A));
          }

          //creating 4 copies of A
          int[] sort1 = A.clone();
          int[] sort2 = A.clone();
          int[] sort3 = A.clone();
          int[] sort4 = A.clone();

          //Using all sorts and printing values
          sort.resetComparisonCount();
          sort.insertionSort(sort1, 0, sort1.length);
          System.out.println("Insertion Sort:");
          printArray(A, sort1, inRange, sort.getComparisonCount());

          sort.resetComparisonCount();
          sort.mergeSort(sort2, 0, sort2.length);
          System.out.println("Merge Sort:");
          printArray(A, sort2, inRange, sort.getComparisonCount());

          sort.resetComparisonCount();
          sort.quickSort(sort3, 0, sort3.length);
          System.out.println("Quick Sort:");
          printArray(A, sort3, inRange, sort.getComparisonCount());

          sort.resetComparisonCount();
          sort.radixSort(sort4);
          System.out.println("Radix Sort:");
          printArray(A, sort4, inRange, sort.getComparisonCount());

          //setting ISALL to false for next iteration
          ISALL = false;
      }

      System.out.println("Would you like to sort another array?\n[Y]es or [N]o?"); //loop indefinitely
      quit = input.next();

      if (quit.equals("N") || quit.equals("n")) {
        flag = false;
      }

    } while (flag == true); //keeps looping until user types "N" or "n"
  }

/* prints sorted and unsorted array
  Preconditon: unsorted and sorted array, a boolean value to test the range, and a compare integer
  print the comparisons.
  Postcondition: prints a sorted array (depending on value of n) and the number of comparisons for that array. */
    private static void printArray(int[] unsortedA, int[] sortedA, boolean inRange, int compare) {
        if (inRange && ISALL) { //if n<=20 and the all option is selected
          if (inRange) {
            System.out.println("Sorted: " + Arrays.toString(sortedA));
            System.out.println("Comparisons: " + compare);
          } else { //if n > 20
            System.out.println("Comparisons: " + compare);
          }
        } else if (inRange) {// only if n <=20 and
          System.out.println("Unsorted: " + Arrays.toString(unsortedA));
          System.out.println("Sorted: " + Arrays.toString(sortedA));
          System.out.println("Comparisons: " + compare);
        } else {
            System.out.println("Comparisons: " + compare);
        }
    }

}
