package user;

import java.util.LinkedList;
import java.util.List;

public class BankAccount {
    private int balance;
    private List<History> historyList;


    public BankAccount() {
        this.balance = 0;
        this.historyList = new LinkedList<>();
    }

    public int getBalance() {
        return balance;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setBalance(int balance) {
        this.balance = this.balance + balance;
    }


    public void addNewHistory(int tradingType, String symbolName, int AmountHistory) {
        this.historyList.add(new History(tradingType,symbolName,AmountHistory,this.balance,this.balance+AmountHistory));
    }
}
