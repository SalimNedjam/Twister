package Servlets;

import Services.FriendsManager;
import Tools.ErrorJSON;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Friends")
public class Friends extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String operation = request.getParameter("op");
        String idFriend = request.getParameter("id");
        String key = request.getParameter("key");
        JSONObject json;
        if (operation != null)
            switch (operation) {
                case "req":
                    json = FriendsManager.requestFriend(key, idFriend);
                    break;
                case "app":
                    json = FriendsManager.approuveFriend(key, idFriend);
                    break;
                case "dis":
                    json = FriendsManager.disApprouveFriend(key, idFriend);
                    break;
                case "requested":
                    json = FriendsManager.isFriend(key, idFriend);
                    break;
                case "del":
                    json = FriendsManager.deleteFriend(key, idFriend);
                    break;
                default:
                    json = ErrorJSON.serviceRefused("Undefined Operation", 5);
                    break;
            }
        else
            json = ErrorJSON.serviceRefused("Undefined Operation", 5);

        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        String operation = request.getParameter("op");

        JSONObject json;
        if (operation != null)
            switch (operation) {
                case "follower":
                    json = FriendsManager.listFollower(key);
                    break;
                case "following":
                    json = FriendsManager.listFollowing(key);
                    break;
                case "requests":
                    json = FriendsManager.listRequest(key);
                    break;
                default:
                    json = ErrorJSON.serviceRefused("Undefined Operation", 5);
                    break;
            }
        else
            json = ErrorJSON.serviceRefused("Undefined Operation", 5);

        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFriend = req.getParameter("id");
        String key = req.getParameter("key");

        JSONObject json = FriendsManager.deleteFriend(key, idFriend);
        resp.setContentType(" text / json ");
        PrintWriter out = resp.getWriter();
        out.println(json);

    }
}
