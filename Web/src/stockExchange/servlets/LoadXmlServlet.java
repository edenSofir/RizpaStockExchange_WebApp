package stockExchange.servlets;

import stockExchange.utils.ServletUtils;
import stockExchange.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadXmlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Part part = request.getPart("fake-key-1");
        try {
            ServletUtils.getEngine(request.getServletContext()).readSystemInfoFile(part.getInputStream(), SessionUtils.getUsername(request));
            String message = "the file has been upload successfully";
            response.getOutputStream().println(message);
        }
        catch (Exception e)
        {
            response.setStatus(401);
            response.getOutputStream().println(e.getMessage());
        }
    }


}
