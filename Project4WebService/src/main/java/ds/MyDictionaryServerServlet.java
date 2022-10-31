//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Project4 Task1
 * Some of the code is reference from 95702 Lab2 - Interesting Picture
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
@WebServlet(name = "MyDictionaryServlet", urlPatterns = {"/getWordInfo"})
public class MyDictionaryServerServlet extends HttpServlet {
    MyDictionaryModelUsingWS dm = null; // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        dm = new MyDictionaryModelUsingWS();
    }

    //
    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        // get the search parameter if it exists
        String search = request.getParameter("word");

        // use model to do the search
        Word word = dm.fetchFromOwlApi(search);

        //send response as json format
        String res = word.toString();

        // reference from: https://www.baeldung.com/servlet-json-response
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(res);
        out.flush();

    }
}
