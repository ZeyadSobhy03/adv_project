<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Demo3</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
        }

        .login-box {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            padding: 40px;
        }

        .login-box h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }

        .btn-login {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            margin-top: 10px;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-login:active {
            transform: translateY(0);
        }

        .form-footer {
            text-align: center;
            margin-top: 20px;
        }

        .form-footer p {
            color: #666;
            font-size: 14px;
        }

        .form-footer a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s;
        }

        .form-footer a:hover {
            color: #764ba2;
        }

        .error-message {
            color: #e74c3c;
            font-size: 14px;
            margin-top: 10px;
            padding: 10px;
            background: #fadbd8;
            border-left: 4px solid #e74c3c;
            border-radius: 3px;
            display: none;
        }

        .error-message.show {
            display: block;
        }

        .success-message {
            color: #27ae60;
            font-size: 14px;
            margin-bottom: 20px;
            padding: 10px;
            background: #d5f4e6;
            border-left: 4px solid #27ae60;
            border-radius: 3px;
            display: none;
        }

        .success-message.show {
            display: block;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-box">
            <h2>Login</h2>

            <%
                String error = request.getParameter("error");
                String message = request.getParameter("message");
            %>

            <% if (message != null && !message.isEmpty()) { %>
                <div class="success-message show">
                    <%= message %>
                </div>
            <% } %>

            <form method="POST" action="${pageContext.request.contextPath}/login" id="loginForm">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Enter your username" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                </div>

                <% if (error != null && !error.isEmpty()) { %>
                    <div class="error-message show">
                        <%= "1".equals(error) ? "Invalid username or password" : error %>
                    </div>
                <% } %>

                <button type="submit" class="btn-login">Login</button>
            </form>

            <div class="form-footer">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Create one here</a></p>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (username === '') {
                e.preventDefault();
                alert('Please enter your username');
                return;
            }
            if (password === '') {
                e.preventDefault();
                alert('Please enter your password');
                return;
            }
        });
    </script>
</body>
</html>

