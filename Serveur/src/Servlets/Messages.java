package Servlets;

import Services.MessagesManager;
import Tools.ErrorJSON;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Messages")
public class Messages extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String operation = request.getParameter("op");
        String content = request.getParameter("content");
        String idMessage = request.getParameter("idMessage");
        String inputStream = request.getParameter("photo");


        String key = request.getParameter("key");
        JSONObject json;

        switch (operation) {
            case "comment":
                json = MessagesManager.addComment(key, content, idMessage);
                break;
            case "message":
                json = MessagesManager.addMessage(key, content, inputStream);
                break;
            case "like":
                json = MessagesManager.addLike(key, idMessage);
                break;
            case "dislike":
                json = MessagesManager.removeLike(key, idMessage);
                break;
            case "del":
                json = MessagesManager.deleteMessage(key, idMessage);
                break;
            default:
                json = ErrorJSON.serviceRefused("Undifined Operation", 5);
                break;
        }
        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String operation = request.getParameter("op");
        String limit = request.getParameter("limit");
        String idMessage = request.getParameter("idMessage");
        String key = request.getParameter("key");
        String idUser = request.getParameter("idUser");

        JSONObject json;
        if (operation != null)
            switch (operation) {
                case "list":
                    json = MessagesManager.listUserMessages(key, idUser, limit);

                    break;
                case "friends":
                    json = MessagesManager.listFriendsMessages(key, limit);

                    break;
                case "one":
                    json = MessagesManager.getMessage(key, idMessage);

                    break;
                default:
                    json = ErrorJSON.serviceRefused("Undifined Operation", 5);
                    break;
            }
        else {
            json = ErrorJSON.serviceRefused("Undifined Operation", 5);
        }

        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idMessage = req.getParameter("idMessage");
        String key = req.getParameter("key");

        JSONObject json = MessagesManager.deleteMessage(key, idMessage);
        resp.setContentType(" text / json ");
        PrintWriter out = resp.getWriter();
        out.println(json);

    }
}
