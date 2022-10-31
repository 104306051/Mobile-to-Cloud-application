//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

/**
 * Project4 Task2
 * This is the Logs object class for storing data about logs from MongoDB
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class Logs {
    private String searchTerm;
    private String phoneModel;
    private String owlReply;
    private String replyToApp;
    private long request_from_app;
    private long sent_to_owlAPI;
    private long back_from_owlAPI;
    private long sent_back_app;


    /**
     * Constructor for log
     *
     * @param searchTerm
     * @param phoneModel
     * @param owlReply
     * @param replyToApp
     * @param request_from_app
     * @param sent_to_owlAPI
     * @param back_from_owlAPI
     * @param sent_back_app
     */
    public Logs(String searchTerm, String phoneModel, String owlReply, String replyToApp, long request_from_app, long sent_to_owlAPI, long back_from_owlAPI, long sent_back_app) {
        this.searchTerm = searchTerm;
        this.phoneModel = phoneModel;
        this.owlReply = owlReply;
        this.replyToApp = replyToApp;
        this.request_from_app = request_from_app;
        this.sent_to_owlAPI = sent_to_owlAPI;
        this.back_from_owlAPI = back_from_owlAPI;
        this.sent_back_app = sent_back_app;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public long getBack_from_owlAPI() {
        return back_from_owlAPI;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public String getOwlReply() {
        return owlReply;
    }

    public String getReplyToApp() {
        return replyToApp;
    }

    public long getRequest_from_app() {
        return request_from_app;
    }

    public long getSent_to_owlAPI() {
        return sent_to_owlAPI;
    }

    public long getSent_back_app() {
        return sent_back_app;
    }

}
