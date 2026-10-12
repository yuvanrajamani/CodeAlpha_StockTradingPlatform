import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Transaction {
    public enum Type { BUY, SELL }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Type type;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final LocalDateTime time;

    public Transaction(Type type, String symbol, int quantity, double price, LocalDateTime time) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.time = time;
    }

    public Transaction(Type type, String symbol, int quantity, double price) {
        this(type, symbol, quantity, price, LocalDateTime.now());
    }

    public Type getType() { return type; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return quantity * price; }

    public String toFileLine() {
        return type + "," + symbol + "," + quantity + "," + price + "," + time;
    }

    public static Transaction fromFileLine(String line) {
        String[] p = line.split(",");
        return new Transaction(Type.valueOf(p[0]), p[1], Integer.parseInt(p[2]),
                Double.parseDouble(p[3]), LocalDateTime.parse(p[4]));
    }
    public String toString() {
        return String.format("%s  %-4s %-6s x%-4d @ $%.2f  = $%.2f",
                time.format(FMT), type, symbol, quantity, price, getTotal());
    }
}
