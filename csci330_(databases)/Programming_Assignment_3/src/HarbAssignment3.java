/*
This is a Java skeleton code to help you out how to start this assignment.
Please feel free to use this skeleton code.
Please give a closer look at the "To Do" parts of this file. You may get an idea of how to finish this assignment.
*/

// connect to server: ssh -N -p922 -L4321:mysql.cs.wwu.edu:3306 harba@proxy.cs.wwu.edu

import java.sql.*;
import java.util.*;
import java.io.*;

class HarbAssignment3 {

    static final String defaultReaderParams = "readerparams.txt";
    static final String defaultWriterParams = "writerparams.txt";
    static Connection readerConn = null;
    static Connection writerConn = null;

    static final String getDatesQuery =
            "select max(startDate) as start, min(endDate) as end" +
                    "  from (select Ticker, min(TransDate) as StartDate, max(TransDate) as EndDate," +
                    "            count(distinct TransDate) as tradingDays" +
                    "          from company natural join pricevolume" +
                    "          where Industry = ?" +
                    "          group by Ticker" +
                    "          having tradingDays >= ?) as TickerDates";

    static final String getTickerDatesQuery =
            "select Ticker, min(TransDate) as StartDate, max(TransDate) as EndDate," +
                    "      count(distinct TransDate) as tradingDays" +
                    "  from company natural join pricevolume" +
                    "  where Industry = ?" +
                    "    and TransDate >= ? and TransDate <= ?" +
                    "  group by Ticker" +
                    "  having tradingDays >= ?" +
                    "  order by Ticker";

    static final String getTickerQuery = "select Ticker, TransDate, OpenPrice, ClosePrice" +
            "  from pricevolume natural join company" +
            "  where Industry = ?" +
            "   and Ticker = ?" +
            "    and TransDate >= ? and TransDate <= ?" +
            "  order by TransDate, Ticker";

    static final String getAllIndustries =
            "select distinct Industry" +
                    "  from company" +
                    "  order by Industry";

    static final String dropPerformanceTable =
            "drop table if exists Performance;";

    static final String createPerformanceTable =
            "create table Performance (" +
                    "  Industry char(30)," +
                    "  Ticker char(6)," +
                    "  StartDate char(10)," +
                    "  EndDate char(10)," +
                    "  TickerReturn char(12)," +
                    "  IndustryReturn char(12)" +
                    "  );";

    static final String insertPerformance =
            "insert into Performance(Industry, Ticker, StartDate, EndDate, TickerReturn, IndustryReturn)" +
                    "  values(?, ?, ?, ?, ?, ?);";

    static final String updatePerformance = "update Performance" +
            " set IndustryReturn = ?" +
            " where Ticker = ?" +
            " and StartDate = ?";

    static class IndustryData {
        // To Do: Create this class which  contains the list of the tickers, the common days, start date, and end date

        public IndustryData(List<String> tickerList, String startDate, String endDate, int commonDays) {
            this.tickerList = tickerList;
            this.startDate = startDate;
            this.endDate = endDate;
            this.commonDays = commonDays;
        }

        private List<String> tickerList;
        private String startDate;
        private String endDate;
        private int commonDays;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public List getTickerList() {
            return tickerList;
        }

        public int getCommonDays() {
            return commonDays;
        }

        public void setCommonDays(int commonDays) {
            this.commonDays = commonDays;
        }
    }

    static class IntervalData {

        public IntervalData(String ticker, List<Double> tickerReturn) {
            this.ticker = ticker;
            this.tickerReturn = tickerReturn;
        }
        String ticker;
        List<Double> tickerReturn;

        public String getTicker() {
            return ticker;
        }

        public List<Double> getTickerReturn() {
            return tickerReturn;
        }
    }

    public static void main(String[] args) throws Exception {
        // Get connection properties
        Properties readerProps = new Properties();
        readerProps.load(new FileInputStream(defaultReaderParams));
        Properties writerProps = new Properties();
        writerProps.load(new FileInputStream(defaultWriterParams));

        try {
            // Setup Reader and Writer Connection
            setupReader(readerProps);
            setupWriter(writerProps);

            List<String> industries = getIndustries();
            System.out.printf("%d industries found%n", industries.size());
            for (String industry : industries) {
                System.out.printf("%s%n", industry);
            }
            System.out.println();

            for (String industry : industries) {
                System.out.printf("Processing %s%n", industry);
                IndustryData iData = processIndustry(industry);
                if (iData != null && iData.tickerList.size() > 2) {
                    System.out.printf("%d accepted tickers for %s(%s - %s), %d common dates%n",
                            iData.tickerList.size(), industry, iData.startDate, iData.endDate, iData.commonDays);
                    processIndustryGains(industry, iData);
                } else
                    System.out.printf("Insufficient data for %s => no analysis%n", industry);
                System.out.println();
            }

            // Close everything you don't need any more

            System.out.println("Database connections closed");
        } catch (SQLException ex) {
            System.out.printf("SQLException: %s%nSQLState: %s%nVendorError: %s%n",
                    ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
        }
    }

    static void setupReader(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        readerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Reader connection %s %s established.%n", dburl, username);
    }

    static void setupWriter(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        writerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Writer connection %s %s established.%n", dburl, username);

        // Create Performance Table; If this table already exists, drop it first
        Statement tstmt = writerConn.createStatement();
        tstmt.execute(dropPerformanceTable);
        tstmt.execute(createPerformanceTable);
        tstmt.close();
    }

    static List<String> getIndustries() throws SQLException {
        List<String> result = new ArrayList<>();
        // To Do: Execute the appropriate query (you need one of them) and return a list of the industries
        PreparedStatement pstmt = readerConn.prepareStatement(getAllIndustries);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            result.add(rs.getString(1));
        }
        return result;
    }

    static IndustryData processIndustry(String industry) throws SQLException {
        // To Do: Execute the appropriate SQL queries (you need two of them) and return an object of IndustryData
        PreparedStatement getDates = readerConn.prepareStatement(getDatesQuery);
        getDates.setString(1, industry);
        getDates.setString(2, "150");
        ResultSet datesRS = getDates.executeQuery();

        String startDate = null;
        String endDate = null;
        while (datesRS.next()) {
            startDate = datesRS.getString("start");
            endDate = datesRS.getString("end");
        }

        PreparedStatement getTickerData = readerConn.prepareStatement(getTickerDatesQuery);
        getTickerData.setString(1, industry);
        getTickerData.setString(2, startDate);
        getTickerData.setString(3, endDate);
        getTickerData.setString(4, "150");
        ResultSet industryRS = getTickerData.executeQuery();

        int numDays = 0;
        List<String> tickers = new ArrayList<>();
        while (industryRS.next()) {
            numDays = Integer.parseInt(industryRS.getString("tradingDays"));
            tickers.add(industryRS.getString("Ticker"));
        }
        return new IndustryData(tickers, startDate, endDate, numDays);
    }

    static void processIndustryGains(String industry, IndustryData data) throws SQLException {
        // To Do:
        // In this method, you should calculate the ticker return and industry return. Look at the assignment description to know how to do that
        // Don't forget to do the split adjustment

        //get list of intervals
        List<String[]> intervals = getIntervals(industry, data.tickerList.get(0), data.getStartDate(), data.getEndDate());

        //store a list of data for each industry
        List<IntervalData> tickerReturnList = new ArrayList<>();
        //for each ticker, iterate through intervals
        for (int i = 0; i < data.getTickerList().size(); i++) {

            String currTicker = (String) data.getTickerList().get(i);
            List<Double> tickerIntervalData = new ArrayList<>();

            for (String[] interval : intervals) {
                //get current ticker and initialize stockdata list
                ArrayList<StockData> currStockData = new ArrayList<>();

                PreparedStatement pstmt1 = readerConn.prepareStatement(getTickerQuery);
                pstmt1.setString(1, industry);
                pstmt1.setString(2, currTicker);
                pstmt1.setString(3, interval[0]);
                pstmt1.setString(4, interval[1]);
                ResultSet rs1 = pstmt1.executeQuery();

                //process ticker return
                while (rs1.next()) {
                    String ticker = rs1.getString("Ticker");
                    String date = rs1.getString("TransDate");
                    double open = Double.parseDouble(rs1.getString("OpenPrice"));
                    double close = Double.parseDouble(rs1.getString("ClosePrice"));
                    currStockData.add(new StockData(ticker, date, open, close));
                }
                updateStockData(currStockData);

                String start = currStockData.get(0).getDate();
                String end = currStockData.get(59).getDate();
                double currOpen = currStockData.get(0).getOpen_price();
                double currClose = currStockData.get(59).getClose_price();
                tickerIntervalData.add(currClose/currOpen);
                double tickerReturn = currClose / currOpen - 1;

                PreparedStatement insertPerformanceData = writerConn.prepareStatement(insertPerformance);
                insertPerformanceData.setString(1, industry);
                insertPerformanceData.setString(2, currTicker);
                insertPerformanceData.setString(3, currStockData.get(0).getDate());
                insertPerformanceData.setString(4, currStockData.get(59).getDate());
                insertPerformanceData.setString(5, String.format("%10.7f", tickerReturn));
                insertPerformanceData.setString(6, "");
                int result = insertPerformanceData.executeUpdate();
                if (result != 1) {
                    System.out.println("Error inserting into table ");
                }
            }
            tickerReturnList.add(new IntervalData(currTicker, tickerIntervalData));
        }

        //iterate through all tickers in specific industry
        for (int i = 0; i < tickerReturnList.size(); i++) {
            //iterate through respective interval for industry
            for (int j = 0; j < intervals.size(); j++) {
                double sum = 0;
                String currTicker = tickerReturnList.get(i).getTicker();
                String startDate = intervals.get(j)[0];
                for (int k = 0; k < tickerReturnList.size(); k++) {
                    sum += tickerReturnList.get(k).getTickerReturn().get(j);
                }
                sum -= tickerReturnList.get(i).getTickerReturn().get(j);
                sum /= (tickerReturnList.size() - 1);

                PreparedStatement insertIndustryReturn = writerConn.prepareStatement(updatePerformance);
                insertIndustryReturn.setString(1, String.format("%10.7f", (sum - 1)));
                insertIndustryReturn.setString(2, currTicker);
                insertIndustryReturn.setString(3, startDate);
                int result = insertIndustryReturn.executeUpdate();
                if (result != 1) {
                    System.out.println("Error updating table.");
                }
            }
        }
    }

    //return list of all intervals for each industry
    private static List<String[]> getIntervals(String industry, String ticker, String startDate, String endDate) throws SQLException {
        PreparedStatement pstmt = readerConn.prepareStatement(getTickerQuery);
        pstmt.setString(1, industry);
        pstmt.setString(2, ticker);
        pstmt.setString(3, startDate);
        pstmt.setString(4, endDate);
        ResultSet rs = pstmt.executeQuery();

        List<String[]> intervals = new ArrayList<>();

        String currStart = null;
        int i = 1;
        while (rs.next()) {
            if (i == 1) {
                i++;
                currStart = rs.getString("TransDate");
            } else if (i == 60) {
                String currEnd = rs.getString("TransDate");
                intervals.add(new String[]{currStart,currEnd});
                i = 1;
            } else {
                i++;
            }
        }
        return intervals;
    }

    static ArrayList<StockData> updateStockData(ArrayList<StockData> stockList) {

        ArrayList<String[]> split = new ArrayList<>();
        for (int i = 0; i < stockList.size()-1; i++) {
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
        return stockList;
    }

    static class StockData {
        // Create this class which should contain the information  (date, open price, high price, low price, close price) for a particular ticker
        private String date;
        private String ticker;
        private double open_price;
        private double close_price;

        public StockData(String ticker, String date, double open_price, double close_price) {
            this.ticker = ticker;
            this.date = date;
            this.open_price = open_price;
            this.close_price = close_price;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
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

        public double getClose_price() {
            return close_price;
        }

        public void setClose_price(double close_price) {
            this.close_price = close_price;
        }
    }

    static void updateStock(StockData stock, Double divisor) {
        //adjust all fields by divisor
        stock.setOpen_price(stock.getOpen_price()/divisor);
        stock.setClose_price(stock.getClose_price()/divisor);
    }
}
