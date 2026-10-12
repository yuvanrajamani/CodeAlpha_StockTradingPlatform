import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/** Simulated stock market with randomly moving prices. */
public class Market {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();
    private final Random random = new Random();

    public Market() {
        add(new Stock("AAPL", "Apple Inc.", 190.00));
        add(new Stock("GOOG", "Alphabet Inc.", 140.00));
        add(new Stock("MSFT", "Microsoft Corp.", 410.00));
        add(new Stock("TSLA", "Tesla Inc.", 250.00));
        add(new Stock("AMZN", "Amazon.com Inc.", 175.00));
        add(new Stock("INFY", "Infosys Ltd.", 18.50));
    }

    private void add(Stock s) {
        stocks.put(s.getSymbol(), s);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public Iterable<Stock> getAllStocks() {
        return stocks.values();
    }

    /** Moves every stock's price by a random amount between -5% and +5%. */
    public void simulateDay() {
        for (Stock s : stocks.values()) {
            double changePct = (random.nextDouble() - 0.5) * 0.10;
            s.setPrice(s.getPrice() * (1 + changePct));
        }
    }

    public void displayMarket() {
        System.out.println("\n--------------- MARKET DATA ---------------");
        System.out.printf("%-6s %-18s %10s %9s%n", "Symbol", "Company", "Price($)", "Change");
        System.out.println("-------------------------------------------");
        for (Stock s : stocks.values()) {
            System.out.printf("%-6s %-18s %10.2f %+8.2f%%%n",
                    s.getSymbol(), s.getName(), s.getPrice(), s.getChangePercent());
        }
    }
}
