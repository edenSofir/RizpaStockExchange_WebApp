package stockExchange.utils;

import StocksD.StocksDto;
import UserDto.UsersDto;
import chat.ChatManager;
import menu.MenuEngineImpl;
import user.Users;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    private static final Object engineMangerLock = new Object();
    private static final Object chatManagerLock = new Object();

    public static MenuEngineImpl getEngine(ServletContext servletContext) {
        synchronized (engineMangerLock)
        {
            if(servletContext.getAttribute("Engine")==null)
            {
                servletContext.setAttribute("Engine" , new MenuEngineImpl());
            }
        }
        return (MenuEngineImpl) servletContext.getAttribute("Engine");
    }


    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute("chatManager") == null) {
                servletContext.setAttribute("chatManager", new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute("chatManager");
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return Integer.MIN_VALUE;
    }
}
