<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Products</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f5f5f5; margin:0; }
        nav { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 15px 30px; display:flex; justify-content:space-between; align-items:center; }
        .nav-brand { color:white; font-size:24px; font-weight:700; text-decoration:none; }
        .nav-link { color:white; text-decoration:none; margin-left:15px; }
        .container { max-width: 1000px; margin: 30px auto; padding: 0 20px; }
        .card { background:white; border-radius:10px; padding:25px; box-shadow: 0 10px 30px rgba(0,0,0,0.12); }
        table { width:100%; border-collapse: collapse; margin-top: 12px; }
        th, td { text-align:left; padding: 10px; border-bottom:1px solid #eee; }
        th { color:#555; }
        .btn { display:inline-block; padding:10px 14px; border-radius:6px; border: none; cursor:pointer; text-decoration:none; font-weight:600; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color:white; }
        .btn-danger { background:#e74c3c; color:white; }
        .input { width:100%; padding:10px; border:1px solid #ddd; border-radius:6px; }
        .row { display:flex; gap:12px; }
        .col { flex:1; }
        .alert { padding:10px 12px; border-radius:6px; margin: 12px 0; }
        .alert-error { background:#fadbd8; border-left:4px solid #e74c3c; color:#8e2820; }
        .alert-success { background:#d5f4e6; border-left:4px solid #27ae60; color:#1b6b3d; }
    </style>
</head>
<body>
<nav>
    <a class="nav-brand" href="${pageContext.request.contextPath}/"> Admin</a>
    <div>
        <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</nav>

<div class="container">
    <div class="card">
        <h1>Admin - Product Management</h1>

        <%
            String error = request.getParameter("error");
            String message = request.getParameter("message");
        %>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        <% if (message != null && !message.isEmpty()) { %>
            <div class="alert alert-success"><%= message %></div>
        <% } %>

        <h2 style="margin-top:18px;">Add Product</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/products" style="margin-top:10px;">
            <input type="hidden" name="action" value="add" />
            <div class="row">
                <div class="col">
                    <label>Name</label>
                    <input class="input" type="text" name="name" required minlength="2" maxlength="100" />
                </div>
                <div class="col">
                    <label>Price</label>
                    <input class="input" type="number" step="0.01" name="price" required min="0" />
                </div>
                <div class="col" style="align-self:end;">
                    <button class="btn btn-primary" type="submit">Add</button>
                </div>
            </div>
        </form>

        <h2 style="margin-top:22px;">All Products</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${products}">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>$${p.price}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/products" style="margin:0; display:inline;">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="id" value="${p.id}" />
                            <button class="btn btn-danger" type="submit" onclick="return confirm('Delete product #' + ${p.id} + '?');">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <p style="color:#666; margin-top:12px;">
            Note: Admin access.
        </p>
    </div>
</div>
</body>
</html>

