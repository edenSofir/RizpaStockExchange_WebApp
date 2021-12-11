package stockExchange.servlets;

import Exceptions.NewCommandEx;
import TransactionD.TransactionsDto;
import com.google.gson.Gson;
import command.Command;
import command.Commands;
import command.Enum.CommandTypes;
import command.Enum.TradingTypes;
import stock.Stock;
import stock.Stocks;
import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;
import user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateNewCommandServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String usernameFromSession = SessionUtils.getUsername(request).trim();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        String tradingType = request.getParameter("tradingType");
        String commandType = request.getParameter("commandType");
        String stockQuantity = request.getParameter("stockQuantityCommand");
        String stockPrice = request.getParameter("newStockPrice");
        User user = ServletUtils.getEngine(request.getServletContext()).getUsers().getUsersList().get(usernameFromSession);
        Stock stock = (Stock) request.getSession().getAttribute("stock");
        Commands commands = stock.getCommands();
        int price = Integer.parseInt(stockPrice);
        int amount = Integer.parseInt(stockQuantity);

        if (tradingType == null || tradingType.isEmpty()) {
            String message = "Please enter the trading type in order to perform the desired command";
            request.setAttribute("Create_Command_Error", message);
            //how to send error message from here
        }
        if (commandType == null || commandType.isEmpty()) {
            String message = "Please enter the command type in order to perform the desired command";
            request.setAttribute("Create_Command_Error", message);
            //how to send error message from here
        }
        if (stockPrice.isEmpty()) {
            String message = "Please enter the stock price company in order to perform the desired command";
            request.setAttribute("Create_Command_Error", message);
        } else {
            synchronized (this) {
                int currentTradingTypes = 1; //BUY;
                int currentCommandType = 1; //(LMT);
                String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                try {
                    if (tradingType.equalsIgnoreCase("BUY")) {
                        currentTradingTypes = 1;
                    }
                    if (tradingType.equalsIgnoreCase("SELL")) {
                        currentTradingTypes = 2;
                    }
                    if (commandType.equalsIgnoreCase("MKT"))
                        currentCommandType = 2;
                    if (commandType.equalsIgnoreCase("LMT"))
                        currentCommandType = 1;
                    if (commandType.equalsIgnoreCase("IOC"))
                        currentCommandType = 3;
                    if (commandType.equalsIgnoreCase("FOK"))
                        currentCommandType = 4;

                    if (currentTradingTypes == 1) {
                        if (price * amount > user.getBankAccount().getBalance()) {
                            String message = "this command denied! your balance is too low";
                            throw new NewCommandEx(message);
                        }
                    }
                    if (currentTradingTypes == 2) {
                        if (amount > user.getHoldings().findItemByStockSymbol(stock.getSymbol()).getQuantity()) {
                            String message = "The stock quantity you wish to sell is more then the quantity you have. Please try again.";
                            throw new NewCommandEx(message);
                        }
                    }
                    if(stockQuantity.isEmpty() && currentCommandType != 2 ) {
                        String message = "Please enter the quantity of stocks in order to perform the desired command";
                        request.setAttribute("Create_Command_Error", message);
                    }
                    TransactionsDto transactionsDto = ServletUtils.getEngine(request.getServletContext()).tradingExecution(currentTradingTypes, stock.getSymbol(), amount, price, currentCommandType, user.getNameOfUser());
                    String message = "Great , your command has been added successfully and added to your relevant command." +
                            "details: " +'\n' +
                            "Trading type is" + currentTradingTypes +","+ '\n' +                            "Command type is" + currentCommandType + ' ' +
                            "Stock symbol: " + stock.getSymbol() +","+ '\n' +
                            "Stock quantity: " + amount +"," + '\n' +
                            "Date: " + date + '\n' +
                            "Command initiator: " + usernameFromSession + ".";
                    String messagePartially ="Unfortunately, your order was not registered in the system because no counter-order was found that matched your order. \n" ;
                    String json;
                    if(transactionsDto.getTransactionList().size() == 0 && (commandType.equalsIgnoreCase("IOC")||commandType.equalsIgnoreCase("FOK")))
                        json = gson.toJson(messagePartially);
                    else
                        json = gson.toJson(message);
                    out.println(json);
                    out.flush();

                } catch (Exception e) {
                    String json = gson.toJson(e.getMessage());
                    out.println(json);
                    out.flush();
                }
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet responsde
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>

}
