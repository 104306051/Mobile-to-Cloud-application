//Name: Jennifer Chen
//AndrewId: yuc3
package ds;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Project4 Task2
 * Some of the code is reference from 95702 Lab2 - Interesting Picture
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
@WebServlet(name = "MyDictionaryServlet", urlPatterns = {"/getWordInfo", "/dashboard"})
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
            throws IOException, ServletException {
        String path = request.getServletPath();
        if (path.equals("/getWordInfo")) { //the path for api
            long requestTime = System.currentTimeMillis();

            // get the search parameter if it exists
            String search = request.getParameter("word");
            System.out.println(search);

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
            long responseTime = System.currentTimeMillis();

            // get the user agent
            String ua = request.getHeader("User-Agent");
            if (ua != null && ua.contains("Android")) {
                // only write log to mongodb with android device
                String phone_model = ua.split("[\\(\\)]")[1].split(";")[2].trim();
                dm.writeLogs(search, res, phone_model, requestTime, responseTime);
            }

        } else if (path.equals("/dashboard")) { //the path for dashboard
            String jsonStringFromDB = dm.readLogs(); //read data from mongo db
            List<Logs> list = dm.showLogs(jsonStringFromDB); //put all logs data in list to display on view
            Analytics ana = dm.calculateAnalytic(); // calculate the analytics data to display on view

            //reference from: https://stackoverflow.com/questions/3751118/output-json-array-in-a-html-tablea-jsp-page
            request.setAttribute("logs", list);
            request.setAttribute("anas", ana);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        }

    }
}
