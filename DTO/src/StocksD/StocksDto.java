package StocksD;

import command.Commands;
import stock.Stock;
import transaction.Transactions;
import user.User;

import java.util.HashMap;
import java.util.Map;

public class StocksDto {

    private final Map<String, Stock> allStocks;

    public StocksDto() { allStocks = new HashMap<>(); }

    public StocksDto(Map<String, Stock> allStocks) {
        this.allStocks = allStocks;
    }

    public Map<String, Stock> getAllStocks() {
        return allStocks; }

    @Override
    public String toString() {
        return "allStocks: \n " + allStocks;
    }


    public synchronized void addUser(String symbol , String compnyName , int stockPrice , int numberOfTransactions , int transactionsCycle , Transactions transactions, Commands commands) {
        Stock stock = new Stock(symbol , compnyName , stockPrice ,numberOfTransactions , transactionsCycle , transactions , commands);
        allStocks.put(symbol,stock);
    }


}
