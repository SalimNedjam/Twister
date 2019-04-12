package Services;

import BDs.AuthsDB;
import Tools.AuthsTools;
import Tools.ErrorJSON;
import Tools.UsersTools;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.sql.SQLException;

public class ImageManager {


    public static JSONObject retreiveImage(String key, String idFriend) {
        int idF;
        if (idFriend == null || key == null || key.equals("") || idFriend.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            idF = Integer.parseInt(idFriend);
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            if (!UsersTools.existUser(idF))
                return ErrorJSON.serviceRefused("Profile d'amis inéxistant", 2);

            return new JSONObject().put("data", AuthsDB.retreiveImage(idF));

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject uploadImage(String key, InputStream inputStream) {
        if (inputStream == null || key == null || key.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            if (AuthsDB.uploadImage(key, inputStream))
                return ErrorJSON.serviceAccepted();
            return ErrorJSON.serviceRefused("Erreur lors de l'upload", 3);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

}
