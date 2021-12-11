<%--
  Created by IntelliJ IDEA.
  User: Eden Sofir
  Date: 6/30/2021
  Time: 12:14 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<%@ page import="stockExchange.utils.SessionUtils" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Rizpa Stock Exchange</title>
    <link rel="stylesheet" href="pages/assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=ABeeZee&amp;display=swap">
    <link rel="stylesheet" href="pages/assets/fonts/fontawesome-all.min.css">
    <link rel="stylesheet" href="pages/assets/fonts/ionicons.min.css">
    <link rel="stylesheet" href="pages/assets/css/Login-Form-Dark.css">
    <link rel="stylesheet" href="pages/assets/css/Social-Icons.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../../common/bootstrap.min.css">
    <script type="javascript" src="../../common/contextPathHelper.js"></script>
    <script src="../../common/jquery-3.6.0.min.js"></script>
    <script src="../../pages/loginPage/login.js"></script>
</head>
<body>
<div class="container">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% String usernameFromParameter = request.getParameter("username") != null ? request.getParameter("username") : "";%>
    <% if (usernameFromSession == null) {%>
    <br/>
    <div id="wrapper">
        <div class="d-flex flex-column" id="content-wrapper"></div>
        <a class="border rounded d-inline scroll-to-top" href="#page-top"><i class="fas fa-angle-up"></i></a>
    </div>
    <section class="login-dark">
        <form method="GET" action="login" id="loginForm">
            <h2 class="visually-hidden">Login Form</h2>
            <div class="illustration"><i class="icon ion-ios-locked-outline"></i></div>
            <span style="font-family: ABeeZee, sans-serif;">please insert your user name :</span>
            <input class="form-control loginUser" name="username" type="text"
                   style="border-color: rgb(255,255,255);margin: 10px;">
            <div class="form-check">
                <input class="form-check-input role" name="Trader&Admin" type="radio" id="Admin" value="Admin">
                <label class="form-check-label" for="Admin" style="font-family: ABeeZee, sans-serif;">Admin</label>
            </div>
            <div class="form-check">
                <input class="form-check-input role" type="radio" id="Trader" name="Trader&Admin" value="Trader"
                       checked="checked">
                <label class="form-check-label" for="Trader" style="font-family: ABeeZee, sans-serif;">Trader</label>
            </div>
            <div class="mb-3"></div>
            <div class="mb-3"></div>
            <div class="mb-3">
                <input value="Login" class="btn btn-primary d-block w-100" type="submit"
                       style="font-family: ABeeZee, sans-serif;border-top-left-radius: 50px;border-top-right-radius: 50px;border-bottom-right-radius: 50px;border-bottom-left-radius: 50px;">
            </div>
        </form>
        <% Object errorMessage = request.getAttribute("user_error");%>
        <% if (errorMessage != null) {%>
        <span class="bg-danger" style="color: white; font-size: 26px; background-color: transparent"><%=errorMessage%></span>
        <% } %>
        <% } else {%>
        <h1>Welcome back, <%=usernameFromSession%>
        </h1>
        <a href="../../pages/secondPage/usersAndStocks.html">Click here to start your trading process/a>
            <!--//need to change !!! not relvent to us -->
            <br/>
            <a href="login?logout=true" id="logout">logout</a>
                <% }%>
            <script src="pages/assets/bootstrap/js/bootstrap.min.js"></script>
            <script src="pages/assets/js/theme.js"></script>
    </section>
</div>

