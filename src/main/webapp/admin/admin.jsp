<!DOCTYPE html>
<html>
<head>
    <title>ADMIN</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        .menu {
            list-style-type: none;
            margin: 0;
            padding: 0;
            overflow: hidden;
            background-color: #333;
        }

        .menu > li {
            float: left;
        }

        .menu li a {
            display: block;
            color: white;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
        }

        .menu li a:hover {
            background-color: #111;
        }

        .submenu {
            display: none;
            position: absolute;
            background-color: #f9f9f9;
            min-width: 160px;
            box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
            z-index: 1;
        }

        .submenu li a {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
            text-align: left;
        }

        .submenu li a:hover {
            background-color: #f1f1f1;
        }

        .menu > li:hover .submenu {
            display: block;
        }
    </style>
</head>
<body>
<ul class="menu">
    <li>
        <a href="#">LIST</a>
        <ul class="submenu">
            <li><a href="customerListing.html">List Customer</a></li>
            <li><a href="EmployeeListing.html">List Employee</a></li>
            <li><a href="productListing.html">List Product</a></li>
            <li><a href="ordersListing.html">List Orders</a></li>
            <li><a href="orderDetailsListing.html">List OrderDetails</a></li>
            <li><a href="productImageListing.html">List ProductImage</a></li>
            <li><a href="productPriceListing.html">List ProductPrice</a></li>
        </ul>
    </li>
</ul>
</body>
</html>
