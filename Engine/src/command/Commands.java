package command;

import Exceptions.AmoutIsLowerThenZero;
import Exceptions.CompanyNameIsAlreadyExists;
import Exceptions.NewCommandEx;
import Exceptions.SymbolIsAlreadyExists;
import TransactionD.TransactionDto;
import TransactionD.TransactionsDto;
import command.Enum.CommandTypes;
import command.Enum.TradingTypes;
import stock.Stock;
import transaction.Transaction;
import transaction.Transactions;
import user.User;
import user.Users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Commands {

    private List<Command> commandsBuyList;
    private List<Command> commandsSellList;
    private int TotalCycleBuyCommand;
    private int TotalCycleSellCommand;
    private TransactionsDto transactionsDto;

    public Commands() {
        commandsSellList = new LinkedList<>();
        commandsBuyList = new LinkedList<>();
        TotalCycleBuyCommand = 0;
        TotalCycleSellCommand = 0;
    }

    public int getTotalCycleBuyCommand() {
        return TotalCycleBuyCommand;
    }

    public void setTotalCycleBuyCommand(int totalCycleBuyCommand) {
        TotalCycleBuyCommand = totalCycleBuyCommand;
    }

    public int getTotalCycleSellCommand() {
        return TotalCycleSellCommand;
    }

    public void setTotalCycleSellCommand(int totalCycleSellCommand) {
        TotalCycleSellCommand = totalCycleSellCommand;
    }

    public List<Command> getCommandsBuyList() {
        return commandsBuyList;
    }

    public void setCommandsBuyList(List<Command> commandsBuyList) {
        this.commandsBuyList = commandsBuyList;
    }

    public List<Command> getCommandsSellList() {
        return commandsSellList;
    }

    public void setCommandsSellList(List<Command> commandsSellList) {
        this.commandsSellList = commandsSellList;
    }

    public TransactionsDto compareSellOrBuyLmt(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1://i'm buying
                return createMatchForLmt(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2: //i'm selling
                return createMatchForLmt(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }

    public TransactionsDto compareSellOrBuyMkt(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1:
                return createMatchForMkt(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2:
                return createMatchForMkt(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }
    public TransactionsDto compareSellOrBuyFok(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1:
                return createMatchForFok(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2:
                return createMatchForFok(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }
    public TransactionsDto compareSellOrBuyIoc(int tradingType, Command currCommand, Stock currStock, Users users) {
        switch (tradingType) {
            case 1:
                return createMatchForIoc(currStock.getCommands().commandsSellList, currCommand, currStock, tradingType, users);
            case 2:
                return createMatchForIoc(currStock.getCommands().commandsBuyList, currCommand, currStock, tradingType, users);
        }
        return null;
    }


    public TransactionsDto createMatchForFok(List<Command> commandList, Command currCommand, Stock currStock, int tradingType, Users users)
    {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        int isAmountMoreOrEqual =0 ;
        Iterator<Command> iterator = commandList.iterator();
        while(iterator.hasNext()) //בדיקה מלכתחילה שיש מספיק פקודות ממתינות בכמות ובמחיר לפקודת FOK המתקבלת
        {
            Command newCommand = iterator.next();
            if(tradingType==1 && currCommand.getPrice() >= newCommand.getPrice() )
            {
                isAmountMoreOrEqual= isAmountMoreOrEqual + newCommand.getStockAmount();
            }
            else if(tradingType ==2 && currCommand.getPrice() <= newCommand.getPrice())
            {
                isAmountMoreOrEqual = isAmountMoreOrEqual + newCommand.getStockAmount();
            }
        }
        if(isAmountMoreOrEqual >= currCommand.getStockAmount()) //
             {
            iterator = commandList.iterator(); //return to the beginning of the list
           User currUser = users.symbol2User(currCommand.getInitiatorName());
            while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) {
                Command newCommand = iterator.next();
                User newUser = users.symbol2User(newCommand.getInitiatorName());
                if ((newCommand.getPrice() <= currCommand.getPrice()) && tradingType == 1) {

                    func(newCommand , currCommand , currStock, totalAmount , newUser , currUser , newTransections,  tradingType,users);
                    if (newCommand.getStockAmount() == 0) {
                        iterator.remove();
                    }
                } else if ((newCommand.getPrice() >= currCommand.getPrice()) && tradingType == 2) {
                    Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                    totalAmount = totalAmount + transaction.getAmount();
                    newTransections.add(transaction);
                    if (newCommand.getStockAmount() == 0) {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT(), newCommand.getSymbol(), transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //146+147 necessary
                        iterator.remove();
                    } else {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT(), newCommand.getSymbol(), transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //152+156 necssery
                    }
                    updateBankAccounts(newUser,  currUser , transaction , tradingType , currStock);
                }
            }
            //the currCommand's amount of stock is zero
            currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
            currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
        }
        return new TransactionsDto(newTransections,0);
    }

    public TransactionsDto createMatchForIoc(List<Command> commandList, Command currCommand, Stock currStock, int tradingType, Users users)
    {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        Iterator<Command> iterator = commandList.iterator();
        User currUser = users.symbol2User(currCommand.getInitiatorName());
        while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) {
            Command newCommand = iterator.next();
            User newUser = users.symbol2User(newCommand.getInitiatorName());
            if ((newCommand.getPrice() <= currCommand.getPrice()) && tradingType == 1) {
                /*Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                totalAmount = totalAmount + transaction.getAmount();
                newUser.getHoldings().updateHoldings(newCommand.getTradingT(),newCommand.getSymbol(), transaction.getAmount());
                newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings());
                newTransections.add(transaction);
                updateBankAccounts(newUser , currUser , transaction , tradingType);*/
                func(newCommand , currCommand , currStock, totalAmount , newUser , currUser , newTransections,  tradingType ,users);
                if (newCommand.getStockAmount() == 0) {
                    iterator.remove();
                }
            } else if ((newCommand.getPrice() >= currCommand.getPrice()) && tradingType == 2) {
                Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                totalAmount = totalAmount + transaction.getAmount();
                newTransections.add(transaction);
                updateBankAccounts(newUser , currUser , transaction , tradingType , currStock);
                if (newCommand.getStockAmount() == 0) {
                    newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() ,transaction.getAmount());
                    newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //146+147 necessary
                    iterator.remove();
                }
                else
                {
                    newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() , transaction.getAmount());
                    newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //152+156 necssery
                }
            }
        }
        currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
        currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());

        return new TransactionsDto(newTransections,0);
    }

    public TransactionsDto createMatchForMkt(List<Command> commandList, Command currCommand, Stock currStock, int tradingType, Users users) {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        int price = 0 ;
        Iterator<Command> iterator = commandList.iterator();
        User currUser = users.symbol2User(currCommand.getInitiatorName());
        if (commandList.isEmpty()) {
            currStock.putNewCommandInCommands(currCommand, tradingType);
        } else {
            while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) { //newCommand = listCommand , currCommand == counterCommand
                Command newCommand = iterator.next();
                User newUser = users.symbol2User(newCommand.getInitiatorName());
                 totalAmount = func(newCommand , currCommand , currStock, totalAmount , newUser , currUser , newTransections,  tradingType ,users);
                price = newCommand.getPrice();
                if (newCommand.getStockAmount() == 0) {
                    iterator.remove();
                }
            }
            if (currCommand.getStockAmount() > 0) {
                currCommand.setPrice(price);
                currStock.putNewCommandInCommands(currCommand, tradingType);
            }
            currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
            currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
        }

        return new TransactionsDto(newTransections,0);
    }

    public TransactionsDto createMatchForLmt(List<Command> commandList, Command currCommand, Stock currStock, int tradingType ,Users users) {
        List<Transaction> newTransections = new LinkedList<>() ;
        int totalAmount = 0;
        User currUser = users.symbol2User(currCommand.getInitiatorName());
        Iterator<Command> iterator = commandList.iterator();
        if (commandList.isEmpty()) {
            currStock.putNewCommandInCommands(currCommand, tradingType);
        } else {
            while ((iterator.hasNext()) && (currCommand.getStockAmount() > 0)) {
                Command newCommand = iterator.next();
                User newUser = users.symbol2User(newCommand.getInitiatorName());
                if ((newCommand.getPrice() <= currCommand.getPrice()) && tradingType == 1) {
                    /*Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                    totalAmount = totalAmount + transaction.getAmount();
                    newUser.getHoldings().updateHoldings(newCommand.getTradingT(),newCommand.getSymbol(), transaction.getAmount());
                    newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings());
                    newTransections.add(transaction);
                    updateBankAccounts(newUser , currUser , transaction , tradingType);*/
                    totalAmount = func(newCommand , currCommand , currStock, totalAmount , newUser , currUser , newTransections,  tradingType ,users);
                    if (newCommand.getStockAmount() == 0) {
                        iterator.remove();
                    }
                } else if ((newCommand.getPrice() >= currCommand.getPrice()) && tradingType == 2) {
                    Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
                    if(currCommand.getTradingT().equals("BUY"))
                        SetMessageForUser(currCommand,newCommand,transaction,users);
                    else
                        SetMessageForUser(newCommand,currCommand,transaction,users);
                    SetMessageForUser(currCommand,newCommand,transaction,users);
                    totalAmount = totalAmount + transaction.getAmount();
                    newTransections.add(transaction);
                    updateBankAccounts(newUser , currUser , transaction , tradingType , currStock);
                    if (newCommand.getStockAmount() == 0) {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() ,transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //146+147 necessary
                        iterator.remove();
                    }
                    else
                    {
                        newUser.getHoldings().updateHoldings(newCommand.getTradingT() , newCommand.getSymbol() , transaction.getAmount());
                        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings()); //152+156 necssery
                    }
                }
            }
            if (currCommand.getStockAmount() > 0) {
                currStock.putNewCommandInCommands(currCommand, tradingType);
            }

            currUser.getHoldings().updateHoldings(currCommand.getTradingT(),currCommand.getSymbol(),totalAmount);
            currUser.setTotalValueHoldings(currUser.getHoldings().calTotalValHoldings());
        }
        return new TransactionsDto(newTransections,0);
    }

    public int func(Command newCommand , Command currCommand , Stock currStock , int totalAmount , User newUser
            , User currUser , List<Transaction> newTransections , int tradingType ,Users users) {

        Transaction transaction = creatTransaction(newCommand, currCommand, currStock);
        if(currCommand.getTradingT().equals("BUY"))
            SetMessageForUser(currCommand,newCommand,transaction,users);
        else
            SetMessageForUser(newCommand,currCommand,transaction,users);
        totalAmount = totalAmount + transaction.getAmount();
        newUser.getHoldings().updateHoldings(newCommand.getTradingT(),newCommand.getSymbol(), transaction.getAmount());
        newUser.setTotalValueHoldings(newUser.getHoldings().calTotalValHoldings());
        newTransections.add(transaction);
        updateBankAccounts(newUser , currUser , transaction , tradingType , currStock);
        return totalAmount ;
    }

    public void SetMessageForUser(Command buyer , Command seller , Transaction transaction ,Users users)
    {
        if(buyer.getStockAmount()-transaction.getAmount()>0) {
            String message = "A transaction was made. Symbol: " + buyer.getSymbol() + "Command type: SELL. Rate: " + transaction.getPrice() +
                    "Amount: " + transaction.getAmount() + "Your Command wasn't committed fully.";
            users.getUsersList().get(buyer.getInitiatorName()).addMessage(message);

            message = "A transaction was made. Symbol: " + buyer.getSymbol() + "Command type: BUY. Rate: " + transaction.getPrice() +
                    "Amount: " + transaction.getAmount() + "Your Command was committed fully.";
            users.getUsersList().get(seller.getInitiatorName()).addMessage(message);
        }
        else{
            String message = "A transaction was made. Symbol: " + buyer.getSymbol() + "Command type: SELL. Rate: " + transaction.getPrice() +
                    "Amount: " + transaction.getAmount() + "Your Command was committed fully.";
            users.getUsersList().get(buyer.getInitiatorName()).addMessage(message);

            message = "A transaction was made. Symbol: " + buyer.getSymbol() + "Command type: BUY. Rate: " + transaction.getPrice() +
                    "Amount: " + transaction.getAmount() + "Your Command wasn't committed fully.";
            users.getUsersList().get(seller.getInitiatorName()).addMessage(message);
        }
    }

    public void updateBankAccounts(User newUser , User currUser , Transaction currTrans , int tradingType ,Stock currStock)
    {
        int newUserBalance = newUser.getBankAccount().getBalance();
        int currUserBalance = currUser.getBankAccount().getBalance();
        if(tradingType == 1) {
            newUser.getBankAccount().setBalance(currTrans.getTotalValue());
            newUser.addHistoryToList(newUser.createHistory(currTrans.getTotalValue() ,currStock.getSymbol() ,newUserBalance , (newUserBalance + currTrans.getTotalValue()), tradingType));
            currUser.getBankAccount().setBalance((-1)*currTrans.getTotalValue());
            currUser.addHistoryToList(currUser.createHistory(currTrans.getTotalValue(), currStock.getSymbol() ,currUserBalance , (currUserBalance - currTrans.getTotalValue()), 2));

        }else{
            newUser.getBankAccount().setBalance((-1)*currTrans.getTotalValue());
            newUser.addHistoryToList(newUser.createHistory(currTrans.getTotalValue() , currStock.getSymbol() , newUserBalance , (newUserBalance - currTrans.getTotalValue()), tradingType));
            currUser.getBankAccount().setBalance(currTrans.getTotalValue());
            currUser.addHistoryToList(currUser.createHistory(currTrans.getTotalValue() , currStock.getSymbol() , currUserBalance , (currUserBalance + currTrans.getTotalValue()), 1));
        }
    }

    public void SortBuyList() {
        commandsBuyList.sort(new Command.BuyComparator());
    }

    public void SortSellList() {
        commandsSellList.sort(new Command.SellComparator());
    }


    public Transaction creatTransaction(Command CounterOrder, Command currCommand, Stock currStock) {
        Transaction newTransaction = null;
        int finalStockAmount = CounterOrder.getStockAmount() - currCommand.getStockAmount();

        if (finalStockAmount >= 0) {
            if(currCommand.getTradingT().equals(TradingTypes.SELL))
                newTransaction = new Transaction("", currCommand.getStockAmount(), CounterOrder.getPrice(), currCommand.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            else
                newTransaction = new Transaction("", currCommand.getStockAmount(), CounterOrder.getPrice(), currCommand.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            currStock.putNewTransactionInTransactions(newTransaction);

            currStock.getTransactions().setSumOfCycleTran(currStock.getTransactions().getSumOfCycleTran() + (currCommand.getStockAmount() * CounterOrder.getPrice()));
            CounterOrder.setStockAmount(finalStockAmount);
            currCommand.setStockAmount(0);
            return newTransaction;

        } else {
            newTransaction = new Transaction("", CounterOrder.getStockAmount(), CounterOrder.getPrice(), CounterOrder.getStockAmount() * CounterOrder.getPrice(), CounterOrder, currCommand);
            currStock.putNewTransactionInTransactions(newTransaction);

            currStock.getTransactions().setSumOfCycleTran(currStock.getTransactions().getSumOfCycleTran() + (CounterOrder.getStockAmount() * CounterOrder.getPrice()));
            CounterOrder.setStockAmount(0);
            currCommand.setStockAmount(finalStockAmount * (-1));
            return newTransaction;
        }

    }

    @Override
    public String toString() {
        return "The list of commands are: " +
                "commandsBuyList: " + commandsBuyList +
                "\n commandsSellList=" + commandsSellList;
    }

    public void addNewCommand(TradingTypes tradingType ,CommandTypes typeCommand , String symbol, int price , int amountOfStock , String date , String initiator)
    {
        Command newCommand = new Command(tradingType, typeCommand , symbol , price , amountOfStock , date , initiator);
        if(tradingType.toString().equals("BUY"))
        {
            this.commandsBuyList.add(newCommand);
        }
        else
            this.commandsSellList.add(newCommand);

    }


}


