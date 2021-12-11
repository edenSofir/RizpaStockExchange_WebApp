package stockExchange.servlets;

import com.google.gson.Gson;
import command.Commands;
import stock.Stock;
import stock.Stocks;
import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;
import transaction.Transactions;
import user.Item;
import user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateNewCompanyServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String usernameFromSession = SessionUtils.getUsername(request).trim();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        String CompanyName = request.getParameter("companyName");
        String SymbolName = request.getParameter("symbolName");
        String IssueQuantity = request.getParameter("IssueQuantity");
        String EstimatedValue = request.getParameter("EstimatedValue");

        User user = ServletUtils.getEngine(request.getServletContext()).getUsers().getUsersList().get(usernameFromSession);
        Stocks stocks = ServletUtils.getEngine(request.getServletContext()).getStocks();
        if (CompanyName == null || CompanyName.isEmpty()) {
            String message = "Please enter the company name in order to perform the desired command";
            request.setAttribute("Load_Money_error", message);
            //how to send error message from here
        }
        if (SymbolName == null || SymbolName.isEmpty()) {
            String message = "Please enter the symbol name in order to perform the desired command";
            request.setAttribute("Load_Money_error", message);
            //how to send error message from here
        }
        if (IssueQuantity == null || IssueQuantity.isEmpty()) {
            String message = "Please enter the issue quantity in order to perform the desired command";
            request.setAttribute("Load_Money_error", message);
        }
        if (EstimatedValue == null || EstimatedValue.isEmpty()) {
            String message = "Please enter the estimated Value company in order to perform the desired command";
            request.setAttribute("Load_Money_error", message);
        } else {
            CompanyName = CompanyName.trim();
            SymbolName = SymbolName.trim();
            IssueQuantity = IssueQuantity.trim();
            EstimatedValue = EstimatedValue.trim();
            synchronized (this) {
                try {
                    stocks.addNewCompany(CompanyName, SymbolName, Integer.parseInt(IssueQuantity), Integer.parseInt(EstimatedValue));
                    user.addNewItem(SymbolName,Integer.parseInt(IssueQuantity));
                    String message = "the Company has been added successfully and added to your holdings";
                    String json = gson.toJson(message);
                    out.println(json);
                    out.flush();
                }
                catch (Exception e) {
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
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
