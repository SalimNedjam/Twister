package Tools;

import BDs.MessagesDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class MessagesTools {
    public static JSONObject getMessage(String idMess) throws JSONException, SQLException {
        return MessagesDB.getMessageById(idMess);
    }

    public static JSONArray getListMessage(int idUser, int countLimit) throws SQLException, JSONException {
        return MessagesDB.getAllMessages(idUser, countLimit);

    }

    public static JSONArray getListMessage(int countLimit) throws SQLException, JSONException {
        return MessagesDB.getAllMessages(countLimit);

    }

    public static JSONArray getListMessage(List<Integer> idUsers, int countLimit) throws SQLException, JSONException {
        return MessagesDB.getAllMessages(idUsers, countLimit);

    }


    public static String sendMessage(String key, String message, String inputStream) throws SQLException {
        String res= MessagesDB.sendMessage(key, message, inputStream);
        MessagesDB.createIndex();
        return res;
    }

    public static boolean deleteMessage(String key, String idMess) throws SQLException {
        boolean res=MessagesDB.removeMessage(key, idMess);
        MessagesDB.createIndex();
        return res;

    }

    public static String sendComment(String key, String comment, String idMessage) throws SQLException {
        return MessagesDB.sendComment(key, comment, idMessage);

    }

    public static boolean addLike(String key, String idMessage) throws SQLException {
        return MessagesDB.addLike(key, idMessage);

    }

    public static boolean removeLike(String key, String idMessage) throws SQLException {
        return MessagesDB.removeLike(key, idMessage);

    }

    public static JSONArray searchMessage(String query) throws JSONException, SQLException {
        return MessagesDB.searchMessages(query);

    }
}
