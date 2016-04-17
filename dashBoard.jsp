<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  errorPage="" %>
<!DOCTYPE html>
<%
    String tests=request.getParameter("application");
	System.out.println("testParameter:"+tests);
%>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>NetProphet-dashboard</title>

    <link rel="shortcut icon" href="images/favicon.png" type="image/png">
    <link rel="icon" href="images/favicon.png" type="image/png">

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <!-- Custom styles for this template -->
    <link href="css/style.css" rel="stylesheet" type="text/css">


    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
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

    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3 col-sm-4 sidebar" id="scrollbar-side">
          <div>
            <h3 class="overview">
              <a class="selectable" href="#"><p>Overview</p></a><br/>
            </h3>
          </div>
          <hr/>
            <div>
              <div class="url-list pull-left">
                <h3><a>Url List</a></h3>
              </div>
              <div class="sortby-selector pull-right">
                <span class="inline">Sort by</span>
                <select>
                  <option>URL ASC</option>
                  <!-- <option>overallDelay DESC</option> -->
                  <!-- <option>dnsDelay DESC</option> -->
                </select>
              </div>
          </div>
            <div class="clearfix"></div>
            <ul class="nav nav-sidebar"></ul>
          </div>
        </div>
        <div class="col-md-9 col-md-offset-3 col-sm-8 col-sm-offset-4 main">
          <div class="row">
            <div class="col-md-12 col-sm-12 card-title">
              <h2>WIFI</h2>
            </div>
            <div id="wifi-content">
            </div>
            <div class="col-md-12 col-sm-12 card-title">
              <h2>Mobile</h2>
            </div>
            <div id="mobile-content">
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Bootstrap core JavaScript
    ================================================== -->

    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript">
      var application="<%=request.getParameter("application")%>";
    </script>
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/highcharts.js"></script>
    <script src="js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="js/dashboard.js"></script>
    <script type="text/javascript">
      var url = "baidu.com";
      function getSortByContent() {
        var sortByContent="url";
        return sortByContent;
      }
      // var X= $.post("ChartServlet",{data:url}, function(data) {drawLineChart(data);});
    </script>
  </body>
</html>
