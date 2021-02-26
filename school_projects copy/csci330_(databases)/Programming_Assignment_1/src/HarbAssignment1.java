import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class HarbAssignment1 {
    public static void main(String[] args) {
        //initialize input from txt file
        String filename = "StockmarketInput.txt";
        ArrayList<String[]> list = getList(filename);

        //initialize first company
        int count = 0;
        String currCompany = list.get(0)[0];
        ArrayList<String[]> crazyDays = new ArrayList<>();
        ArrayList<String[]> split = new ArrayList<>();
        System.out.printf("Processing %s\n ========================================\n", currCompany);

        for (int i = 0; i < list.size(); i++) {
            //check for "crazy day"
            double high = Double.parseDouble(list.get(i)[3]);
            double low = Double.parseDouble(list.get(i)[4]);
            double currFlux = (high - low) / high;

            //add crazy day to arraylist if it exists
            if (currFlux >= 0.15) {
                crazyDays.add(list.get(i));
                count++;
            }
            //add split day to split list if it exists
            if (i < list.size()-1) {
                double close = Double.parseDouble(list.get(i+1)[5]);
                double open = Double.parseDouble(list.get(i)[2]);

                if (Math.abs(close/open - 2) < 0.2) {
                    split.add(new String[]{"2:1", list.get(i+1)[1], String.valueOf(open), String.valueOf(close)});
                } else if (Math.abs((close/open) - 3) < 0.3) {
                    split.add(new String[]{"3:1", list.get(i+1)[1], String.valueOf(open), String.valueOf(close)});
                } else if (Math.abs((close/open) - 1.5) < 0.15) {
                    split.add(new String[]{"3:2", list.get(i+1)[1], String.valueOf(open), String.valueOf(close)});
                }
            }

            //if company changes, print results and initialize next company
            if(i < list.size()-1 && !list.get(i)[0].equals(list.get(i+1)[0])) {
                printCraziest(crazyDays);
                printSplit(split);
                split.clear();
                crazyDays.clear();
                currCompany = list.get(i+1)[0];
                System.out.printf("\nProcessing %s\n ========================================\n", currCompany);
            }

        }
        //print results for last company
        printCraziest(crazyDays);
        printSplit(split);
    }

    public static ArrayList<String[]> getList(String filename) {
        ArrayList<String[]> list =  new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] s = sc.nextLine().split("\\t");
                list.add(s);
            }
        } catch (Exception  e) {
            System.out.println("Error, unable to parse file. Exception " + e);
        }
        return list;
    }

    public static void printCraziest(ArrayList<String[]> crazyDays) {
        Double craziestFlux = 0.0;
        String craziestDay = "0/0/0";
        for (String[] s: crazyDays) {
            double hx = Double.parseDouble(s[3]);
            double lx = Double.parseDouble(s[4]);
            double flux = 100*(hx - lx) / hx;
            System.out.printf("Crazy day: %s \t %.2f\n", s[1], flux);
            if (flux > craziestFlux) {
                craziestFlux = flux;
                craziestDay = s[1];
            }
        }
        System.out.printf("Total crazy days = %d\n", crazyDays.size());
        if (crazyDays.size() > 0) {
            System.out.printf("The craziest day: %s\t %.2f\n\n", craziestDay, craziestFlux);
        }
        crazyDays.clear();
    }

    public static void printSplit(ArrayList<String[]> split) {
        for (String[] s : split) {
            System.out.printf("%s split on %s \t %s -->\t %s\n", s[0], s[1], s[3], s[2]);
        }
        System.out.println("Total number of splits: " + split.size());
    }
}
