package user;

import command.Enum.TradingTypes;
import scheme3.generateClasses.RseHoldings;
import scheme3.generateClasses.RseItem;
import stock.Stock;
import stock.Stocks;

import java.util.LinkedList;
import java.util.List;

public class Holdings {

    private List<Item> userHoldings ;
    private Stocks allStocks ;

    public Holdings() {
        userHoldings = new LinkedList<>();
    }


    public Holdings(RseHoldings rseHoldings , Stocks stocks) {
        userHoldings = new LinkedList<>();
        allStocks = stocks ;
        createUserHoldings(rseHoldings.getRseItem());
    }

    public Boolean checkIfItemExsict(String symbol, int quantity)
    {
        for(Item item : this.userHoldings)
        {
            if(item.getStockSymbol().equalsIgnoreCase(symbol)) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        return false;
    }
    public Item findItemByStockSymbol(String symbol) {
        for(Item item : this.userHoldings)
        {
            if(item.getStockSymbol().equalsIgnoreCase(symbol))
            {
                return item;
            }
        }
        return null ;
    }



    public void createUserHoldings (List<RseItem> itemListUser)
    {
        for(RseItem currItem : itemListUser)
        {
            if(!checkIfItemExsict(currItem.getSymbol(),currItem.getQuantity()))
                userHoldings.add(new Item(currItem.getSymbol(),currItem.getQuantity()));
        }
    }


    public List<Item> getUserHoldings() {
        return userHoldings;
    }

    public int calTotalValHoldings()  {
        int TotalVal = 0 ;
        try {
            for (Item currItem : userHoldings) {
                Stock stock = allStocks.checkSymbolInAllStocks(currItem.getStockSymbol());
                TotalVal = TotalVal + (stock.getStockPrice()*currItem.getQuantity());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return TotalVal ;
    }

    public void updateHoldings(TradingTypes tradingType, String symbolName , int quantity)
    {
        Boolean flag = false ;

        for(Item item : userHoldings)
        {
            if(item.getStockSymbol().equals(symbolName))
            {
                if(tradingType == TradingTypes.BUY) {
                    item.setQuantity(item.getQuantity() + quantity);
                    flag = true;
                }
                else
                {
                    if(item.getQuantity() - quantity == 0 )
                        userHoldings.remove(item);
                    else
                        item.setQuantity(item.getQuantity() - quantity);
                    flag = true ;
                }
            }
        }
        if(!flag) //the stock dose not exists in the list the user must buy - add to the holdings
        {
            userHoldings.add(new Item(symbolName, quantity));
        }

    }

}
