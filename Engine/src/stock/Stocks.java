package stock;

import Exceptions.AmoutIsLowerThenZero;
import Exceptions.CompanyNameIsAlreadyExists;
import Exceptions.SymbolIsAlreadyExists;
import Exceptions.SymbolNotExists;
import command.Commands;
import scheme3.generateClasses.RseStock;
import transaction.Transactions;


import java.util.*;

public class Stocks {
    private Map<String, Stock> allStocks;

    public Stocks() {
        allStocks = new HashMap<>();
    }

    public Stocks(Stocks other) {
        this.allStocks = new HashMap<String, Stock>();
        for (String symbol : other.getAllStocks().keySet()) {
            Stock newStock = new Stock(other.getAllStocks().get(symbol));
            this.allStocks.put(symbol, newStock);
        }
    }

    public Map<String, Stock> getAllStocks() {
        return allStocks;
    }

    public void setAllStocks(Map<String, Stock> stockMap) {
        allStocks = stockMap;
    }

    public Map<String, Stock> createMapOfStocks(List<RseStock> list) throws SymbolIsAlreadyExists, CompanyNameIsAlreadyExists {
        Map<String, Stock> tempMap = new HashMap<>();
        Stock newStock = new Stock();
        for (RseStock currStock : list) {
            if (verifyExistence(currStock.getRseSymbol(), currStock.getRseCompanyName(), tempMap)) {
                tempMap.put(currStock.getRseSymbol().toUpperCase(), newStock.createSingleStock(currStock));
            }
        }
        //this.allStocks = tempMap;
        return tempMap;
    }

    public boolean verifyExistence(String companyName, String symbolName, Map<String, Stock> currMap) throws CompanyNameIsAlreadyExists, SymbolIsAlreadyExists {
        Collection<Stock> values = currMap.values();
        for (Stock currStock : values) {

            if (checkSymbol(currStock.getSymbol(), symbolName) || checkCompanyName(currStock.getCompanyName(), companyName)) {
                //not OK - throws exception
            }
        }
        return true;
    }

    public boolean checkSymbol(String currSymbol, String otherSymbol) throws SymbolIsAlreadyExists {
        if (currSymbol.equalsIgnoreCase(otherSymbol)) {
            String message2 = String.format("the symbol name: %s is already exist in the current XML file", currSymbol);
            throw new SymbolIsAlreadyExists(message2);
        } else
            return false;
    }


    public boolean checkCompanyName(String currCompanyName, String otherCompanyName) throws CompanyNameIsAlreadyExists {
        if (currCompanyName.equalsIgnoreCase(otherCompanyName)) {
            String message1 = String.format("the symbol name: %s is already exist in the current XML file", currCompanyName);
            throw new CompanyNameIsAlreadyExists(message1);
        } else
            return false;
    }

    public Stock checkSymbolInAllStocks(String currSymbol) throws SymbolNotExists {
        for (String symbol : allStocks.keySet()) {
            if (currSymbol.toUpperCase().equals(symbol))
                return allStocks.get(symbol);
        }
        String message = String.format("the symbol name: %s is not exists in the current XML file", currSymbol);
        throw new SymbolNotExists(message);
    }

    public Boolean checkSymbolInStocks(String currSymbol) {
        for (String symbol : allStocks.keySet()) {
            if (currSymbol.toUpperCase().equals(symbol))
                return true;
        }
        return false;
    }

    public Stock findStockByName(String stockName) {
        for (String symbol : allStocks.keySet()) {
            if (allStocks.get(symbol).getCompanyName().equals(stockName))
                return allStocks.get(symbol);
        }
        return null;
    }

    @Override
    public String toString() {
        return "allStocks: \n " + allStocks;
    }

    public void clear() {
        allStocks.clear();
    }

    public void addStocks(Map<String, Stock> currStocks) {
        for (String symbol : currStocks.keySet()) {
            if (!this.allStocks.containsKey(symbol))
                this.allStocks.put(symbol, currStocks.get(symbol));
        }
    }


    public void addNewCompany(String companyName, String symbolName, int issueQuantity, int estimatedValue) throws AmoutIsLowerThenZero, SymbolIsAlreadyExists, CompanyNameIsAlreadyExists {
        if (verifyExistence(companyName, symbolName, this.allStocks)) {
            if (issueQuantity <= 0 || estimatedValue <= 0) {
                String message = "the issue quantity or estimated value is lower then zero ";
                throw new AmoutIsLowerThenZero(message);
            } else
                this.allStocks.put(symbolName,new Stock(symbolName, companyName, estimatedValue / issueQuantity, 0, 0, new Transactions(), new Commands()));
        }
    }

}
