<%--Name: Jennifer Chen--%>
<%--AndrewId: yuc3--%>
<%@ page import="ds.Analytics" %>
<%@ page import="ds.Logs" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: chenyu
  Date: 2022/4/4
  Time: 1:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    ArrayList<Logs> list = new ArrayList<Logs>();
    list = (ArrayList<Logs>) request.getAttribute("logs");
    Analytics ana = (Analytics) request.getAttribute("anas");
%>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Dashboard</title>
</head>
<body>
<div class="container">
    <h1 style="text-align: center;">Dashboard</h1>
    <p style="text-align: center;">This is for CMU 95702 Project4 Task2, by Jennifer Chen(yuc3).</p>
    <hr/>
    <div class="row">
        <div class="col">
            <h4>Top 5 Search Term:</h4>
            <table class="table table-striped table-hover">
                <thead>
                <th>Rank</th>
                <th>Search term</th>
                </thead>
                <% for (int i = 0; i < ana.getTopSearch().length; i++) {%>
                <tr>
                    <td><%=i + 1%>
                    </td>
                    <td><%=ana.getTopSearch()[i]%>
                    </td>
                </tr>
                <%
                    }
                    ;
                %>
            </table>
        </div>
        <div class="col">

            <h4>Top 3 Android Phone Model:</h4>
            <table class="table table-striped table-hover">
                <thead>
                <th>Rank</th>
                <th>Phone Model</th>
                </thead>
                <% for (int i = 0; i < ana.getTopPhone().length; i++) {%>
                <tr>
                    <td><%=i + 1%>
                    </td>
                    <td><%=ana.getTopPhone()[i]%>
                    </td>
                </tr>
                <%
                    }
                    ;
                %>
            </table>
        </div>
    </div>

    <hr/>
    <div class="row">
        <h4>Average user wait time: <%=ana.getMyWebDelayTime()%> milliseconds</h4>
        <h4>Average Owl Api search latency: <%=ana.getOwlDelayTime()%> milliseconds</h4>
    </div>
    <hr/>

    <div class="row">
        <h2 style="text-align: center;">Logs</h2>
        <p style="text-align: center;">Here is a total of <%=list.size()%> logs.</p>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                <th>Search Term</th>
                <th>Phone Model</th>
                <th>Request time</th>
                <th>Sent to Owl</th>
                <th>Back from Owl</th>
                <th>Response time</th>
                <th>Owl's reply</th>
                <th>Reply to app</th>
                </thead>
                <% for (int i = 0; i < list.size(); i++) {%>
                <tr>
                    <td><%=list.get(i).getSearchTerm()%>
                    </td>
                    <td><%=list.get(i).getPhoneModel()%>
                    </td>
                    <td><%=list.get(i).getRequest_from_app()%>
                    </td>
                    <td><%=list.get(i).getSent_to_owlAPI()%>
                    </td>
                    <td><%=list.get(i).getBack_from_owlAPI()%>
                    </td>
                    <td><%=list.get(i).getSent_back_app()%>
                    </td>
                    <td><%=list.get(i).getOwlReply()%>
                    </td>
                    <td><%=list.get(i).getReplyToApp()%>}</td>
                </tr>
                <%
                    }
                    ;
                %>

            </table>
        </div>
    </div>
</div>
</body>
</html>

