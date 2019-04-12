package BDs;

import Tools.AuthsTools;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static Tools.Database.getMongoCollection;

public class MessagesDB {

    public static JSONObject getMessageById(String idMessage) throws JSONException, SQLException {
        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document request = new Document("_id", new ObjectId(idMessage));
        MongoCursor<Document> messageCursor = messageDB.find(request).iterator();

        JSONObject json = new JSONObject();
        JSONObject author = new JSONObject();

        if (messageCursor.hasNext())
        {
            Document document = messageCursor.next();
            author.put("user_id", document.get("user_id"));
            String login = AuthsTools.getLoginFromId(Integer.parseInt(document.get("user_id").toString()));
            author.put("login", login);
            JSONObject user = AuthsTools.getUserInfosFromLogin(login);
            author.put("nom", user.getString("nom"));
            author.put("prenom", user.getString("prenom"));
            json.put("author", author);
            json.put("message_id", document.get("_id"));
            json.put("message", document.get("message"));
            json.put("date", document.get("date"));
            json.put("likes", document.get("likes"));

            JSONArray comments = new JSONArray();
            ArrayList<ObjectId> commentList = (ArrayList<ObjectId>) document.get("comments");
            if (commentList != null) {
                for (ObjectId comment : commentList) {
                    comments.put(getMessageById(comment.toString()));
                }
            }
            json.put("comments", comments);

        }
        return json;
    }


    public static JSONArray getAllMessages(int idUser, int limitCount) throws JSONException, SQLException {
        MongoCollection<Document> messageDb = getMongoCollection("Messages");
        Document query = new Document();
        ArrayList<Document> list = new ArrayList<>();
        list.add(new Document().append("user_id", idUser));
        list.add(new Document().append("is_message", true));
        query.put("$and", list);
        MongoCursor<Document> messageCursor = messageDb.find(query).sort(new Document("date", -1)).limit(limitCount).iterator();
        return cursorFormat(messageCursor);

    }

    public static JSONArray getAllMessages(List<Integer> idUsers, int limitCount) throws JSONException, SQLException {
        MongoCollection<Document> messageDb = getMongoCollection("Messages");
        Document query = new Document();
        ArrayList<Document> list = new ArrayList<>();
        list.add(new Document().append("user_id", new Document().append("$in", idUsers)));
        list.add(new Document().append("is_message", true));
        query.put("$and", list);
        MongoCursor<Document> messageCursor = messageDb.find(query).sort(new Document("date", -1)).limit(limitCount).iterator();
        return cursorFormat(messageCursor);

    }

    public static JSONArray getAllMessages(int limitCount) throws JSONException, SQLException {
        MongoCollection<Document> messageDb = getMongoCollection("Messages");
        Document query = new Document();
        ArrayList<Document> list = new ArrayList<>();
        list.add(new Document().append("is_message", true));
        query.put("$and", list);
        MongoCursor<Document> messageCursor = messageDb.find(query).sort(new Document("date", -1)).limit(limitCount).iterator();
        return cursorFormat(messageCursor);
    }

    private static JSONArray cursorFormat(MongoCursor<Document> messageCursor) throws JSONException, SQLException {
        JSONArray userMessages = new JSONArray();
        while (messageCursor.hasNext()) {
            JSONObject jsonObject = new JSONObject();
            JSONObject author = new JSONObject();
            Document document = messageCursor.next();


            author.put("user_id", document.get("user_id"));
            String login = AuthsTools.getLoginFromId(Integer.parseInt(document.get("user_id").toString()));
            author.put("login", login);
            JSONObject user = AuthsTools.getUserInfosFromLogin(login);
            author.put("nom", user.getString("nom"));
            author.put("prenom", user.getString("prenom"));

            jsonObject.put("author", author);
            jsonObject.put("message_id", document.get("_id"));
            jsonObject.put("message", document.get("message"));
            jsonObject.put("date", document.get("date"));
            jsonObject.put("likes", document.get("likes"));

            if (document.get("picture") != null)
                jsonObject.put("picture", document.get("picture"));

            JSONArray comments = new JSONArray();
            ArrayList<ObjectId> commentIds = (ArrayList<ObjectId>) document.get("comments");
            if (commentIds != null) {
                for (ObjectId one_comment : commentIds) {
                    comments.put(getMessageById(one_comment.toString()));
                }
            }
            jsonObject.put("comments", comments);

            userMessages.put(jsonObject);
        }
        return userMessages;
    }

    public static String sendMessage(String key, String message, String inputStream) throws SQLException {
        int idUser = AuthsDB.getUserIdFromKey(key);
        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document newMessage = new Document();
        newMessage.put("user_id", idUser);
        newMessage.put("message", message);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        GregorianCalendar c = new GregorianCalendar();
        if (inputStream != null) {
            newMessage.put("picture", inputStream);
        }
        newMessage.put("date", nowAsISO);
        newMessage.put("is_message", true);
        newMessage.put("comments", new ArrayList<ObjectId>());
        newMessage.put("likes", new ArrayList<ObjectId>());

        messageDB.insertOne(newMessage);

        ObjectId idMessage = (ObjectId) newMessage.get("_id");

        return idMessage.toString();
    }


    public static boolean removeMessage(String key, String id_message) throws SQLException {
        int idUser = AuthsDB.getUserIdFromKey(key);
        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document query = new Document("_id", new ObjectId(id_message));
        MongoCursor<Document> messageCursor = messageDB.find(query).iterator();
        if (messageCursor.hasNext()) {
            Document message = messageCursor.next();

            if (idUser == Integer.parseInt(message.get("user_id").toString())) {
                return removeMessage(id_message);
            }

        }
        return false ;
    }


    public static boolean removeMessage(String id_message) {

        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document query = new Document("_id", new ObjectId(id_message));
        MongoCursor<Document> messageCursor = messageDB.find(query).iterator();

        if (messageCursor.hasNext()) {
            Document message = messageCursor.next();

            Document parentQuery = new Document("comments", new Document("$elemMatch", new Document("$eq", new ObjectId(id_message))));
            MongoCursor<Document> parentCursor = messageDB.find(query).iterator();

            if (parentCursor.hasNext()) {
                messageDB.updateOne(parentQuery, new Document("$pull", new Document("comments", new ObjectId(id_message))));
            }

            ArrayList<ObjectId> commentList = (ArrayList<ObjectId>) message.get("comments");
            if (commentList != null) {
                for (ObjectId comment : commentList) {
                    removeMessage(comment.toString());

                }
            }
            messageDB.deleteOne(query);
            return true;
        }
        return false;
    }

    public static String sendComment(String key, String message, String idMessage) throws SQLException {
        int idUser = AuthsDB.getUserIdFromKey(key);

        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document query = new Document("_id", new ObjectId(idMessage));
        MongoCursor<Document> messageCursor = messageDB.find(query).iterator();

        if (messageCursor.hasNext()) {
            Document document = messageCursor.next();
            GregorianCalendar calendar = new GregorianCalendar();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            Document newMessage = new Document();
            newMessage.put("user_id", idUser);
            newMessage.put("message", message);
            newMessage.put("date", nowAsISO);
            newMessage.put("is_message", false);
            newMessage.put("comments", new ArrayList<ObjectId>());
            newMessage.put("likes", new ArrayList<ObjectId>());


            messageDB.insertOne(newMessage);

            ObjectId idComment = (ObjectId) newMessage.get("_id");

            ArrayList<ObjectId> comments = (ArrayList<ObjectId>) document.get("comments");
            comments.add(idComment);

            messageDB.updateOne(query, new Document("$set", new Document("comments", comments)));

            return idComment.toString();
        }

        return "";
    }

    public static boolean addLike(String key, String idMessage) throws SQLException {
        int idUser = AuthsDB.getUserIdFromKey(key);

        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document query = new Document("_id", new ObjectId(idMessage));
        MongoCursor<Document> messageCursor = messageDB.find(query).iterator();

        if (messageCursor.hasNext()) {
            Document document = messageCursor.next();


            ArrayList<Integer> likes = (ArrayList<Integer>) document.get("likes");
            if (!likes.contains(idUser)) {
                likes.add(idUser);
                messageDB.updateOne(query, new Document("$set", new Document("likes", likes)));
                return true;
            }
        }

        return false;
    }

    public static boolean removeLike(String key, String idMessage) throws SQLException {
        int idUser = AuthsDB.getUserIdFromKey(key);

        MongoCollection<Document> messageDB = getMongoCollection("Messages");
        Document query = new Document("_id", new ObjectId(idMessage));
        MongoCursor<Document> messageCursor = messageDB.find(query).iterator();

        if (messageCursor.hasNext()) {
            Document document = messageCursor.next();


            ArrayList<Integer> likes = (ArrayList<Integer>) document.get("likes");
            if (likes.contains(idUser)) {
                likes.remove((Integer) idUser);
                messageDB.updateOne(query, new Document("$set", new Document("likes", likes)));
                return true;
            }
        }

        return false;
    }

    public static double getNumberOfWord(String id_doc) {
        double wordCount = 0;
        MongoCollection<Document> messageDb = getMongoCollection("Messages");
        Document req = new Document("_id", new ObjectId(id_doc));
        MongoCursor<Document> messageCursor = messageDb.find(req).iterator();
        if (messageCursor.hasNext()) {
            Document document = messageCursor.next();
            String message = (String) document.get("message");
            String[] words = message.split(" ");
            for (int i = 0; i < words.length; i++)
                if (!words[i].equals(""))
                    wordCount++;
        }
        return wordCount;
    }


    public static JSONArray searchMessages(String query) throws JSONException, SQLException, NumberFormatException {
        query = query.toLowerCase();

        String normalized = Normalizer.normalize(query, Normalizer.Form.NFD);
        String accentRemoved = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String[] words = accentRemoved.split(" ");
        HashSet<String> hashSet = new HashSet(Arrays.asList(words));

        JSONArray jsonArray = new JSONArray();
        double docsCount;
        MongoCollection<Document> messageDb = getMongoCollection("Messages");
        docsCount = messageDb.countDocuments();
        HashMap<String, Double> hashMap = new HashMap<>();


        for (String word : hashSet) {
            if (!word.equals("")) {
                MongoCollection<Document> idfsDB = getMongoCollection("Idfs");
                Document req = new Document("_id", word);
                MongoCursor<Document> messageCursor = idfsDB.find(req).iterator();
                if (messageCursor.hasNext()) {
                    Document document = messageCursor.next();
                    Document value = (Document) document.get("value");
                    ArrayList<Document> array = (ArrayList<Document>) value.get("array");
                    for (Document d : array) {
                        String id_doc;
                        Object id_object = d.get("id_doc");
                        if (id_object instanceof ObjectId)
                            id_doc = ((ObjectId) id_object).toHexString();
                        else
                            id_doc = (String) id_object;


                        double countTermInDoc = (double) d.get("oc");
                        double countWordInDoc = getNumberOfWord(id_doc);
                        double docsInvolvedCount = array.size();

                        double TF = countTermInDoc / countWordInDoc;
                        double IDF = Math.log(docsCount / docsInvolvedCount);

                        if (!hashMap.containsKey(id_doc))
                            hashMap.put(id_doc, TF * IDF);
                        else
                            hashMap.put(id_doc, hashMap.get(id_doc) + TF * IDF);

                    }

                }
            }
        }
        Map<String, Double> sortedByValue = hashMap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        for (String _id : sortedByValue.keySet()) {
            jsonArray.put(getMessageById(_id));
        }

        return jsonArray;

    }

    public static void createIndex() {

        String mapIdf = "function() {\n" +
                "    if (!this.is_message) return;\n" +
                "   var r=this.message.toLowerCase();\n" +
                "   r = r.replace(new RegExp(\"[àáâãäå]\", 'g'),\"a\");\n" +
                "   r = r.replace(new RegExp(\"æ\", 'g'),\"ae\");\n" +
                "   r = r.replace(new RegExp(\"ç\", 'g'),\"c\");\n" +
                "   r = r.replace(new RegExp(\"[èéêë]\", 'g'),\"e\");\n" +
                "   r = r.replace(new RegExp(\"[ìíîï]\", 'g'),\"i\");\n" +
                "   r = r.replace(new RegExp(\"ñ\", 'g'),\"n\");                            \n" +
                "   r = r.replace(new RegExp(\"[òóôõö]\", 'g'),\"o\");\n" +
                "   r = r.replace(new RegExp(\"œ\", 'g'),\"oe\");\n" +
                "   r = r.replace(new RegExp(\"[ùúûü]\", 'g'),\"u\");\n" +
                "   r = r.replace(new RegExp(\"[ýÿ]\", 'g'),\"y\");\n"+
                "    var words=r.match(/\\w{4,}/g)\n" +
                "    for(var i=0;i<words.length;i++) {\n" +
                "        var array=[]\n" +
                "        array.push({id_doc:this._id,oc:1})\n" +
                "        emit(words[i], {array:array})\n" +
                "\n" +
                "    }\n" +
                "}";

        String reduceIdf = "function(key,values) {\n" +
                "    var occurences = {};\n" +
                "    var array=[]\n" +
                "    for (var i = 0; i < values.length; i++) {\n" +
                "        if (occurences[values[i].array[0].id_doc] == undefined) {\n" +
                "            occurences[values[i].array[0].id_doc]=1;\n" +
                "\n" +
                "        } else {\n" +
                "            occurences[values[i].array[0].id_doc]+=1;\n" +
                "        }\n" +
                "    }\n" +
                "    for (var val in occurences) array.push({id_doc:val.toString().substr(10,val.toString().length-10-2),oc:occurences[val]});\n" +
                "    return {array:array};\n" +
                "\n" +
                "}";

        String mapTf = "function() {\n" +
                "    if (!this.is_message) return;\n" +
                "var r=this.message.toLowerCase();\n" +
                "r = r.replace(new RegExp(\"[àáâãäå]\", 'g'),\"a\");\n" +
                "r = r.replace(new RegExp(\"æ\", 'g'),\"ae\");\n" +
                "r = r.replace(new RegExp(\"ç\", 'g'),\"c\");\n" +
                "r = r.replace(new RegExp(\"[èéêë]\", 'g'),\"e\");\n" +
                "r = r.replace(new RegExp(\"[ìíîï]\", 'g'),\"i\");\n" +
                "r = r.replace(new RegExp(\"ñ\", 'g'),\"n\");                            \n" +
                "r = r.replace(new RegExp(\"[òóôõö]\", 'g'),\"o\");\n" +
                "r = r.replace(new RegExp(\"œ\", 'g'),\"oe\");\n" +
                "r = r.replace(new RegExp(\"[ùúûü]\", 'g'),\"u\");\n" +
                "r = r.replace(new RegExp(\"[ýÿ]\", 'g'),\"y\");\n"+
                "    var words=r.match(/\\w{4,}/g)\n" +
                "\n" +
                "    for(var i=0;i<words.length;i++) {\n" +
                "        var array=[]\n" +
                "        array.push({mot:words[i],oc:1})\n" +
                "        emit(this._id, {array:array})\n" +
                "\n" +
                "    }\n" +
                "}";
        String reduceTf ="function(key,values) {\n" +
                "\n" +
                "    var occurences = {};\n" +
                "    var array=[]\n" +
                "    for (var i = 0; i < values.length; i++) {\n" +
                "        if (occurences[values[i].array[0].mot] == undefined) {\n" +
                "            occurences[values[i].array[0].mot]=1;\n" +
                "        } else {\n" +
                "            occurences[values[i].array[0].mot]+=1;\n" +
                "        }\n" +
                "    }\n" +
                "    for (var val in occurences) array.push({mot:val.toString(),oc:occurences[val]});\n" +
                "\n" +
                "    return {array:array};\n" +
                "\n" +
                "}";

        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
        DB db = mongoClient.getDB("Nedjam_AitGhezali");
        DBCollection collection = db.getCollection("Messages");
        collection.mapReduce(mapTf, reduceTf, "Tfs", MapReduceCommand.OutputType.REPLACE, null);
        collection.mapReduce(mapIdf, reduceIdf, "Idfs", MapReduceCommand.OutputType.REPLACE, null);

    }
}
