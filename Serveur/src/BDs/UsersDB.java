package BDs;

import Tools.AuthsTools;
import Tools.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UsersDB {

    public static boolean insertUser(String username, String password, String email, String nom, String prenom,
                                     Date date_naiss, boolean sexe) throws SQLException {

        try (Connection conn = Database.getMySQLConnection();
             Statement statement = conn.createStatement()) {
            statement.execute("BEGIN");
            String query = " insert into Users ( Username, Mail,Password,date_create)" + " values (?, ?, md5(?), ?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {

                Timestamp createDate = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

                preparedStmt.setString(1, username);
                preparedStmt.setString(2, email);
                preparedStmt.setString(3, password);
                preparedStmt.setTimestamp(4, createDate);

                if (preparedStmt.executeUpdate() == 0) {
                    statement.execute("ROLLBACK");
                    return false;
                }

            } catch (SQLException e) {
                statement.execute("ROLLBACK");
                throw new SQLException("Rollback Users");
            }

            query = " select id_user From Users where Username=?";

            int idUser;
            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {

                preparedStmt.setString(1, username);

                ResultSet rs = preparedStmt.executeQuery();
                if (rs.next())
                    idUser = rs.getInt("id_user");
                else {
                    statement.execute("ROLLBACK");
                    return false;
                }

            } catch (SQLException e) {
                statement.execute("ROLLBACK");
                throw new SQLException("Rollback UserId");
            }

            query = " insert into UserInfos (id_user, nom, prenom,date_naiss,sexe)" + " values (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {

                preparedStmt.setInt(1, idUser);
                preparedStmt.setString(2, nom);
                preparedStmt.setString(3, prenom);
                preparedStmt.setDate(4, date_naiss);
                preparedStmt.setBoolean(5, sexe);

                if (preparedStmt.executeUpdate() == 0) {
                    statement.execute("ROLLBACK");
                    return false;
                }
                statement.execute("COMMIT");

            } catch (SQLException e) {
                statement.execute("ROLLBACK");
                throw new SQLException("Rollback UserInfo");
            }
        }

        return true;
    }

    public static boolean existUser(int idUser) throws SQLException {
        String query = " select * From Users where id_user=?";

        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {

            preparedStmt.setInt(1, idUser);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                return true;


        }
        return false;
    }

    public static boolean existEmail(String email) throws SQLException {
        String query = " select * From Users where Mail=?";

        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {

            preparedStmt.setString(1, email);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                return true;


        }
        return false;
    }

    public static boolean existUsername(String username) throws SQLException {
        String query = " select * From Users where Username=?";

        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {

            preparedStmt.setString(1, username);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                return true;


        }
        return false;
    }

    public static int getUserId(String Username) throws SQLException {
        String query = " select id_user From Users where Username=?";

        int userId = -1;
        try (Connection conn = Database.getMySQLConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(query)) {

            preparedStmt.setString(1, Username);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next())
                userId = rs.getInt("id_user");

        }
        return userId;

    }

    public static boolean sameUser(String key, int id) throws SQLException {
        return AuthsTools.getUserIdFromKey(key) == id;
    }
}
