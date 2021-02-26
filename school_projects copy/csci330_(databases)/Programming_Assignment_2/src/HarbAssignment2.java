/*
This is a Java skeleton code to help you out how to start this assignment.
Please keep in mind that this is NOT a compilable/runnable java file.
Please feel free to use this skeleton code.
Please give a closer look at the "To Do" parts of this file. You may get an idea of how to finish this assignment.
*/

import java.util.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class HarbAssignment2 {

    private static class StockData {
        // Create this class which should contain the information  (date, open price, high price, low price, close price) for a particular ticker
        private String date;
        private double open_price;
        private double high_price;
        private double low_price;
        private double close_price;

        public StockData(String date, double open_price, double high_price, double low_price, double close_price) {
            this.date = date;
            this.open_price = open_price;
            this.high_price = high_price;
            this.low_price = low_price;
            this.close_price = close_price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getOpen_price() {
            return open_price;
        }

        public void setOpen_price(double open_price) {
            this.open_price = open_price;
        }

        public double getHigh_price() {
            return high_price;
        }

        public void setHigh_price(double high_price) {
            this.high_price = high_price;
        }

        public double getLow_price() {
            return low_price;
        }

        public void setLow_price(double low_price) {
            this.low_price = low_price;
        }

        public double getClose_price() {
            return close_price;
        }

        public void setClose_price(double close_price) {
            this.close_price = close_price;
        }
    }

    static Connection conn;
    static final String prompt = "Enter ticker symbol [start/end dates]: ";

    public static void main(String[] args) throws Exception {
        String paramsFile = "readerparams.txt";
        if (args.length >= 1) {
            paramsFile = args[0];
        }

        Properties connectprops = new Properties();
        connectprops.load(new FileInputStream(paramsFile));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dburl = connectprops.getProperty("dburl");
            String username = connectprops.getProperty("user");
            conn = DriverManager.getConnection(dburl, connectprops);
            System.out.printf("Database connection %s %s established.%n", dburl, username);

            Scanner in = new Scanner(System.in);
            System.out.print(prompt);
            String input = in.nextLine().trim();

            while (input.length() > 0) {
                String[] params = input.split("\\s+");
                String ticker = params[0];
                String startdate = null, enddate = null;
                if (getName(ticker)) {
                    if (params.length >= 3) {
                        startdate = params[1];
                        enddate = params[2];
                    }
                    Deque<StockData> data = getStockData(ticker, startdate, enddate);
                    System.out.println();
                    System.out.println("Executing investment strategy");
                    doStrategy(ticker, data);
                }

                System.out.println();
                System.out.print(prompt);
                input = in.nextLine().trim();
            }
            System.out.println("Database connection closed.");

            // Close the database connection
        } catch (SQLException ex) {
            System.out.printf("SQLException: %s%nSQLState: %s%nVendorError: %s%n",
                    ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
        }
    }

    static boolean getName(String ticker) throws SQLException {
        // Execute the first query and print the company name of the ticker user provided (e.g., INTC to Intel Corp.)
        // Please don't forget to use a prepared statement

        PreparedStatement pstmt = conn.prepareStatement(
                "select Name " +
                        "from company " +
                        "where Ticker = ?");
        pstmt.setString(1, ticker);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.printf("%5s%n", rs.getString("Name"));
            pstmt.close();
            return true;
        } else {
            System.out.printf("%s not found in database.%n", ticker);
            pstmt.close();
            return false;
        }
    }

    static Deque<StockData> getStockData(String ticker, String start, String end) throws SQLException {
        // Execute the second query which will return stock information of the ticker (descending on the transaction date)
        // Please don't forget to use prepared statement
        Deque<StockData> result = new ArrayDeque<>();
        ResultSet rs = null;
        //if date is not specified
        if (start == null || end == null) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "select * " +
                            "from pricevolume " +
                            "where Ticker = ? " +
                            "order by TransDate DESC");
            pstmt.setString(1, ticker);
            rs = pstmt.executeQuery();
        }
        //if date is in specified range
        else {
            PreparedStatement pstmt = conn.prepareStatement(
                    "select * " +
                            "from pricevolume " +
                            "where Ticker = ? " +
                            "and TransDate >= ? " +
                            "and TransDate <= ? " +
                            "order by TransDate DESC");
            pstmt.setString(1, ticker);
            pstmt.setString(2, start);
            pstmt.setString(3, end);
            rs = pstmt.executeQuery();
        }
        //store data for particular ticker into deque
        while(rs.next()) {
            StockData currData = new StockData(rs.getString("TransDate"), rs.getDouble("OpenPrice"),
                    rs.getDouble("HighPrice"), rs.getDouble("LowPrice"), rs.getDouble("ClosePrice"));
            result.add(currData);
        }

        // Loop through all the dates of that company (descending order)
        // Find split if there is any (2:1, 3:1, 3:2) and adjust the split accordingly
        // Include the adjusted data to result (which is a Deque); You can use addFirst method for that purpose
        ArrayList<StockData> stockList = new ArrayList<>(result);
        ArrayList<String[]> split = new ArrayList<>();
        for (int i = 0; i < result.size()-1; i++) {
            double open = stockList.get(i).getOpen_price();
            double close = stockList.get(i+1).getClose_price();

            if (Math.abs(close/open - 2) < 0.2) {
                split.add(new String[]{"2:1", stockList.get(i+1).getDate(), String.valueOf(open), String.valueOf(close), "2"});
            } else if (Math.abs((close/open) - 3) < 0.3) {
                split.add(new String[]{"3:1", stockList.get(i+1).getDate(), String.valueOf(open), String.valueOf(close), "3"});
            } else if (Math.abs((close/open) - 1.5) < 0.15) {
                split.add(new String[]{"3:2", stockList.get(i+1).getDate(), String.valueOf(open), String.valueOf(close), "1.5"});
            }
        }
        printSplit(split, stockList.size());

        //initialize divisor
        double divisor = 1.0;

        for (int i = 0; i < stockList.size(); i++) {
            //iterate through all days
            //if date of split equals date of stock, update divisor/stocks
            if (!split.isEmpty()) {
                if (stockList.get(i).getDate().equals(split.get(0)[1])) {
                    double adjustment = Double.parseDouble(split.remove(0)[4]);
                    divisor *= adjustment;
                    //update after adjusting divisor
                    updateStock(stockList.get(i), divisor);
                } else {
                    updateStock(stockList.get(i), divisor);
                }
            }
            //else, update stocks by current divisor
            else {
                updateStock(stockList.get(i), divisor);
            }
        }
        Deque<StockData> orderedResult = new ArrayDeque<>();
        for (StockData s : stockList) {
            orderedResult.addFirst(s);
        }
        return orderedResult;
    }

    static void printSplit(ArrayList<String[]> split, int size) {
        for (String[] s : split) {
            System.out.printf("%s split on %s \t%.2f -->\t %.2f%n", s[0], s[1], Double.parseDouble(s[3]), Double.parseDouble(s[2]));
        }
        System.out.printf("%d splits in %d trading days%n", split.size(), size);
    }

    //update stock by a given divisor
    static void updateStock(StockData stock, Double divisor) {
        //adjust all fields by divisor
        stock.setOpen_price(stock.getOpen_price()/divisor);
        stock.setClose_price(stock.getClose_price()/divisor);
        stock.setHigh_price(stock.getHigh_price()/divisor);
        stock.setLow_price(stock.getLow_price()/divisor);
    }

    static void doStrategy(String ticker, Deque<StockData> data) {
        // Apply Steps 2.6 to 2.10 explained in the assignment description
        // data (which is a Deque) has all the information (after the split adjustment) you need to apply these steps
        if (data.size() <= 50) {
            System.out.printf("Less than 50 days of trading --> No investment%nNet cash: 0%n");
        } else {
            //compute initial 50 day average
            Deque<StockData> fiftyDays = new ArrayDeque<>();
            for (int i = 0; i < 50; i++) {
                fiftyDays.add(data.pop());
            }
            double avg = computeAverage(fiftyDays);

            //initialize investment data
            double cash = 0;
            int shares = 0;
            int transactions = 0;
            while(data.size() > 0) {
                StockData currStock = data.pop();
                double open = currStock.getOpen_price();
                double close = currStock.getClose_price();

                //buy
                if (close < avg && ((close/open) < 0.97000001)) {
                    shares += 100;
                    cash -= 100 * (data.peekFirst().getOpen_price());

                    //transaction fee
                    cash -= 8;
                    transactions++;
                }
                //sell
                else if (shares >= 100 && (open > avg) && ((open/fiftyDays.getLast().getClose_price()) > 1.00999999) ){
                    shares -= 100;
                    cash += 100*(open + close)/2;

                    //transaction fee
                    cash -= 8;
                    transactions++;
                }
                fiftyDays.removeFirst();
                fiftyDays.addLast(currStock);
                avg = computeAverage(fiftyDays);
            }
            if (shares > 0) {
                cash += data.getFirst().getOpen_price()*shares;
                transactions++;
            }
            //output investment results
            System.out.printf("Transactions executed: %d%nNet Cash: %.2f%n", transactions, cash);
        }
    }

    //compute 50 day average
    public static double computeAverage(Deque<StockData> data) {
        List<StockData> values = new ArrayList<>(data);
        double sum = 0;
        for (StockData s : values) {
            sum += s.getClose_price();
        }
        return sum/50;
    }
}