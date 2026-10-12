import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Saves and loads a Portfolio to a plain-text file (file I/O persistence). */
public class DataStore {
    private static final Path FILE = Paths.get("portfolio_data.txt");

    public static boolean exists() {
        return Files.exists(FILE);
    }

    public static void save(Portfolio p) {
        List<String> lines = new ArrayList<>();
        lines.add("USER," + p.getUsername() + "," + p.getStartingCash() + "," + p.getCash());
        for (Map.Entry<String, Integer> e : p.getHoldings().entrySet()) {
            lines.add("HOLD," + e.getKey() + "," + e.getValue() + "," + p.getTotalCost().get(e.getKey()));
        }
        for (Transaction t : p.getTransactions()) lines.add("TX," + t.toFileLine());
        for (double v : p.getValueHistory()) lines.add("HIST," + v);
        try {
            Files.write(FILE, lines);
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    public static Portfolio load() {
        try {
            List<String> lines = Files.readAllLines(FILE);
            Portfolio p = null;
            for (String line : lines) {
                String[] parts = line.split(",", 2);
                String[] f = parts[1].split(",");
                switch (parts[0]) {
                    case "USER" -> {
                        p = new Portfolio(f[0], Double.parseDouble(f[1]));
                        p.setCash(Double.parseDouble(f[2]));
                    }
                    case "HOLD" -> {
                        p.getHoldings().put(f[0], Integer.parseInt(f[1]));
                        p.getTotalCost().put(f[0], Double.parseDouble(f[2]));
                    }
                    case "TX" -> p.getTransactions().add(Transaction.fromFileLine(parts[1]));
                    case "HIST" -> p.getValueHistory().add(Double.parseDouble(f[0]));
                    default -> { }
                }
            }
            return p;
        } catch (Exception e) {
            System.out.println("Could not load saved data: " + e.getMessage());
            return null;
        }
    }
}
