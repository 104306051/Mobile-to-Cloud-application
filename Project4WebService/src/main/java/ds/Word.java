//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Project4 Task1
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class Word {
    String definition;
    String picUrl;

    public Word(String def, String pic) {
        definition = def;
        picUrl = pic;
    }

    public String getDefinition() {
        return definition;
    }

    public String getPicUrl() {
        return picUrl;
    }

    /**
     * Using Jackson library to turn this class to json String
     *
     * @return json String
     */
    @Override
    public String toString() {
        String res = null;
        ObjectMapper Obj = new ObjectMapper();
        try {
            res = Obj.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

}
