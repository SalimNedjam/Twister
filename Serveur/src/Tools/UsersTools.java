package Tools;

import BDs.UsersDB;

import java.sql.Date;
import java.sql.SQLException;

public class UsersTools {
    public static boolean insertUser(String user, String password, String email, String nom, String prenom, Date date,
                                     boolean sexe) throws SQLException {
        return UsersDB.insertUser(user, password, email, nom, prenom, date, sexe);
    }

    public static boolean existUser(int idFriend) throws SQLException {
        return UsersDB.existUser(idFriend);
    }

    public static boolean existUsername(String user) throws SQLException {
        return UsersDB.existUsername(user);
    }

    public static boolean existEmail(String email) throws SQLException {
        return UsersDB.existEmail(email);
    }

    public static boolean sameUser(String key, int id) throws SQLException {
        return UsersDB.sameUser(key, id);
    }


}
