import Services.AuthsManager;
import Services.FriendsManager;
import Services.MessagesManager;
import Services.UsersManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

class Main {
    static String users[] = new String[3];
    static String ids[] = new String[3];
    static String keys[] = new String[3];

    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        createUsers();
        doLogin();

        testFriends();
        testMessage();


    }


    public static void doLogin() {
        System.out.println("LOGIN DES 3 USERS:");

        try {
            JSONObject json0 = AuthsManager.doLogin(users[0], "testpass", "false");
            keys[0] = json0.getString("key");
            ids[0] = json0.getString("id");
            System.out.println(json0);


            JSONObject json1 = AuthsManager.doLogin(users[1], "testpass", "false");
            keys[1] = json1.getString("key");
            ids[1] = json1.getString("id");
            System.out.println(json1);

            JSONObject json2 = AuthsManager.doLogin(users[2], "testpass", "false");
            keys[2] = json2.getString("key");
            ids[2] = json2.getString("id");
            System.out.println(json2);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void doLogout() {
        System.out.println("LOGOUT DES 3 USERS:");

        System.out.println(AuthsManager.doLogout(keys[0]));
        System.out.println(AuthsManager.doLogout(keys[1]));
        System.out.println(AuthsManager.doLogout(keys[2]));

    }


    public static void testMessage() {
        try {

            System.out.println("Conversation:");
            JSONObject jsonMessage1 = MessagesManager.addMessage(keys[0], "Hello World", null);
            JSONObject jsonComment1 = MessagesManager.addComment(keys[1], "Hi !", jsonMessage1.getString("idMessage"));
            MessagesManager.addComment(keys[0], "thanks for responding !", jsonComment1.getString("idComment"));

            JSONObject Conversation = MessagesManager.getMessage(keys[0], jsonMessage1.getString("idMessage"));
            System.out.println(Conversation);


            System.out.println("Another Message:");
            JSONObject jsonMessage2 = MessagesManager.addMessage(keys[0], "Hello World again", null);
            JSONObject Conversation2 = MessagesManager.getMessage(keys[0], jsonMessage2.getString("idMessage"));
            System.out.println(Conversation2);


            System.out.println("Print all messages User 0");
            System.out.println(MessagesManager.listUserMessages(keys[0], ids[0], "50"));


            System.out.println("Print all friend's message of User 1");
            MessagesManager.listFriendsMessages(keys[1], "20");

            System.out.println("Delete Message 2:");
            MessagesManager.deleteMessage(keys[0], jsonMessage2.getString("idMessage"));

            System.out.println("Print all messages User 0");
            System.out.println(MessagesManager.listUserMessages(keys[0], ids[0], "50"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createUsers() {
        users[0] = "test" + (900 + (int) (Math.random() * ((1000 - 900) + 1)));
        users[1] = "test" + (900 + (int) (Math.random() * ((1000 - 900) + 1)));
        users[2] = "test" + (900 + (int) (Math.random() * ((1000 - 900) + 1)));

        System.out.println("CREATION DE 3 USERS:");
        System.out.println(UsersManager.createUser(users[0], "testpass", users[0] + "@test.com", "nomtest", "prenomtest", "2000-01-01", "true"));
        System.out.println(UsersManager.createUser(users[1], "testpass", users[1] + "@test.com", "nomtest", "prenomtest", "2000-01-01", "true"));
        System.out.println(UsersManager.createUser(users[2], "testpass", users[2] + "@test.com", "nomtest", "prenomtest", "2000-01-01", "true"));

    }


    public static void testFriends() {
        System.out.println("Send Request:");
        System.out.println(FriendsManager.requestFriend(keys[0], ids[1]));
        System.out.println(FriendsManager.requestFriend(keys[0], ids[2]));
        System.out.println(FriendsManager.requestFriend(keys[1], ids[2]));


        System.out.println("Accept:");
        System.out.println(FriendsManager.approuveFriend(keys[1], ids[0]));
        System.out.println(FriendsManager.approuveFriend(keys[2], ids[1]));

        System.out.println("Refuse:");
        System.out.println(FriendsManager.disApprouveFriend(keys[2], ids[0]));

        System.out.println("Delete Friends:");
        System.out.println(FriendsManager.deleteFriend(keys[2], ids[1]));

        System.out.println("List Followers:");
        System.out.println(FriendsManager.listFollower(keys[1]));

        System.out.println("List Following:");
        System.out.println(FriendsManager.listFollowing(keys[0]));
    }

}
