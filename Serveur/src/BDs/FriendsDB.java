package BDs;

import Tools.AuthsTools;
import Tools.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class FriendsDB {

    public static boolean isFriends(String key, int idFriend) throws SQLException {
        boolean ret = false;
        String query = " Select * from Friends where (id_user1=? and id_user2=?)";

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {


            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, idFriend);


            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                ret = true;

        }

        return ret;
    }

    public static boolean deleteFriends(String key, int idFriend) throws SQLException {
        boolean ret = false;
        String query = " Delete from Friends where (id_user1=? and id_user2=?) ";

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {


            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, idFriend);


            if (preparedStmt.executeUpdate() > 0)
                ret = true;

        }

        return ret;
    }

    public static boolean sendFriendRequest(String key, int idFriend) throws SQLException {
        boolean ret = false;
        String query = "insert into FriendsRequest (id_sender, id_receiver, date_request)" + " values (?, ?, ?)";

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {
            Timestamp requestDate = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, idFriend);
            preparedStmt.setTimestamp(3, requestDate);

            if (preparedStmt.executeUpdate() > 0)
                ret = true;

        }

        return ret;
    }

    public static boolean approuveFriends(String key, int user2) throws SQLException {

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             Statement statement = conn.createStatement()) {
            statement.execute("BEGIN");

            String query = " Delete from FriendsRequest where (id_sender=? and id_receiver=?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {


                preparedStmt.setInt(1, user2);
                preparedStmt.setInt(2, userId);

                if (preparedStmt.executeUpdate() == 0) {
                    statement.execute("ROLLBACK");
                    return false;
                }

            } catch (SQLException e) {
                statement.execute("ROLLBACK");
                throw new SQLException("Rollback DeleteRequest");
            }
            query = " insert into Friends (id_user1, id_user2, date_accept)" + " values (?, ?, ?)";

            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {
                Timestamp acceptDate = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
                preparedStmt.setInt(1, user2);
                preparedStmt.setInt(2, userId);
                preparedStmt.setTimestamp(3, acceptDate);

                if (preparedStmt.executeUpdate() == 0) {
                    statement.execute("ROLLBACK");
                    return false;
                }
                statement.execute("COMMIT");
            } catch (SQLException e) {
                statement.execute("ROLLBACK");
                throw new SQLException("Rollback InsertFriend");
            }

        }

        return true;
    }

    public static JSONArray listFollower(String key) throws SQLException, JSONException {
        JSONArray friends = new JSONArray();
        String query = " Select * from Friends f, Users u, UserInfos i where (f.id_user2=?) && f.id_user1=u.id_user && f.id_user1=i.id_user";


        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId != -1) {
            try (Connection conn = Database.getMySQLConnection();
                 PreparedStatement preparedStmt = conn.prepareStatement(query)) {


                preparedStmt.setInt(1, userId);

                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    JSONObject o = new JSONObject();
                    o.put("id_user", rs.getInt("id_user1"));
                    o.put("nom", rs.getString("nom"));
                    o.put("prenom", rs.getString("prenom"));
                    o.put("login", rs.getString("Username"));
                    friends.put(o);
                }

            }

        }
        return friends;

    }

    public static ArrayList<Integer> listFollowingId(String key) throws SQLException {
        ArrayList<Integer> friends = new ArrayList<>();
        String query = " Select * from Friends where (id_user1=?)";


        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId != -1) {
            try (Connection conn = Database.getMySQLConnection();
                 PreparedStatement preparedStmt = conn.prepareStatement(query)) {


                preparedStmt.setInt(1, userId);

                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    int idFriend = rs.getInt("id_user2");
                    friends.add(idFriend);
                }

            }
        }

        return friends;
    }

    public static JSONArray listFollowing(String key) throws SQLException, JSONException {
        JSONArray friends = new JSONArray();
        String query = " Select * from Friends f, Users u, UserInfos i where (f.id_user1=?) && f.id_user2=u.id_user && f.id_user2=i.id_user";


        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId != -1) {
            try (Connection conn = Database.getMySQLConnection();
                 PreparedStatement preparedStmt = conn.prepareStatement(query)) {


                preparedStmt.setInt(1, userId);

                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    JSONObject o = new JSONObject();
                    o.put("id_user", rs.getInt("id_user2"));
                    o.put("nom", rs.getString("nom"));
                    o.put("prenom", rs.getString("prenom"));
                    o.put("login", rs.getString("Username"));
                    friends.put(o);
                }


            }
        }

        return friends;
    }

    public static JSONArray requestList(String key) throws SQLException, JSONException {
        JSONArray requests = new JSONArray();
        String query = " Select * from FriendsRequest f, Users u, UserInfos i where (f.id_receiver=?) && f.id_sender=u.id_user && u.id_user=i.id_user";


        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId != -1) {
            try (Connection conn = Database.getMySQLConnection();
                 PreparedStatement preparedStmt = conn.prepareStatement(query)) {


                preparedStmt.setInt(1, userId);

                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    JSONObject o = new JSONObject();
                    o.put("id_user", rs.getInt("id_sender"));
                    o.put("nom", rs.getString("nom"));
                    o.put("prenom", rs.getString("prenom"));
                    o.put("login", rs.getString("Username"));
                    requests.put(o);
                }


            }
        }

        return requests;
    }

    public static boolean isRequested(String key, int idFriend) throws SQLException {
        String query = " Select * from FriendsRequest where (id_sender=? and id_receiver=?)";
        boolean ret = false;
        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {
            preparedStmt.setInt(1, idFriend);
            preparedStmt.setInt(2, userId);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                ret = true;

        }

        return ret;
    }

    public static boolean requested(String key, int idFriend) throws SQLException {
        boolean ret = false;
        String query = " Select * from FriendsRequest where (id_sender=? and id_receiver=?)";

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {
            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, idFriend);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                ret = true;

        }

        return ret;
    }

    public static boolean disApprouveFriends(String key, int idF) throws SQLException {

        String query = " Delete from FriendsRequest where (id_sender=? and id_receiver=?)";

        int userId = AuthsTools.getUserIdFromKey(key);
        if (userId == -1)
            return false;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {


            preparedStmt.setInt(1, idF);
            preparedStmt.setInt(2, userId);

            if (preparedStmt.executeUpdate() > 0)
                return true;

        }

        return false;
    }

    public static JSONArray searchUsers(String query) throws SQLException, JSONException {
        query = query.toLowerCase();
        String[] words = query.split(" ");
        StringBuilder regex = new StringBuilder();
        regex.append("(");
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals("")) {
                regex.append(words[i]);
                regex.append("|");
            }
        }
        regex.deleteCharAt(regex.lastIndexOf("|"));
        regex.append(")");

        JSONArray array = new JSONArray();
        String request = " select * From Users u ,UserInfos i where (LOWER(u.Username) REGEXP ? and  u.id_user=i.id_user) OR (LOWER(i.nom) REGEXP ? and  u.id_user=i.id_user) OR (LOWER(i.prenom) REGEXP ? and  u.id_user=i.id_user)";

        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(request)) {

            preparedStmt.setString(1, regex.toString());
            preparedStmt.setString(2, regex.toString());
            preparedStmt.setString(3, regex.toString());

            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("nom", rs.getString("nom"));
                o.put("prenom", rs.getString("prenom"));
                o.put("login", rs.getString("Username"));
                o.put("user", rs.getInt("id_user"));
                array.put(o);
            }


        }
        return array;
    }
}
