import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Market market = new Market();
        Portfolio portfolio = null;

        if (DataStore.exists()) {
            System.out.print("Saved portfolio found. Load it? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) portfolio = DataStore.load();
        }
        if (portfolio == null) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            portfolio = new Portfolio(name.isEmpty() ? "Trader" : name, 10000.00);
            portfolio.recordSnapshot(market);
            System.out.println("Welcome, " + portfolio.getUsername() + "! You start with $10,000.00.");
        } else {
            System.out.println("Welcome back, " + portfolio.getUsername() + "!");
        }

        boolean running = true;
        while (running) {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. View market data");
            System.out.println("2. Buy stock");
            System.out.println("3. Sell stock");
            System.out.println("4. View portfolio");
            System.out.println("5. View transaction history");
            System.out.println("6. Advance market by one day");
            System.out.println("7. View portfolio performance over time");
            System.out.println("8. Save portfolio");
            System.out.println("0. Save & exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> market.displayMarket();
                case "2" -> trade(market, portfolio, true);
                case "3" -> trade(market, portfolio, false);
                case "4" -> portfolio.displayPortfolio(market);
                case "5" -> portfolio.displayTransactions();
                case "6" -> {
                    market.simulateDay();
                    portfolio.recordSnapshot(market);
                    System.out.println("A day passes... prices have changed.");
                    market.displayMarket();
                }
                case "7" -> portfolio.displayPerformance();
                case "8" -> {
                    DataStore.save(portfolio);
                    System.out.println("Portfolio saved.");
                }
                case "0" -> {
                    DataStore.save(portfolio);
                    System.out.println("Portfolio saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void trade(Market market, Portfolio portfolio, boolean buying) {
        market.displayMarket();
        System.out.print("Enter stock symbol: ");
        Stock stock = market.getStock(scanner.nextLine().trim());
        if (stock == null) {
            System.out.println("Unknown symbol.");
            return;
        }
        System.out.print("Quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }
        String error = buying ? portfolio.buy(stock, qty) : portfolio.sell(stock, qty);
        if (error != null) {
            System.out.println("Failed: " + error);
        } else {
            System.out.printf("%s %d x %s @ $%.2f. Cash left: $%,.2f%n",
                    buying ? "Bought" : "Sold", qty, stock.getSymbol(), stock.getPrice(), portfolio.getCash());
        }
    }
}
