//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Project4 Task2
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class MyDictionaryModelUsingWS {
    private List<Logs> totalLogs = new ArrayList<>();
    private String owlApiResponse;
    private long requestTime;
    private long responseTime;

    /**
     * This is the method for fetching data from OWL api through http request
     * Reference some of the code from :https://stackoverflow.com/questions/12916169/how-to-consume-rest-in-java
     *
     * @param searchTag
     * @return
     * @throws IOException
     */
    public Word fetchFromOwlApi(String searchTag) throws IOException {
        searchTag = URLEncoder.encode(searchTag, "UTF-8");
        StringBuilder owlResponse = new StringBuilder();
        long requestApiTime = 0;
        long responseApiTime = 0;

        try {
            requestApiTime = System.currentTimeMillis();
            URL url = new URL("https://owlbot.info/api/v4/dictionary/" + searchTag);
            //System.out.println(url.toString()); //for debug
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String API_KEY = "31a7c5aa604e239c4a89bd90a889be765d5d93df";
            String basicAuth = "Token " + API_KEY;
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                owlResponse.append(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        responseApiTime = System.currentTimeMillis();

        owlApiResponse = owlResponse.toString();
        requestTime = requestApiTime;
        responseTime = responseApiTime;

        return parseInfoFromJson(owlResponse.toString());
    }

    /**
     * This is the method to parse the info I want from the owl api.
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    public Word parseInfoFromJson(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);
        String def = root.get("definitions").get(0).get("definition").asText();
        String picUrl = root.get("definitions").get(0).get("image_url").asText();
        return new Word(def, picUrl);
    }

    /**
     * Insert data to Mongodb.
     * Reference from: https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/
     *
     * @param search
     * @param response
     * @param model
     * @param reqFromApp
     * @param responseToApp
     */
    public void writeLogs(String search, String response, String model, long reqFromApp, long responseToApp) {
        //String uri = "mongodb+srv://j4ennifer22:sally248@ds-project4.3jcys.mongodb.net/dashboard_db?retryWrites=true&w=majority";
        String uri = "mongodb://j4ennifer22:sally248@ds-project4-shard-00-02.3jcys.mongodb.net:27017,ds-project4-shard-00-01.3jcys.mongodb.net:27017,ds-project4-shard-00-00.3jcys.mongodb.net:27017/dashboard_db?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("dashboard_db");
            MongoCollection<Document> collection = database.getCollection("logs");
            try {
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("search_term", search) //the request from the mobile phone
                        .append("owl_reply", Document.parse(owlApiResponse.toString())) //information about the request and reply to the 3rd party API
                        .append("reply_to_app", Document.parse(response)) //information about the reply to the mobile phone
                        .append("phone_model", model) // phone model
                        .append("timestamp_request_from_app", reqFromApp)//when requests are received
                        .append("timestamp_sent_to_owlAPI", requestTime) //when requests sent to the 3rd party API
                        .append("timestamp_back_from_owlAPI", responseTime)
                        .append("timestamp_sent_back_app", responseToApp)// when the data sent in the reply back to the phone
                );
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }

    /**
     * Read data from Mongodb.
     * Reference from: https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/
     * Also use Jackson library to construct json array/
     * Reference from: https://www.javatips.net/api/com.fasterxml.jackson.databind.node.arraynode
     *
     * @return
     */
    public String readLogs() {
        String res = null;
        //String uri = "mongodb+srv://j4ennifer22:sally248@ds-project4.3jcys.mongodb.net/dashboard_db?retryWrites=true&w=majority";
        String uri = "mongodb://j4ennifer22:sally248@ds-project4-shard-00-02.3jcys.mongodb.net:27017,ds-project4-shard-00-01.3jcys.mongodb.net:27017,ds-project4-shard-00-00.3jcys.mongodb.net:27017/dashboard_db?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("dashboard_db");
            MongoCollection<Document> collection = database.getCollection("logs");
            MongoCursor<Document> cursor = collection.find().cursor();

            //use Jackson library to put all the data in a json array
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = mapper.createArrayNode();
            while (cursor.hasNext()) {
                String jsonString = cursor.next().toJson();
                JsonNode node = mapper.readTree(jsonString);
                arrayNode.add(node);
            }
            res = mapper.writeValueAsString(arrayNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;

    }

    /**
     * Put the logs data to Logs object for easy display to view
     *
     * @param logs
     * @return
     * @throws IOException
     */
    public List<Logs> showLogs(String logs) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(logs);
        int size = root.size();
        List<Logs> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String searchTerm = root.get(i).get("search_term").asText();
            String phone = root.get(i).get("phone_model").asText();
            String owl = root.get(i).get("owl_reply").toString();
            String reply = root.get(i).get("reply_to_app").toString();
            long time1 = root.get(i).get("timestamp_request_from_app").asLong();
            long time2 = root.get(i).get("timestamp_sent_to_owlAPI").asLong();
            long time3 = root.get(i).get("timestamp_back_from_owlAPI").asLong();
            long time4 = root.get(i).get("timestamp_sent_back_app").asLong();
            Logs log = new Logs(searchTerm, phone, owl, reply, time1, time2, time3, time4);
            list.add(log);
        }
        totalLogs = list;

        return list;
    }

    /**
     * Calculate the analytic data.
     *
     * @return
     */
    public Analytics calculateAnalytic() {
        Map<String, Integer> searchTermMap = new HashMap<>();
        Map<String, Integer> phoneMap = new HashMap<>();
        long appDelaySum = 0;
        long owlDelaySum = 0;

        for (Logs log : totalLogs) {
            searchTermMap.put(log.getSearchTerm(), searchTermMap.getOrDefault(log.getSearchTerm(), 0) + 1);
            phoneMap.put(log.getPhoneModel(), phoneMap.getOrDefault(log.getPhoneModel(), 0) + 1);
            appDelaySum += (log.getSent_back_app() - log.getRequest_from_app());
            owlDelaySum += (log.getBack_from_owlAPI() - log.getSent_to_owlAPI());
        }

        String[] searchTop = getTopSearchTerm(searchTermMap); //get top 5
        String[] phoneTop = getTopPhone(phoneMap); //get top 3

        long owlDelayTime = owlDelaySum / totalLogs.size();
        long myWebDelayTime = appDelaySum / totalLogs.size();

        return new Analytics(searchTop, phoneTop, owlDelayTime, myWebDelayTime, totalLogs.size());

    }

    /**
     * Get the top search term and put it into list.
     *
     * @param searchTermMap
     * @return
     */
    public String[] getTopSearchTerm(Map<String, Integer> searchTermMap) {
        PriorityQueue<String> searchPQ = new PriorityQueue<>(Comparator.comparingInt(searchTermMap::get));
        for (String s : searchTermMap.keySet()) {
            searchPQ.add(s);
            if (searchPQ.size() > 5) {
                searchPQ.poll();
            }
        }
        String[] topSearchTerm = new String[5];
        int searchIndex = topSearchTerm.length - 1;
        while (!searchPQ.isEmpty()) {
            topSearchTerm[searchIndex--] = searchPQ.poll();
        }

        return topSearchTerm;
    }

    /**
     * Get the top phone model and put it into list.
     *
     * @param phoneMap
     * @return
     */
    public String[] getTopPhone(Map<String, Integer> phoneMap) {
        PriorityQueue<String> phonePQ = new PriorityQueue<>(Comparator.comparingInt(phoneMap::get));
        for (String s : phoneMap.keySet()) {
            phonePQ.add(s);
            if (phonePQ.size() > 3) {
                phonePQ.poll();
            }
        }

        String[] topPhone = new String[3];
        int phoneIndex = topPhone.length - 1;
        while (!phonePQ.isEmpty()) {
            topPhone[phoneIndex--] = phonePQ.poll();
        }

        return topPhone;
    }

}
