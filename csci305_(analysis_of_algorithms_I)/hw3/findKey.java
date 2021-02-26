import java.util.ArrayList;
import java.lang.Math;

public class findKey {

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();

        // add each input (including commas) as their own element of "list"
        for (int i = 0; i < args.length; i++) {
            String[] split = args[i].split(" ");
            for (String s : split) {
                list.add(s);
            }
        }
        //get and remove key from arraylist
        int key = Integer.parseInt(list.remove(0));
        //since matrix is square, size == sqrt(# of values)
        int size = (int)Math.sqrt(list.size());

        int[][] A = new int[size][size];
        int index = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                String[] split = list.get(index).split("");
                if (split[split.length-1].equals(",")) {

                    //If infinity, set the element to infinity
                    if (split[0].equals("I")) {
                        A[i][j] = (int) Double.POSITIVE_INFINITY;
                    }
                    // else create a number not including "," and add to array
                    else {
                        StringBuilder b = new StringBuilder();
                        for (int k = 0; k < split.length-1; k++) {
                            b.append(split[k]);
                            A[i][j] = Integer.parseInt(b.toString());
                        }
                    }
                    //increment index
                    index++;
                }
                else if (list.get(index).equals("I")) {
                    A[i][j] = (int) Double.POSITIVE_INFINITY;
                    index++;
                } else {
                    A[i][j] = Integer.parseInt(list.get(index));
                    index++;
                }
            }
        }
        //print the array
        System.out.println("Array:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (A[i][j] == Integer.MAX_VALUE) {
                    System.out.print("       |\u221E|");
                } else {
                    System.out.printf("       |%d|",A[i][j]);
                }
            }
            System.out.println("\n");
        }
        findKey(A, size-1, key);
    }

    private static void findKey (int[][] A, int n, int key) {
        int i = n;
        int j = 0;
        int searched = 0;
        boolean found = false;

        while (j <= n) {
            //if we try to lower A[i][j] value (by decrementing i) and
            // there is no lower value, key is not in the matrix
            if (i < 0) {
                break;
            }
            //key found
            if (A[i][j] == key) {
                searched++;
                found = true;
                break;
            }
            //decrement i to lower A[i][j] value
            else if (A[i][j] > key) {
                i--;
                searched++;
            }
            //increment j to raise A[i][j] value
            else {
                j++;
                searched++;
            }
        }
        //print if found or not
        if (found) {
            System.out.printf("Key: (%d,%d)\n", i+1, j+1);
        } else {
            System.out.println("Key not found");
        }
        //print elements searched with grammar check
        if (searched == 1) {
          System.out.println("1 element searched");
        } else {
          System.out.println(searched + " elements searched");
        }
    }
  }
