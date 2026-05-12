<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f5f5f5; margin:0; }
        nav { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 15px 30px; display:flex; justify-content:space-between; align-items:center; }
        .nav-brand { color:white; font-size:24px; font-weight:700; text-decoration:none; }
        .nav-link { color:white; text-decoration:none; margin-left:15px; }
        .container { max-width: 900px; margin: 30px auto; padding: 0 20px; }
        .card { background:white; border-radius:10px; padding:25px; box-shadow: 0 10px 30px rgba(0,0,0,0.12); }
        table { width:100%; border-collapse: collapse; }
        th, td { text-align:left; padding: 10px; border-bottom:1px solid #eee; }
        th { color:#555; }
        .price { font-weight:700; color:#667eea; }
        .total { font-size: 18px; font-weight: 800; margin-top: 15px; text-align:right; }
        .btn { display:inline-block; padding:12px 18px; border-radius:6px; border: none; cursor:pointer; text-decoration:none; font-weight:600; }
        .btn-secondary { background:#f0f0f0; border:1px solid #ddd; color:#333; }
    </style>
</head>
<body>
<nav>
    <a class="nav-brand" href="${pageContext.request.contextPath}/">My project</a>
    <div>
        <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
    </div>
</nav>

<div class="container">
    <div class="card">
        <h1>Your Cart</h1>
        <p style="color:#666; margin-top:6px;">Items in cart: <strong><c:out value="${empty cart ? 0 : cart.size()}"/></strong></p>

        <c:if test="${empty cart}">
            <p>Your cart is empty.</p>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/products">Back to Products</a>
        </c:if>

        <c:if test="${not empty cart}">
            <table>
                <thead>
                <tr>
                    <th>#</th>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="total" value="0" />
                <c:set var="idx" value="0" />
                <c:forEach var="p" items="${cart}">
                    <c:set var="idx" value="${idx + 1}" />
                    <tr>
                        <td><c:out value="${idx}"/></td>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td class="price">$${p.price}</td>
                    </tr>
                    <c:set var="total" value="${total + p.price}" />
                </c:forEach>
                </tbody>
            </table>
            <div class="total">Total: $<c:out value="${total}" /></div>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/products">Continue Shopping</a>
        </c:if>
    </div>
</div>
</body>
</html>

