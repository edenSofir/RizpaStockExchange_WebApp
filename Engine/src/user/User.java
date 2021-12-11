package user;

import UserDto.UserDto;
import scheme3.generateClasses.RseHoldings;
import scheme3.generateClasses.RseUser;
import stock.Stocks;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class User {
    private String nameOfUser;
    private Holdings holdings;
    private int TotalValueHoldings;
    private String Role;
    private UserDto userDto;
    private BankAccount bankAccount;
    private Queue<String> messageInfo;

    public User(RseUser user, Stocks stocks) {
        this.nameOfUser = user.getName();
        holdings = new Holdings(user.getRseHoldings(), stocks);
        TotalValueHoldings = holdings.calTotalValHoldings();
        this.userDto = new UserDto(this.nameOfUser, this.holdings, this.TotalValueHoldings);   /**/
        this.bankAccount = new BankAccount();
        this.messageInfo = new PriorityQueue<>();
    }

    public User(String nameOfUser, String role) {
        this.nameOfUser = nameOfUser;
        Role = role;
        this.bankAccount = new BankAccount();
        this.messageInfo = new LinkedList<>();
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }


    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public Holdings getHoldings() {
        if(holdings == null)
        {
            holdings = new Holdings();
        }
        return holdings;
    }

    public void setHoldings(Holdings holdings) {
        this.holdings = holdings;
    }

    public int getTotalValueHoldings() {
        return TotalValueHoldings;
    }

    public void setTotalValueHoldings(int totalValueHoldings) {
        TotalValueHoldings = totalValueHoldings;
    }

    public String getRole() {
        return Role;
    }

    public void setHoldingsFromXml(RseHoldings rseHoldings, Stocks stocks) {
        if (holdings == null)
            holdings = new Holdings(rseHoldings, stocks);
        else
            holdings.createUserHoldings(rseHoldings.getRseItem());
    }

    public void addNewItem(String symbolName, int amount)
    {
        Item currItem = new Item(symbolName,amount);
        if(holdings==null)
        {
            this.holdings = new Holdings();
            holdings.getUserHoldings().add(currItem);
        }
        else
            holdings.getUserHoldings().add(currItem);
    }

    public History createHistory(int amount, String symbol, int balanceBeforeTrade, int balanceAfterTrade, int tradeType) {
        History history = new History(tradeType, symbol, amount, balanceBeforeTrade, balanceAfterTrade);
        return history;
    }

    public void addHistoryToList(History newHistory) {
        bankAccount.getHistoryList().add(newHistory);
    }

    public Queue<String> getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(Queue<String> messageInfo) {
        this.messageInfo = messageInfo;
    }

    public void addMessage(String string)
    {
        this.messageInfo.add(string);
    }
}

