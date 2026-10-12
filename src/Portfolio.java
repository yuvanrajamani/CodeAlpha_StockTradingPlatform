import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class Portfolio {
    private final String username;
    private double cash;
    private final double startingCash;
    private final Map<String, Integer> holdings = new LinkedHashMap<>();
    private final Map<String, Double> totalCost = new LinkedHashMap<>(); // cost basis per symbol
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<Double> valueHistory = new ArrayList<>(); // net worth snapshots

    public Portfolio(String username, double startingCash) {
        this.username = username;
        this.cash = startingCash;
        this.startingCash = startingCash;
    }

    public String getUsername() { return username; }
    public double getCash() { return cash; }
    public double getStartingCash() { return startingCash; }
    public Map<String, Integer> getHoldings() { return holdings; }
    public Map<String, Double> getTotalCost() { return totalCost; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<Double> getValueHistory() { return valueHistory; }

    public void setCash(double cash) { this.cash = cash; }
    public String buy(Stock stock, int qty) {
        if (qty <= 0) return "Quantity must be positive.";
        double cost = stock.getPrice() * qty;
        if (cost > cash) {
            return String.format("Insufficient funds. Need $%.2f but have $%.2f.", cost, cash);
        }
        cash -= cost;
        holdings.merge(stock.getSymbol(), qty, Integer::sum);
        totalCost.merge(stock.getSymbol(), cost, Double::sum);
        transactions.add(new Transaction(Transaction.Type.BUY, stock.getSymbol(), qty, stock.getPrice()));
        return null;
    }
    public String sell(Stock stock, int qty) {
        if (qty <= 0) return "Quantity must be positive.";
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (qty > owned) {
            return "You only own " + owned + " share(s) of " + stock.getSymbol() + ".";
        }
        double avgCost = totalCost.get(stock.getSymbol()) / owned;
        cash += stock.getPrice() * qty;
        if (qty == owned) {
            holdings.remove(stock.getSymbol());
            totalCost.remove(stock.getSymbol());
        } else {
            holdings.put(stock.getSymbol(), owned - qty);
            totalCost.put(stock.getSymbol(), avgCost * (owned - qty));
        }
        transactions.add(new Transaction(Transaction.Type.SELL, stock.getSymbol(), qty, stock.getPrice()));
        return null;
    }

    public double getStockValue(Market market) {
        double total = 0;
        for (Map.Entry<String, Integer> e : holdings.entrySet()) {
            total += market.getStock(e.getKey()).getPrice() * e.getValue();
        }
        return total;
    }

    public double getNetWorth(Market market) {
        return cash + getStockValue(market);
    }

    public void recordSnapshot(Market market) {
        valueHistory.add(Math.round(getNetWorth(market) * 100.0) / 100.0);
    }

    public void displayPortfolio(Market market) {
        System.out.println("\n----------------------- PORTFOLIO -----------------------");
        System.out.printf("%-6s %6s %10s %10s %12s %10s%n",
                "Symbol", "Qty", "Avg Cost", "Price", "Value", "P/L");
        System.out.println("---------------------------------------------------------");
        if (holdings.isEmpty()) System.out.println("(no holdings)");
        for (Map.Entry<String, Integer> e : holdings.entrySet()) {
            Stock s = market.getStock(e.getKey());
            int qty = e.getValue();
            double avg = totalCost.get(e.getKey()) / qty;
            double value = s.getPrice() * qty;
            System.out.printf("%-6s %6d %10.2f %10.2f %12.2f %+10.2f%n",
                    s.getSymbol(), qty, avg, s.getPrice(), value, value - totalCost.get(e.getKey()));
        }
        System.out.println("---------------------------------------------------------");
        System.out.printf("Cash:        $%,.2f%n", cash);
        System.out.printf("Stock value: $%,.2f%n", getStockValue(market));
        System.out.printf("Net worth:   $%,.2f%n", getNetWorth(market));
        double pl = getNetWorth(market) - startingCash;
        System.out.printf("Total gain/loss since start: %+,.2f (%+.2f%%)%n", pl, pl / startingCash * 100);
    }

    public void displayPerformance() {
        System.out.println("\n----------- PORTFOLIO PERFORMANCE OVER TIME -----------");
        if (valueHistory.isEmpty()) {
            System.out.println("No history yet. Advance the market a day to record snapshots.");
            return;
        }
        double max = valueHistory.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double min = valueHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double base = min - Math.max(1, (max - min)) * 0.25; // scale bars to the data range
        for (int i = 0; i < valueHistory.size(); i++) {
            double v = valueHistory.get(i);
            int bars = (int) Math.round((v - base) / (max - base) * 40);
            System.out.printf("Day %-3d $%,10.2f %s%n", i, v, "#".repeat(Math.max(1, bars)));
        }
    }

    public void displayTransactions() {
        System.out.println("\n---------------- TRANSACTION HISTORY ----------------");
        if (transactions.isEmpty()) System.out.println("(no transactions)");
        for (Transaction t : transactions) System.out.println(t);
    }
}
