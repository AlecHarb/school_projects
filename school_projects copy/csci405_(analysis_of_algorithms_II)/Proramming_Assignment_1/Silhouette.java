/*
Alec Harb
Programming Assignment 1
The purpose of this document is to solve the "skyline problem" which takes in a txt file with input in the form
<<x1 y1 z1><x2 y2 z2>...<xn yn zn>>
And outputs a list of left vertices that constructs the silhouette
 */

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Silhouette {
    public static void main(String[] args) {
        //input filename
        String fileName = args[0];

        //construct buildings list
        List<int[]> A = myBuildings(fileName);
        //construct skyline list
        List<int[]> list = mySkyline(A);

        //print result
        System.out.print("<");
        for (int[] k : list) {
            System.out.printf("<%d %d>", k[0], k[1]);
        }
        System.out.print(">");
    }

    //construct skyline
    public static List<int[]> mySkyline(List<int[]> A) {
        List<int[]> silhouetteCoords = new ArrayList<>();
        HashMap<Integer, Integer> silhouette = new HashMap<>();
        //gets total width of silhouette
        int maxWidth = A.get(A.size()-1)[2];

        while (!A.isEmpty()) {
            int[] curr = A.remove(0);
            int l = curr[0];
            int r = curr[2];
            int h = curr[1];

            for (int i = l; i <= r; i++) {
                if(!silhouette.containsKey(i)) {
                    silhouette.put(i, h);
                } else if (silhouette.containsKey(i) && silhouette.get(i) < h) {
                    silhouette.put(i, h);
                }
            }
        }

        for (int i = 0; i <= maxWidth; i++) {
            //if the next step to the right has a smaller height, add coordinate (curr x val, height at x+1)
            if (silhouette.containsKey(i) && silhouette.containsKey(i+1) &&
                    silhouette.get(i) > silhouette.get(i+1)) {
                silhouetteCoords.add(new int[] {i, silhouette.get(i+1)});
            }
            //if the next step to the right has no value, set height to 0 because there is no building
            else if (silhouette.containsKey(i) && !silhouette.containsKey(i+1)) {
                silhouetteCoords.add(new int[] {i, 0});
            }
            //add coordinate to silhouetteCoords
            else if (silhouette.containsKey(i)) {
                silhouetteCoords.add(new int[] {i, silhouette.get(i)});
            }
        }

        int currHeight = 0;
        List<int[]> list = new ArrayList<>();

        //get rid of redundant coordinates and return list of left vertices of the silhouette
        for (int[] k : silhouetteCoords) {
            if (k[1] != currHeight) {
                currHeight = k[1];
                list.add(new int[] {k[0], currHeight});
            }
        }
        return list;
    }

    //read txt file and create arraylist of int arrays with coordinates of buildings
    public static List<int[]> myBuildings(String filename) {
        List<int[]> vectors = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            String coords = sc.nextLine();
            //split vectors between "<" and ">"
            String[] q = coords.split("[<>]");

            //creates twice as many positions in array, so start at 1 and iterate by 2
            for (int i = 1; i < q.length; i+= 2) {
                String[] split = q[i].split(" ");
                int[] vec = new int[3];
                for (int j = 0; j < 3; j++) {
                    vec[j] = Integer.parseInt(split[j]);
                }
                vectors.add(vec);
            }
        } catch (Exception e) {
            System.out.println("Unable to parse file. Error: " + e);
        }
        //return list of int arrays
        return vectors;
    }
}
