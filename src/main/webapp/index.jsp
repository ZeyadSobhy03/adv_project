<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Demo3</title>
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
        }

        nav {
            background: rgba(0, 0, 0, 0.7);
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
        }

        .nav-brand {
            color: white;
            font-size: 24px;
            font-weight: 700;
            text-decoration: none;
        }

        .nav-links {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .nav-link {
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 5px;
            transition: background 0.3s;
        }

        .nav-link:hover {
            background: rgba(102, 126, 234, 0.5);
        }

        .nav-link.btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-weight: 600;
        }

        .nav-link.btn-primary:hover {
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
        }

        .welcome-section {
            background: white;
            border-radius: 10px;
            padding: 40px;
            margin-bottom: 40px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        .welcome-section h1 {
            color: #333;
            font-size: 36px;
            margin-bottom: 15px;
        }

        .welcome-section p {
            color: #666;
            font-size: 16px;
            margin-bottom: 30px;
        }

        .cta-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #667eea;
            color: white;
            transform: translateY(-2px);
        }



        .feature-card h3 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 20px;
        }

        .feature-card p {
            color: #666;
            line-height: 1.6;
        }




    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/" class="nav-brand">Project</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/login.jsp" class="nav-link">Login</a>
            <a href="${pageContext.request.contextPath}/register.jsp" class="nav-link btn-primary">Register</a>
        </div>
    </nav>

    <div class="container">
        <div class="welcome-section">
            <h1>Welcome to My Project</h1>
            <p>Your ultimate product management platform with secure authentication and real-time data</p>

            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">Login Now</a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-secondary">Create Account</a>
            </div>


        </div>
    </div>


</body>
</html>

