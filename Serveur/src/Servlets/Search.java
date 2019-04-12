package Servlets;

import Services.FriendsManager;
import Services.MessagesManager;
import org.json.JSONArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(name = "Search")
public class Search extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        String query = request.getParameter("query");

        JSONArray json = new JSONArray();
        json.put(MessagesManager.searchMessage(key, query));
        json.put(FriendsManager.searchUsers(key, query));

        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);
    }


}
