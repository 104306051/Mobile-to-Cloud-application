//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

/**
 * Project4 Task2
 * This is the Analytics object class for storing data about Analytics after calculate
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class Analytics {
    private String[] topSearch;
    private String[] topPhone;
    private long owlDelayTime;
    private long myWebDelayTime;
    private int totalLogSize;

    public Analytics(String[] topSearch, String[] topPhone, long owlDelayTime, long myWebDelayTime, int size) {
        this.topSearch = topSearch;
        this.topPhone = topPhone;
        this.owlDelayTime = owlDelayTime;
        this.myWebDelayTime = myWebDelayTime;
        totalLogSize = size;
    }

    public String[] getTopSearch() {
        return topSearch;
    }

    public String[] getTopPhone() {
        return topPhone;
    }

    public long getOwlDelayTime() {
        return owlDelayTime;
    }

    public long getMyWebDelayTime() {
        return myWebDelayTime;
    }

    public int getTotalLogSize() {
        return totalLogSize;
    }
}
