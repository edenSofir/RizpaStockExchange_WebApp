package menu;

import Exceptions.*;
import StocksD.StockDto;
import StocksD.StocksDto;
import TransactionD.TransactionsDto;
import UserDto.UserDto;
import UserDto.UsersDto;
import command.Command;
import command.Commands;
import command.Enum.CommandTypes;
import command.Enum.TradingTypes;
import menu.jaxb.ImportInfo;
import scheme3.generateClasses.RizpaStockExchangeDescriptor;
import scheme3.generateClasses.RseHoldings;
import scheme3.generateClasses.RseItem;
import scheme3.generateClasses.RseUser;
import stock.Stock;
import stock.Stocks;
import user.User;
import user.Users;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuEngineImpl implements MenuEngine {

    private StocksDto stocksDto;
    private StockDto stockDto;
    private UserDto userDto;
    private UsersDto usersDto;
    private Stocks stocks;
    private Commands commands;
    private Users users ;

    public MenuEngineImpl() {

        this.stocks = new Stocks();
        this.commands = new Commands();
        this.users = new Users();
    }

    @Override
    public void readSystemInfoFile(InputStream in ,String name) throws SymbolIsAlreadyExists, CompanyNameIsAlreadyExists, JAXBException, SymbolNotExists {
        ImportInfo importInfo = new ImportInfo();
        RizpaStockExchangeDescriptor resXml = importInfo.unmarshall(in);
        Map<String, Stock> tempStocks = stocks.createMapOfStocks(resXml.getRseStocks().getRseStock());
        if(users.checkHoldings(tempStocks, resXml.getRseHoldings().getRseItem())) {
            stocks.addStocks(tempStocks);
            users.getUsersList().get(name).setHoldingsFromXml(resXml.getRseHoldings(),stocks);
        }
        this.stocksDto = new StocksDto(stocks.getAllStocks());
    }
    

    @Override
    public StockDto showCurrStock(String symbol) throws SymbolNotExists {
        Stock stock = stocks.checkSymbolInAllStocks(symbol);
        this.stockDto = new StockDto(stock.getSymbol(), stock.getCompanyName(), stock.getStockPrice(), stock.getNumberOfTransactions(), stock.getTransactionsCycle(), stock.getTransactions(), stock.getCommands());
        return this.stockDto;
    }
    public UsersDto getAllUsers() {return usersDto;};

    public UserDto currUser(String symbolName) {
        User user = users.symbol2User(symbolName);
        this.userDto = new UserDto(user.getNameOfUser() ,user.getHoldings() , user.getTotalValueHoldings());
        return this.userDto;
    }

    @Override
    public TransactionsDto tradingExecution(int tradingType, String symbol, int amount, int price, int commandType , String UserName) throws SymbolNotExists, AmoutIsLowerThenZero, TradingTypeError {

        Stock currStock = stocks.checkSymbolInAllStocks(symbol);
        switch(commandType)
        {
            case 1:
                Command commandForLmt = new Command(typeCommandVerify(tradingType), commandTypeVerify(commandType), symbol.toUpperCase(), price, amount, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) ,UserName);
                return commands.compareSellOrBuyLmt(tradingType, commandForLmt, currStock,users);
            case 2:
                Command commandForMkt = new Command(typeCommandVerify(tradingType), commandTypeVerify(commandType), symbol.toUpperCase(), currStock.getStockPrice() , amount, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),UserName);
                return commands.compareSellOrBuyMkt(tradingType, commandForMkt, currStock,users);
            case 3:
                Command commandForIoc = new Command(typeCommandVerify(tradingType), commandTypeVerify(commandType), symbol.toUpperCase(), price, amount, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) ,UserName);
                return commands.compareSellOrBuyIoc(tradingType, commandForIoc, currStock,users);
            case 4:
                Command commandForFok = new Command(typeCommandVerify(tradingType), commandTypeVerify(commandType), symbol.toUpperCase(), price, amount, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) ,UserName);
                return commands.compareSellOrBuyFok(tradingType, commandForFok, currStock,users);
        }
        return null ;
    }

    @Override
    public StockDto showListOfCommands(String symbol) throws SymbolNotExists {
        Stock stock = stocks.checkSymbolInAllStocks(symbol);
        this.stockDto = new StockDto(stock.getSymbol(), stock.getCompanyName(), stock.getStockPrice(), stock.getNumberOfTransactions(), stock.getTransactionsCycle(), stock.getTransactions(), stock.getCommands());
        updateInfoCycle();
        return this.stockDto;
    }

    public void updateInfoCycle()
    {
        stockDto.getCommands().setTotalCycleSellCommand(calculateCycle(stockDto.getCommands().getCommandsSellList()));
        stockDto.getCommands().setTotalCycleBuyCommand( calculateCycle(stockDto.getCommands().getCommandsBuyList()));
    }

    public int calculateCycle(List<Command> commands)
    {
        int sumOfCycle = 0 ;
        for(Command command : commands)
        {
            sumOfCycle = sumOfCycle + (command.getPrice() * command.getStockAmount());
        }
        return sumOfCycle ;
    }

    public CommandTypes commandTypeVerify(int commandType) {

        switch (commandType) {
            case (1):
                return CommandTypes.LMT;
            case (2):
                return CommandTypes.MKT;
            case(3):
                return CommandTypes.IOC;
            case(4):
                return CommandTypes.FOK;
            default:
                return null;
        }
    }

    public TradingTypes typeCommandVerify(int tradingOrder) {

        switch (tradingOrder) {
            case (1):
                return TradingTypes.BUY;
            case (2):
                return TradingTypes.SELL;
            default:
                return null ;
        }
    }


    public void CheckIfNumTradingOk(int tradingOrder) throws TradingTypeError {

        if (tradingOrder != 1 && tradingOrder != 2) {
            String message = String.format("the current number of trading order: %d is unavailable", tradingOrder);
            throw new TradingTypeError(message);
        }

    }

    public void checkAmount(int amount) throws AmoutIsLowerThenZero {
        if (amount <= 0) {
            String message = String.format("the current amount: %d is lower then zero , please write amount bigger then zero !", amount);
            throw new AmoutIsLowerThenZero(message);
        }
    }

    public void checkPrice(int price) throws AmoutIsLowerThenZero {
        if (price <= 0) {
            String message = String.format("the current price: %d is lower then zero", price);
            throw new AmoutIsLowerThenZero(message);
        }
    }

    public Map<String,Integer> createSetOfStocksAndPrice()
    {
        Map<String,Integer> stockSymbolToPrice = new HashMap<>();
        for(Stock stock : stocks.getAllStocks().values())
        {
            stockSymbolToPrice.put(stock.getSymbol(),stock.getStockPrice());
        }
        return stockSymbolToPrice ;
    }

    public Collection<User> createCollectionOfUsers()
    {
        return users.getUsersList().values();
    }

    public Users getUsers() {
        return users;
    }

    @Override
    public Stocks getStocks() {
        return stocks;
    }

}
