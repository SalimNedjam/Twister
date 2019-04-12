package Services;

import Tools.AuthsTools;
import Tools.ErrorJSON;
import Tools.FriendsTools;
import Tools.UsersTools;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class FriendsManager {

    public static JSONObject requestFriend(String key, String idFriend) {
        int idF;
        if (idFriend == null || key == null || key.equals("") || idFriend.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            idF = Integer.parseInt(idFriend);
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", 1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 2);

            if (!UsersTools.existUser(idF))
                return ErrorJSON.serviceRefused("Profile d'amis inéxistant", 3);
            if (UsersTools.sameUser(key, idF))
                return ErrorJSON.serviceRefused("Vous ne pouvez pas vous ajouter vous meme", 4);
            if (FriendsTools.areFriend(key, idF))
                return ErrorJSON.serviceRefused("Déja Amis", 5);
            /*if (FriendsTools.isRequested(key, idF))
                return ErrorJSON.serviceRefused("Vous avez deja recu une invitation de cette personne", 6);*/
            if (FriendsTools.requested(key, idF))
                return ErrorJSON.serviceRefused("Vous avez deja envoyé une invitation a cette personne", 7);
            if (FriendsTools.sendFriendRequest(key, idF))
                return ErrorJSON.serviceAccepted();

            return ErrorJSON.serviceRefused("Invatation impossible.", 8);
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }

    }

    public static JSONObject deleteFriend(String key, String idFriend) {
        int idF;
        if (idFriend == null || key == null || key.equals("") || idFriend.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments " + key + "  " + idFriend, -1);


        try {
            idF = Integer.parseInt(idFriend);
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 2);

            if (!UsersTools.existUser(idF))
                return ErrorJSON.serviceRefused("Profile d'amis inéxistant", 3);
            if (UsersTools.sameUser(key, idF))
                return ErrorJSON.serviceRefused("Vous ne pouvez pas vous supprimé vous meme", 8);
            if (!FriendsTools.areFriend(key, idF))
                return ErrorJSON.serviceRefused("Pas amis", 9);
            if (FriendsTools.deleteFriends(key, idF))
                return ErrorJSON.serviceAccepted();

            return ErrorJSON.serviceRefused("Supression impossible", 15);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

    public static JSONObject approuveFriend(String key, String idFriend) {
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
           /* if (FriendsTools.areFriend(key, idF))
                return ErrorJSON.serviceRefused("Déja Amis", 5);*/
            if (!FriendsTools.isRequested(key, idF))
                return ErrorJSON.serviceRefused("Vous n'avez pas quoi accepté", 11);

            if (FriendsTools.addFriends(key, idF))
                return ErrorJSON.serviceAccepted();

            return ErrorJSON.serviceRefused("Ajout impossible.", 15);
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

    public static JSONObject disApprouveFriend(String key, String idFriend) {
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
                return ErrorJSON.serviceRefused("Profile inéxistant", 2);
            if (!FriendsTools.isRequested(key, idF))
                return ErrorJSON.serviceRefused("Vous n'avez pas quoi refusé", 12);

            if (FriendsTools.disApprouveFriend(key, idF))
                return ErrorJSON.serviceAccepted();

            return ErrorJSON.serviceRefused("Operation impossible.", 15);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

    public static JSONObject listFollower(String key) {
        if (key == null || key.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            return new JSONObject().put("Array", FriendsTools.getFollower(key));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR" + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR" + e.getMessage(), 100);
        }
    }

    public static JSONObject isFriend(String key, String idFriend) {
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

            return new JSONObject().put("isFriend", FriendsTools.areFriend(key, idF)).put("isRequested", FriendsTools.requested(key, idF));

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR " + e.getMessage(), 100);
        }
    }

    public static JSONObject listFollowing(String key) {
        if (key == null || key.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            return new JSONObject().put("Array", FriendsTools.getFollowing(key));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR" + e.getMessage(), 100);
        }
    }

    public static JSONObject listRequest(String key) {
        if (key == null || key.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            return new JSONObject().put("Array", FriendsTools.requestList(key));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR" + e.getMessage(), 100);
        }
    }

    public static JSONObject searchUsers(String key, String query) {
        if (key == null || key.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        try {
            if (!AuthsTools.isKeyValid(key))
                return ErrorJSON.serviceRefused("Clé invalide", 1);

            return new JSONObject().put("Users", FriendsTools.searchUsers(query));
        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        } catch (JSONException e) {
            return ErrorJSON.serviceRefused("JSON ERROR" + e.getMessage(), 100);
        }
    }
}
