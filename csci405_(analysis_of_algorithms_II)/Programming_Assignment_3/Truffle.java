import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Truffle {
    public static void main(String[] args) {
        String fileName = args[0];
        int[][] path = trufflePath(fileName);
        int height = path.length;
        int width = path[0].length;

        List<int[]> trufflesInPath = new ArrayList<>();
        List<int[][]> allPaths = new ArrayList<>();

        int count = 0;
        while (count < height) {
            int i = 0;
            int j = count;
            int[] truffleCount = new int[height];
            int[][] tempPath = new int[2][width];

            tempPath[0][0] = 0;
            tempPath[1][0] = j;
            truffleCount[0] = path[0][j];
            for(int p = 1; p < height; p++) {
                int[] max = getMax(getLeftChild(path, i, j),
                        getMiddleChild(path, i, j), getRightChild(path, i, j));
                truffleCount[p] = max[0];
                tempPath[0][p] = max[1];
                tempPath[1][p] = max[2];

                i++;
                j = max[2];
            }
            trufflesInPath.add(truffleCount);
            allPaths.add(tempPath);
            count++;
        }

        int[] optimalTruffleCount = trufflesInPath.get(0);
        int[][] optimalPath = allPaths.get(0);

        int sum = 0;

        for (int i = 0; i < trufflesInPath.size(); i++) {
            int currSUm = totalTruffles(trufflesInPath.get(i));
            if (currSUm > sum) {
                sum = currSUm;
                optimalTruffleCount = trufflesInPath.get(i);
                optimalPath = allPaths.get(i);
            }
        }

        for (int i = 0; i < height; i++) {
                System.out.printf("[%d, %d] - %d\n", optimalPath[0][i]+1, optimalPath[1][i]+1, optimalTruffleCount[i]);
        }
        System.out.println(sum + " truffles");
    }

    public static int[] getLeftChild(int[][] x, int r, int c) {
        int nextRow = r + 1;
        int nextColumn = c-1;
        if(r < x.length - 1 && c > 0)  {
            return new int[]{x[nextRow][nextColumn], nextRow, nextColumn};
        } else {
            return (new int[]{-1,-1,-1});
        }
    }

    public static int[] getMiddleChild(int[][] x, int r, int c) {
        int nextRow = r + 1;
        if (r < x.length - 1) {
            return new int[]{x[nextRow][c], nextRow, c};
        } else {
            return (new int[]{-1,-1,-1});
        }
    }

    public static int[] getRightChild(int[][] x, int r, int c) {
        int width = x[0].length;
        int nextRow = r + 1;
        int nextColumn = c + 1;
        if (r < x.length - 1 && c < width - 1) {
            return new int[]{x[nextRow][nextColumn], nextRow, nextColumn};
        } else {
            return (new int[]{-1,-1,-1});
        }
    }

    //index 0: truffleCount, index 1: row, index 2: column
    public static int[] getMax(int[] l, int[] m, int[] r) {

       if (m[0] >= l[0] && m[0] >= r[0]) {
            return m;
        } else if (l[0] >= m[0] && l[0] >= r[0]) {
           return l;
       } else {
            return r;
        }
    }

    public static int totalTruffles(int[] x) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }

    public static int[][] trufflePath(String fileName) {
        List<int[]> currList = new ArrayList<>();
        try {
            //create new file
            File file = new File(fileName);
            Scanner sc = new Scanner(file);

            //initialize arrayList of int arrays

            //parse file, turn each line into an array, add to arraylist
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] currString = s.split("\\t");

                //if next line is empty, break loop
                if (s.equals("")) {
                    break;
                }

                int[] currInt = new int[currString.length];
                for (int i = 0; i < currString.length; i++) {
                    currInt[i] = Integer.parseInt(currString[i]);
                }
                currList.add(currInt);
            }

        } catch(Exception e) {
            System.out.println("Failed to parse file. Error: " + e);
        }

        //construct 2-D int array of the path
        int i = currList.size();
        int j = currList.get(0).length;

        int[][] path =  new int[i][j];

        for (int r = 0; r < i; r++) {
            for (int c = 0; c < j; c++) {
                path[r][c] = currList.get(r)[c];
            }
        }
        return path;
    }
}
