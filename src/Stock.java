public class Stock {
    private final String symbol;
    private final String name;
    private double price;
    private double previousPrice;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.previousPrice = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setPrice(double newPrice) {
        this.previousPrice = this.price;
        this.price = Math.max(1.0, Math.round(newPrice * 100.0) / 100.0);
    }

    public double getChangePercent() {
        return previousPrice == 0 ? 0 : (price - previousPrice) / previousPrice * 100.0;
    }
}
