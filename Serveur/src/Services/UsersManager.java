package Services;

import Tools.ErrorJSON;
import Tools.UsersTools;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class UsersManager {

    public static JSONObject createUser(String username, String password, String email, String nom, String prenom,
                                        String date, String sexe) {
        if (username == null || password == null || email == null || nom == null || prenom == null || date == null
                || sexe == null || username.equals("") || password.equals("") || email.equals("") || nom.equals("") || prenom.equals("") || date.equals("")
                || sexe.equals(""))
            return ErrorJSON.serviceRefused("Erreur arguments", -1);
        boolean bolSexe;
        Date date_naiss;
        try {
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
            nom = nom.substring(0, 1).toUpperCase() + nom.substring(1).toLowerCase();
            prenom = prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase();

            bolSexe = Boolean.parseBoolean(sexe);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date_naiss = new Date(sdf.parse(date).getTime());
        } catch (Exception e) {
            return ErrorJSON.serviceRefused("Mauvais type d'arguments", -1);
        }
        try {
            if (UsersTools.existUsername(username) || UsersTools.existEmail(email))
                return ErrorJSON.serviceRefused("Utilisateur déja existant", 2);

            if (UsersTools.insertUser(username, password, email, nom, prenom, date_naiss, bolSexe))
                return ErrorJSON.serviceAccepted();
            else
                return ErrorJSON.serviceRefused("Operation impossible ", 15);

        } catch (SQLException e) {
            return ErrorJSON.serviceRefused("SQL ERROR " + e.getMessage(), 1000);
        }
    }

}
