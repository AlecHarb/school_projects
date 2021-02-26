/*  Alec Harb
    CSCI 405
    May 20, 2020

    Given n castles at random distances apart, construct an algorithm that gives the optimal path minimizing the penalty
    of (x - m)^2 where x is the miles traveled per day and m is the optimal miles needed to travel per day.
*/
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.lang.Math;
public class OptimalRoute {
    public static void main(String[] args) {
        //get filename from command line
        String filename = args[0];

        //define arraylist of distances
        List<Integer> d = new ArrayList<>();

        //read input and update distances
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            while (sc.hasNextInt()) {
                d.add(sc.nextInt());
            }

        } catch (Exception e) {
            System.out.println("Unable to parse file. Error " + e);
        }

        //define optimal distance per day
        int m = d.remove(0);

        //create distance array from arraylist (for simplicity)
        //define starting penalties from first index
        //initialize optimal path as 0 for each index
        int[] distances = new int[d.size()];
        int[] penalty = new int[distances.length];
        int[] optimalPath = new int[distances.length];
        for(int i = 0; i < distances.length; i++){
            distances[i] = d.get(i);
            penalty[i] = (int)Math.pow(distances[i] - m, 2);
            optimalPath[i] = 0;
        }

        //check distance index i to every previous node and get the min distance
        for (int i = 0; i < distances.length; i++) {
            //decrement from i to 0, finding minimum penalty for next destination
            for (int j = i; j >= 0; j--) {
                //penalty from our current index i to our next index j (must take into account penalty at j so we can get total penalty)
                int currPenalty = penalty[j] + (int)Math.pow(distances[i] - distances[j] - m, 2);
                if(currPenalty < penalty[i]) {
                    //update penalty at index i
                    penalty[i] = currPenalty;
                    //minimum penalty is at index j so we update optimal path
                    optimalPath[i] = j;
                }
            }
        }

        //print optimal path
        //has some instances of duplicates in the path so this print method is a bit weird
        List<Integer> optimal = new ArrayList<>();
        int i = 1;
        //if not 0 or duplicate path value, print
        while (i < optimalPath.length) {
            if (optimalPath[i] != optimalPath[i-1] && optimalPath[i] != 0) {
                optimal.add(optimalPath[i]);
            }
            i++;
        }
        //add final point in path
        optimal.add(optimalPath.length-1);

        System.out.print("Optimal Path: ");
        for (int p : optimal) {
            System.out.print(distances[p] + " ");
        }
        System.out.println();
        
        //penalty is stored in last index of penalties
        System.out.println("Penalty: " + penalty[penalty.length-1]);
    }
}
