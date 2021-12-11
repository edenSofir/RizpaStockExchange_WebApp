package user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class History {
    private int tradingType ;
    private String symbol;
    private String date;
    private int amount ;
    private int balanceBeforeTrade ;
    private int balanceAfterTrade;

    public History(int tradingType , String symbol , int amount, int balanceBeforeTrade, int balanceAfterTrade) {
        this.tradingType = tradingType;
        this.date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.amount = amount;
        this.balanceBeforeTrade = balanceBeforeTrade;
        this.balanceAfterTrade = balanceAfterTrade;
        this.symbol = symbol ;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getTradingType() {
        return tradingType;
    }

    public void setTradingType(int tradingType) {
        this.tradingType = tradingType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalanceBeforeTrade() {
        return balanceBeforeTrade;
    }

    public void setBalanceBeforeTrade(int balanceBeforeTrade) {
        this.balanceBeforeTrade = balanceBeforeTrade;
    }

    public int getBalanceAfterTrade() {
        return balanceAfterTrade;
    }

    public void setBalanceAfterTrade(int balanceAfterTrade) {
        this.balanceAfterTrade = balanceAfterTrade;
    }
}

