package Tools;

import BDs.FriendsDB;
import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;

public class FriendsTools {
    public static boolean areFriend(String key, int idFriend) throws SQLException {
        return FriendsDB.isFriends(key, idFriend);
    }

    public static boolean isRequested(String key, int idFriend) throws SQLException {
        return FriendsDB.isRequested(key, idFriend);
    }

    public static JSONArray requestList(String key) throws SQLException, JSONException {
        return FriendsDB.requestList(key);
    }


    public static boolean requested(String key, int idFriend) throws SQLException {
        return FriendsDB.requested(key, idFriend);
    }

    public static boolean deleteFriends(String key, int idF) throws SQLException {
        return FriendsDB.deleteFriends(key, idF);
    }

    public static boolean sendFriendRequest(String key, int idFriend) throws SQLException {
        return FriendsDB.sendFriendRequest(key, idFriend);
    }

    public static boolean addFriends(String key, int idF) throws SQLException {
        return FriendsDB.approuveFriends(key, idF);
    }

    public static JSONArray getFollowing(String key) throws SQLException, JSONException {
        return FriendsDB.listFollowing(key);
    }

    public static JSONArray getFollower(String key) throws SQLException, JSONException {
        return FriendsDB.listFollower(key);
    }

    public static boolean disApprouveFriend(String key, int idF) throws SQLException {
        return FriendsDB.disApprouveFriends(key, idF);
    }

    public static JSONArray searchUsers(String query) throws SQLException, JSONException {
        return FriendsDB.searchUsers(query);
    }
}
