<%@page import="me.xcdu.dto.AccessManager"%>
<%@page import="java.util.ArrayList"%>
<%
  ArrayList<String> applicationList = new AccessManager().getApplicationsList();
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  errorPage="" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="shortcut icon" href="images/favicon.png" type="image/png">
    <link rel="icon" href="images/favicon.png" type="image/png">

    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <link href="css/style.css" rel="stylesheet" type="text/css">

    <title>Applications</title>
  </head>
  <body style="background-color: #FFF">
    <nav class="navbar navbar-defualt navbar-fixed-top navbar-header">
      <div class="container-fluid">
        <div class="row">
          <a class="col-md-2 col-md-offset-1 navbar-brand" href="#">NetProphet</a>
           <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
              <li><a href="#">Help</a></li>
            </ul>
          </div>
        </div>
      </div>
    </nav>

    <div class="container" id="app-container">
      <div class="row">
        <div class="col-md-4 col-md-offset-4">
          <label for="app-table">Please select the application:</label>
        </div>
        <div class="col-md-4"></div>
        <div class="col-md-4 col-md-offset-4">
          <table id="app-table"class="table table-hover">
            <tbody>
              <%
              for(int i=0;i<applicationList.size();++i){
              %>
              <tr>
                <td><%=applicationList.get(i)%></td>
              </tr>
               <%}%>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/applications.js"></script>
  </body>
</html>
