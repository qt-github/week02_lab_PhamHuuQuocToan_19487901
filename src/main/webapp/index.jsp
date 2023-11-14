<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f0f0f0;
      margin: 0;
      padding: 0;
    }

    h1 {
      color: #333;
      text-align: center;
      padding: 50px 0;
      font-size: 2.5em;
    }

    a {
      display: block;
      color: #fff;
      text-decoration: none;
      padding: 20px;
      margin: 10px auto;
      width: 200px;
      text-align: center;
      background-color: #333;
      border-radius: 5px;
      transition: background-color 0.3s ease;
    }

    a:hover {
      background-color: #111;
    }

    .container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: calc(100vh - 100px);
    }
  </style>
</head>
<body>
<h1><%= "WEEK-02-LAB" %></h1>
<div class="container">
  <a href="admin/admin.jsp">ADMIN</a>
  <a href="customer/productList.html">CUSTOMER</a>
</div>
</body>
</html>