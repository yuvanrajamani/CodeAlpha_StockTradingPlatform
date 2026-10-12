# CodeAlpha_StockTradingPlatform

A console-based stock trading simulator in Java (CodeAlpha Java Internship, Task 2).

## Features
- Market data display with simulated price movement (random -5% to +5% per day)
- Buy / sell operations with validation (funds, shares owned)
- Portfolio view with average cost, current value and profit/loss per holding
- Portfolio performance over time (net worth recorded each simulated day, shown as a bar chart)
- Full transaction history
- File I/O persistence: portfolio is saved to `portfolio_data.txt` and can be reloaded

## OOP Design
| Class | Responsibility |
|---|---|
| `Stock` | Symbol, name, current/previous price |
| `Market` | Holds stocks, simulates daily price changes, prints market data |
| `Transaction` | One BUY or SELL record |
| `Portfolio` | User's cash, holdings, cost basis, history, buy/sell logic |
| `DataStore` | Saves/loads portfolio to a text file |
| `Main` | Menu-driven console interface |

## How to Run
Requires JDK 17+.
```bash
cd src
javac *.java
java Main
```

## Sample Flow
1. Start with $10,000
2. Buy 10 AAPL, buy 5 TSLA
3. Advance the market a few days
4. View portfolio and performance chart
5. Save & exit, then reload next time
