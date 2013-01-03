<!DOCTYPE html>
<%@tag description="Bootstrap Hero Template" pageEncoding="UTF-8"%>
<%@attribute name="title"%>
<%@attribute name="body" fragment="true" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="/StoreDemo/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="/StoreDemo/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

   </head>
  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="/StoreDemo/store/catalog/">Pink Demo Store</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li><a href="/StoreDemo/store/checkout/cart">Cart</a></li>
              <li><a href="#contact">Contact</a></li>
            </ul>
            <form class="navbar-form pull-right">
              <input class="span2" type="text" placeholder="Email">
              <input class="span2" type="password" placeholder="Password">
              <button type="submit" class="btn">Sign in</button>
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container">

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="hero-unit">
        <h1>Welcome!</h1>
        <p>This is a simple online retail store created with the Pink framework.</p>
        <p><a class="btn btn-primary btn-large" href="http://mobiarch.wordpress.com/pink">Learn more &raquo;</a></p>
      </div>

      <!-- Example row of columns -->
      <jsp:invoke fragment="body"/>

      <hr>

      <footer>
        <p>&copy; Company 2012</p>
      </footer>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/StoreDemo/bootstrap/js/jquery.js"></script>
    <script src="/StoreDemo/bootstrap/js/bootstrap.js"></script>
  </body>
</html>
