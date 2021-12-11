package command;

import Exceptions.AmoutIsLowerThenZero;
import Exceptions.CompanyNameIsAlreadyExists;
import Exceptions.SymbolIsAlreadyExists;
import command.Enum.CommandTypes;
import command.Enum.TradingTypes;
import stock.Stock;
import transaction.Transactions;

import java.util.Comparator;

public class Command {

    private TradingTypes tradingT;
    private CommandTypes commandT;
    private String symbol;
    private int price;
    private int stockAmount;
    private String commandDate;
    private String InitiatorName ;

    public Command(TradingTypes type, CommandTypes buyOrSellCommand, String symbol, int price, int stockAmount, String commandDate ,String userName) {
        this.tradingT = type;
        this.commandT = buyOrSellCommand;
        this.symbol = symbol;
        this.price = price;
        this.stockAmount = stockAmount;
        this.commandDate = commandDate;
        this.InitiatorName = userName ;
    }

    public TradingTypes getTradingT() {
        return tradingT;
    }

    public void setTradingT(TradingTypes tradingT) {
        this.tradingT = tradingT;
    }

    public CommandTypes getCommandT() {
        return commandT;
    }

    public void setCommandT(CommandTypes commandT) {
        this.commandT = commandT;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public String getCommandDate() {
        return commandDate;
    }

    public String getInitiatorName() { return InitiatorName; }

    public static class BuyComparator implements Comparator<Command> {
        @Override
        public int compare(Command o1, Command o2) {
            if (o1.getPrice() < o2.getPrice()) {
                return 1;
            } else if (o1.getPrice() == o2.getPrice()) {
                return o1.getCommandDate().compareTo(o2.getCommandDate());
            }
            return -1;
        }
    }

    public static class SellComparator implements Comparator<Command> {

        @Override
        public int compare(Command o1, Command o2) {
            if (o1.getPrice() > o2.getPrice()) {
                return 1;
            } else if (o1.getPrice() == o2.getPrice()) {
                return o1.getCommandDate().compareTo(o2.getCommandDate());
            }
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Command{" +
                "tradingT=" + tradingT +
                ", commandT=" + commandT +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", stockAmount=" + stockAmount +
                ", commandDate='" + commandDate + '\'' +
                ", InitiatorName='" + InitiatorName + '\'' +
                '}';
    }


}
