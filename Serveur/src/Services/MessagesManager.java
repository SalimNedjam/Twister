package Services;

import BDs.AuthsDB;
import BDs.FriendsDB;
import Tools.AuthsTools;
import Tools.ErrorJSON;
import Tools.MessagesTools;
import Tools.UsersTools;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessagesManager {
    public static JSONObject addComment(String key, String comment, String idMessage) {

        if (key == null || comment == null || idMessage == null || key.equals("") || comment.equals("") || idMessage.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            String idComment = MessagesTools.sendComment(key, comment, idMessage);
            if (!idComment.equals("")) {
                JSONObject jsonObject = ErrorJSON.serviceAccepted();
                return jsonObject != null ? jsonObject.put("idComment", idComment) : null;
            }
            return ErrorJSON.serviceRefused("Message Inexistant", 2);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject addMessage(String key, String message, String inputStream) {

        if (key == null || message == null || key.equals("") || message.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            String idMessage = MessagesTools.sendMessage(key, message, inputStream);
            JSONObject jsonObject = ErrorJSON.serviceAccepted();
            return jsonObject != null ? jsonObject.put("idMessage", idMessage) : null;
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 100);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject getMessage(String key, String idMessage) {
        if (idMessage == null || key == null || key.equals("") || idMessage.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);

        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);


            return MessagesTools.getMessage(idMessage);
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject listUserMessages(String key, String idUser, String countLimit) {
        int limit, idU;

        if (countLimit == null || countLimit.equals("")) countLimit = "0";
        if (key == null || idUser == null || key.equals("") || idUser.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);

        try {
            limit = Integer.parseInt(countLimit);
            idU = Integer.parseInt(idUser);

        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);
            if (!UsersTools.existUser(idU))
                return ErrorJSON.serviceRefused("User inéxistant", 2);

            return new JSONObject().put("Array", MessagesTools.getListMessage(idU, limit));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject listFriendsMessages(String key, String countLimit) {
        int limit;

        if (countLimit == null || countLimit.equals("")) countLimit = "0";
        if (key == null || key.equals("")) return ErrorJSON.serviceRefused("Erreur arguments", -1);

        try {
            limit = Integer.parseInt(countLimit);
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);
            ArrayList<Integer> listFriends = FriendsDB.listFollowingId(key);
            listFriends.add(AuthsDB.getUserIdFromKey(key));

            return new JSONObject().put("Array", MessagesTools.getListMessage(listFriends, limit));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject deleteMessage(String key, String idMessage) {
        if (key == null || idMessage == null || key.equals("") || idMessage.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);

        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);
            if(MessagesTools.deleteMessage(key, idMessage))
                return ErrorJSON.serviceAccepted();
            else
                return ErrorJSON.serviceRefused("Message Inexistant", 2);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

    public static JSONObject addLike(String key, String idMessage) {

        if (key == null || idMessage == null || key.equals("") || idMessage.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            if (MessagesTools.addLike(key, idMessage)) {
                return ErrorJSON.serviceAccepted();
            }
            return ErrorJSON.serviceRefused("Message Inexistant", 2);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }


    public static JSONObject removeLike(String key, String idMessage) {

        if (key == null || idMessage == null || key.equals("") || idMessage.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            if (MessagesTools.removeLike(key, idMessage)) {
                return ErrorJSON.serviceAccepted();
            }
            return ErrorJSON.serviceRefused("Message Inexistant", 2);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

    public static JSONObject searchMessage(String key, String query) {
        if (key == null || query == null || key.equals("") || query.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            ArrayList<Integer> listFriends = FriendsDB.listFollowingId(key);
            listFriends.add(AuthsDB.getUserIdFromKey(key));
            return new JSONObject().append("Messages", MessagesTools.searchMessage(query));

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }
}
