package stockExchange.servlets;

import UserDto.UsersDto;
import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final String SIGN_UP_URL = "../Web_Web2"; //need to check if the location is ok
    private final String LOGIN_ERROR_URL = "/pages/loginError/login_after_error.jsp";
    private final String USERS_AND_STOCKS_ADMIN = "pages/secondPage/usersAndStocksAdmin.html";
    private final String USERS_AND_STOCKS_TRADER = "pages/secondPage/usersAndStocksTrader.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        Users users = ServletUtils.getEngine(request.getServletContext()).getUsers();
        String roleUserFromParameter =  request.getParameter("Trader&Admin");
        if(usernameFromSession == null)
        {
            String userNameFromParameter = request.getParameter("username");
            if(userNameFromParameter == null || userNameFromParameter.isEmpty())
            {
                response.sendRedirect(SIGN_UP_URL);
            }
            else
            {
                userNameFromParameter = userNameFromParameter.trim() ;
                synchronized (this)
                {
                    if(users.isUserExists(userNameFromParameter))
                    {
                        String errorMessage = "username " + userNameFromParameter + " already exists. Please enter a different username.";
                        request.setAttribute("user_error",errorMessage);
                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request,response);

                    }
                    else if(users.isRolAdminTaken() && roleUserFromParameter.equalsIgnoreCase("Admin"))
                    {
                        String errorMessage = "the role " +  roleUserFromParameter + " is already taken please chose a different role";
                        request.setAttribute("user_error",errorMessage);
                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request,response);
                    }
                    else
                    {
                        users.addUser(userNameFromParameter,roleUserFromParameter);
                        request.getSession(true).setAttribute("username",userNameFromParameter); //check with aviad if eol is need in session
                        if(roleUserFromParameter.equals("Admin"))
                            response.sendRedirect(USERS_AND_STOCKS_ADMIN);
                        else
                            response.sendRedirect(USERS_AND_STOCKS_TRADER);
                    }
                }
            }

        }
        else
        {
            if(roleUserFromParameter.equals("Admin"))
                response.sendRedirect(USERS_AND_STOCKS_ADMIN);
            else
                response.sendRedirect(USERS_AND_STOCKS_TRADER);
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

