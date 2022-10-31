//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Project4 Task1
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class MyDictionaryModelUsingWS {

    /**
     * This is the method to fetch data from owl api.
     * Some of the code is reference from: https://stackoverflow.com/questions/12916169/how-to-consume-rest-in-java
     *
     * @param searchTag
     * @return
     * @throws IOException
     */
    public Word fetchFromOwlApi(String searchTag) throws IOException {
        searchTag = URLEncoder.encode(searchTag, "UTF-8");
        StringBuilder res = new StringBuilder();
        try {
            URL url = new URL("https://owlbot.info/api/v4/dictionary/" + searchTag);
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
                res.append(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }

        return parseInfoFromJson(res.toString());
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

}
