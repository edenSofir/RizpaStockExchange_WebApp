package user;

import Exceptions.SymbolNotExists;
import Exceptions.UserNameIsAlreadyExists;
import scheme3.generateClasses.RseItem;
import scheme3.generateClasses.RseUser;
import stock.Stock;
import stock.Stocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {

   private Map<String,User> usersList ;

    public Users() {
        usersList = new HashMap<>();
    }

    public Map<String,User> createUsersList(List<RseUser> rseUsers , Stocks stocks) throws UserNameIsAlreadyExists, SymbolNotExists {
        Map<String,User>  tempMap = new HashMap<>();
        for(RseUser currUser : rseUsers)
        {
            if(checkUserName(currUser.getName(),tempMap)) //check if the current user is already exist in the XML file
            {
                if(checkUserHoldings(currUser.getRseHoldings().getRseItem() , stocks , currUser.getName())) {
                    tempMap.put(currUser.getName(), new User(currUser , stocks));
                }
            }
        }
        return tempMap ;
    }

    public User findUserByUserName(String userName){
        for(User user : usersList.values())
        {
            if(user.getNameOfUser().equalsIgnoreCase( userName)){
                return user ;
            }
        }
        return null ;
    }


    public Boolean checkUserHoldings(List<RseItem> ListRseItem ,Stocks stocks ,String userName ) throws SymbolNotExists {
        for( RseItem currItem : ListRseItem)
        {
            if(!stocks.checkSymbolInStocks(currItem.getSymbol())) {
                String message = String.format("sorry " + userName + "! the symbol name: %s is not exist in our stocks", currItem.getSymbol());
                throw new SymbolNotExists(message);
            }
        }
        return true ; //all user symbols exist in our stocks
    }

    public Boolean checkUserName(String userName,Map<String,User>  tempMap) throws UserNameIsAlreadyExists {
        for (String currUserName : tempMap.keySet()) {
            if (userName.equalsIgnoreCase(currUserName))
            {
                String message = String.format("the user name: %s is already exist in the current XML file", currUserName);
                throw new UserNameIsAlreadyExists(message);
            }
        }
        return true;
    }


    public Map<String, User> getUsersList() {
        return usersList;
    }

    public User symbol2User(String symbolName)
    {
        return usersList.get(symbolName);
    }

    public void setUsersList(Map<String, User> usersList) {
        this.usersList = usersList;
    }

    public synchronized Map<String, User> getUserFinal() { return Collections.unmodifiableMap(usersList); }

    public void clear() {
        this.usersList.clear();
    }


    public synchronized void addUser(String username ,String role) {
        User user = new User(username,role);
        usersList.put(username,user);
    }

    public synchronized void removeUser(String username) {
        usersList.remove(username);
    }

    public boolean isRolAdminTaken() {
        for (User user : usersList.values()) {
            if(user.getRole().equalsIgnoreCase("Admin"))
            {
                return true;
            }
        }
        return false ;
    }



    public boolean isUserExists(String username) {
        return usersList.containsKey(username);
    }

    public Boolean checkHoldings(Map<String, Stock> tempStocks , List<RseItem> rseHoldings) throws SymbolNotExists {
        for(RseItem currItem : rseHoldings)
        {
            if(tempStocks.get(currItem.getSymbol())==null) {
                String message = String.format("the symbol name: %s is not exists in the current XML.\n please upload different XML file", currItem.getSymbol());
                throw new SymbolNotExists(message);
            }

        }
        return true;
    }
}
