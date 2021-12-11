package stockExchange.servlets;

import com.google.gson.Gson;
import stock.Stock;
import stock.Stocks;
import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;
import user.User;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

public class StockDataServlet extends HttpServlet {
    private final String STOCK_DATA_AND_TRADE_TRADER = "../thirdPage/stockDataAndTradeTrader.html";
    private final String STOCK_DATA_AND_TRADE_ADMIN = "../thirdPage/stockDataAndTradeAdmin.html";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        //response.sendRedirect(STOCK_DATA_AND_TRADE);
        try(PrintWriter out = response.getWriter()) {
            Stocks stocks = ServletUtils.getEngine(request.getServletContext()).getStocks();
            String name = request.getParameter("singleStock");
            Stock stock = stocks.findStockByName(name);
            request.getSession().setAttribute("stock", stock);
            String userNameFromSession = SessionUtils.getUsername(request).trim();
            User user = ServletUtils.getEngine(request.getServletContext()).getUsers().getUsersList().get(userNameFromSession);
            String role = user.getRole().toUpperCase();
            if(role.equals("ADMIN"))
                response.sendRedirect(STOCK_DATA_AND_TRADE_ADMIN);
            else
                response.sendRedirect(STOCK_DATA_AND_TRADE_TRADER);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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