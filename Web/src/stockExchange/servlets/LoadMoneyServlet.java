package stockExchange.servlets;

import com.google.gson.Gson;
import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;
import user.History;
import user.User;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoadMoneyServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request).trim();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        String AmountFromReq = request.getParameter("amountToLoad");
        User user = ServletUtils.getEngine(request.getServletContext()).getUsers().getUsersList().get(usernameFromSession);
        if (AmountFromReq == null || AmountFromReq.isEmpty()) {
            String message = "please insert your amount of money you wish to load first ";
            request.setAttribute("Load_Money_error", message);
            //how to send error message from here
        } else {
            AmountFromReq = AmountFromReq.trim();
            if (Integer.parseInt(AmountFromReq) <= 0) {
                String message = "please try again and load amount of money higher then zero ";
                request.setAttribute("Load_Money_error", message);
            } else {
                synchronized (this) {
                    user.getBankAccount().addNewHistory(3,"",Integer.parseInt(AmountFromReq));
                    user.getBankAccount().setBalance(Integer.parseInt(AmountFromReq));
                    String message = "the money has been loaded successfully";
                    String json = gson.toJson(message);
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
