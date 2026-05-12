<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - Demo3</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }

        nav {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        }

        .nav-brand {
            color: white;
            font-size: 24px;
            font-weight: 700;
            text-decoration: none;
        }

        .nav-right {
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .user-info {
            color: white;
            font-size: 14px;
        }

        .nav-link {
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 5px;
            transition: background 0.3s;
        }

        .nav-link:hover {
            background: rgba(255, 255, 255, 0.3);
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
        }

        .page-header {
            text-align: center;
            margin-bottom: 40px;
            color: #333;
        }

        .page-header h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }

        .page-header p {
            color: #666;
            font-size: 16px;
        }

        .products-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }

        .product-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .product-image {
            width: 100%;
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 50px;
        }

        .product-info {
            padding: 20px;
        }

        .product-id {
            color: #999;
            font-size: 12px;
            margin-bottom: 8px;
        }

        .product-name {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin-bottom: 12px;
        }

        .product-price {
            font-size: 24px;
            color: #667eea;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .product-actions {
            display: flex;
            gap: 10px;
        }

        .btn {
            flex: 1;
            padding: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            transition: transform 0.2s;
            text-align: center;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #f0f0f0;
            color: #333;
            border: 1px solid #ddd;
        }

        .btn-secondary:hover {
            background: #e0e0e0;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state h2 {
            font-size: 24px;
            margin-bottom: 10px;
        }

        .footer {
            text-align: center;
            padding: 20px;
            color: #666;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/" class="nav-brand">My project</a>
        <div class="nav-right">
            <%
                Object user = session.getAttribute("loggedInUser");
                if (user != null) {
            %>
                <div class="user-info">
                    Welcome, <strong><%= user %></strong>
                </div>
                <a href="${pageContext.request.contextPath}/cart" class="nav-link">Cart</a>
                <% if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) { %>
                    <a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Admin</a>
                <% } %>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">Logout</a>
                <form method="post" action="${pageContext.request.contextPath}/account/delete" style="margin:0;">
                    <button type="submit" class="nav-link" style="background:transparent;border:none;padding:8px 16px;border-radius:5px;cursor:pointer;">Delete Account</button>
                </form>
            <%
                } else {
            %>
                <a href="${pageContext.request.contextPath}/login.jsp" class="nav-link">Login</a>
            <%
                }
            %>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1>Our Products</h1>
            <p>Browse our collection of high-quality products</p>
        </div>

        <c:if test="${empty data}">
            <div class="empty-state">
                <h2>No Products Available</h2>
                <p>Please check back later</p>
            </div>
        </c:if>

        <c:if test="${not empty data}">
            <div class="products-grid">
                <c:forEach var="product" items="${data}">
                    <div class="product-card">
                        <div class="product-image">📦</div>
                        <div class="product-info">
                            <div class="product-id">ID: ${product.id}</div>
                            <div class="product-name">${product.name}</div>
                            <div class="product-price">$${product.price}</div>
                            <div class="product-actions">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/product?id=${product.id}">View Details</a>
                                <form method="post" action="${pageContext.request.contextPath}/cart/add" style="flex: 1; margin: 0;">
                                    <input type="hidden" name="id" value="${product.id}" />
                                    <button type="submit" class="btn btn-secondary" style="width:100%;">Add to Cart</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>


</body>
</html>