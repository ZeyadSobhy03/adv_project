<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f5f5f5; margin:0; }
        nav { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 15px 30px; display:flex; justify-content:space-between; align-items:center; }
        .nav-brand { color:white; font-size:24px; font-weight:700; text-decoration:none; }
        .nav-link { color:white; text-decoration:none; margin-left:15px; }
        .container { max-width: 900px; margin: 30px auto; padding: 0 20px; }
        .card { background:white; border-radius:10px; padding:25px; box-shadow: 0 10px 30px rgba(0,0,0,0.12); }
        .title { font-size:28px; margin:0 0 10px; }
        .meta { color:#666; margin-bottom:15px; }
        .price { font-size:26px; font-weight:700; color:#667eea; margin: 10px 0 20px; }
        .btn { display:inline-block; padding:12px 18px; border-radius:6px; border: none; cursor:pointer; text-decoration:none; font-weight:600; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color:white; }
        .btn-secondary { background:#f0f0f0; border:1px solid #ddd; color:#333; }
        form { display:inline; }
    .alert { padding:10px 12px; border-radius:6px; margin: 12px 0; }
    .alert-error { background:#fadbd8; border-left:4px solid #e74c3c; color:#8e2820; }
    .alert-success { background:#d5f4e6; border-left:4px solid #27ae60; color:#1b6b3d; }
    .reviews { margin-top: 22px; }
    .review-item { border-top: 1px solid #eee; padding: 12px 0; }
    .review-meta { color:#666; font-size: 13px; margin-bottom: 6px; }
    .review-comment { white-space: pre-wrap; }
    .review-form { margin-top: 14px; border-top: 1px solid #eee; padding-top: 14px; }
    .input, textarea, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-family: inherit; }
    .row { display:flex; gap:12px; }
    .col { flex:1; }
    </style>
</head>
<body>
<nav>
    <a class="nav-brand" href="${pageContext.request.contextPath}/">My Project</a>
    <div>
        <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/cart">Cart</a>
    </div>
</nav>

<div class="container">
    <div class="card">
        <%
            String error = request.getParameter("error");
            String message = request.getParameter("message");
            Object userObj = session.getAttribute("loggedInUser");
        %>

        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        <% if (message != null && !message.isEmpty()) { %>
            <div class="alert alert-success"><%= message %></div>
        <% } %>

        <c:if test="${empty product}">
            <h2>Product not found</h2>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/products">Back</a>
        </c:if>
        <c:if test="${not empty product}">
            <h1 class="title">${product.name}</h1>
            <div class="meta">Product ID: ${product.id}</div>
            <div class="price">$${product.price}</div>

            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/products">Back to Products</a>

            <form method="post" action="${pageContext.request.contextPath}/cart/add">
                <input type="hidden" name="id" value="${product.id}" />
                <button class="btn btn-primary" type="submit">Add to Cart</button>
            </form>

      <div class="reviews">
        <h2 style="margin: 18px 0 8px;">Reviews</h2>
        <div class="meta">Average rating: <strong><c:out value="${avgRating}"/></strong> / 5 ( <c:out value="${reviewCount}"/> reviews )</div>

        <c:if test="${empty reviews}">
          <p style="color:#666;">No reviews yet. Be the first to review this product.</p>
        </c:if>
        <c:if test="${not empty reviews}">
          <c:forEach var="r" items="${reviews}">
            <div class="review-item">
              <div class="review-meta">
                <strong><c:out value="${r.username}"/></strong>
                • Rating: <strong><c:out value="${r.rating}"/></strong>/5
              </div>
              <div class="review-comment"><c:out value="${r.comment}"/></div>
            </div>
          </c:forEach>
        </c:if>

        <div class="review-form">
          <h3 style="margin:0 0 10px;">Add your review</h3>
          <% if (userObj == null) { %>
            <p style="color:#666;">Please <a href="${pageContext.request.contextPath}/login.jsp">login</a> to add a review.</p>
          <% } else { %>
            <form method="post" action="${pageContext.request.contextPath}/review/add" style="display:block;">
              <input type="hidden" name="productId" value="${product.id}" />
              <div class="row">
                <div class="col">
                  <label>Rating (1-5)</label>
                  <select name="rating" class="input" required>
                    <option value="5">5</option>
                    <option value="4">4</option>
                    <option value="3">3</option>
                    <option value="2">2</option>
                    <option value="1">1</option>
                  </select>
                </div>
              </div>
              <div style="margin-top:10px;">
                <label>Comment</label>
                <textarea name="comment" rows="4" required placeholder="Write your feedback..."></textarea>
              </div>
              <div style="margin-top:10px;">
                <button class="btn btn-primary" type="submit">Submit Review</button>
              </div>
            </form>
          <% } %>
        </div>
      </div>
        </c:if>
    </div>
</div>
</body>
</html>

