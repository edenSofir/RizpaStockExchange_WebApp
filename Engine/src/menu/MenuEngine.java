package menu;

import Exceptions.*;
import StocksD.StockDto;
import StocksD.StocksDto;
import TransactionD.TransactionsDto;
import command.Enum.TradingTypes;
import stock.Stock;
import stock.Stocks;
import user.User;
import user.Users;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MenuEngine {

    public void readSystemInfoFile(InputStream in , String name) throws SymbolIsAlreadyExists, CompanyNameIsAlreadyExists, FileNotFoundException, JAXBException, UserNameIsAlreadyExists, SymbolNotExists;

    public StockDto showCurrStock(String symbol) throws SymbolNotExists;

    public TransactionsDto tradingExecution(int tradingType, String symbol, int amount, int price, int commandType , String UserName) throws SymbolNotExists, AmoutIsLowerThenZero, CommandTypeError, TradingTypeError;

    public StockDto showListOfCommands(String symbol) throws SymbolNotExists;

    public Map<String,Integer> createSetOfStocksAndPrice();

    public Collection<User> createCollectionOfUsers();

    public void checkAmount(int amount) throws AmoutIsLowerThenZero ;

    public Users getUsers();

    public Stocks getStocks();

}
