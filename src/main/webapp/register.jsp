<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Demo3</title>
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
            padding: 20px;
        }

        .container {
            width: 100%;
            max-width: 400px;
        }

        .register-box {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            padding: 40px;
        }

        .register-box h2 {
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

        .password-requirements {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
            padding: 8px;
            background: #f5f5f5;
            border-radius: 3px;
        }

        .requirement {
            margin: 3px 0;
        }

        .requirement.valid {
            color: #27ae60;
        }

        .requirement.invalid {
            color: #e74c3c;
        }

        .btn-register {
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

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-register:active {
            transform: translateY(0);
        }

        .btn-register:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
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
        <div class="register-box">
            <h2>Create Account</h2>

            <%
                String error = request.getParameter("error");
                String message = request.getParameter("message");
            %>

            <% if (message != null && !message.isEmpty()) { %>
                <div class="success-message show">
                    <%= message %>
                </div>
            <% } %>

            <form method="POST" action="${pageContext.request.contextPath}/register" id="registerForm">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Enter your username"
                           pattern="[A-Za-z0-9_]{3,30}" required>
                    <div class="password-requirements">
                        Username must be 3-30 characters (letters, numbers, underscore only)
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    <div class="password-requirements">
                        <div class="requirement invalid" id="lengthReq">
                            ✗ Minimum 6 characters
                        </div>
                        <div class="requirement invalid" id="numberReq">
                            ✗ At least one number
                        </div>
                        <div class="requirement invalid" id="letterReq">
                            ✗ At least one letter
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm your password" required>
                    <div class="password-requirements">
                        <div class="requirement invalid" id="matchReq">
                            ✗ Passwords must match
                        </div>
                    </div>
                </div>

                <% if (error != null && !error.isEmpty()) { %>
                    <div class="error-message show">
                        <%= error %>
                    </div>
                <% } %>

                <button type="submit" class="btn-register" id="submitBtn">Create Account</button>
            </form>

            <div class="form-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login here</a></p>
            </div>
        </div>
    </div>

    <script>
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const submitBtn = document.getElementById('submitBtn');

        function validatePassword() {
            const password = passwordInput.value;

            const lengthValid = password.length >= 6;
            const numberValid = /[0-9]/.test(password);
            const letterValid = /[a-zA-Z]/.test(password);

            document.getElementById('lengthReq').className =
                'requirement ' + (lengthValid ? 'valid' : 'invalid');
            document.getElementById('lengthReq').textContent =
                (lengthValid ? '✓' : '✗') + ' Minimum 6 characters';

            document.getElementById('numberReq').className =
                'requirement ' + (numberValid ? 'valid' : 'invalid');
            document.getElementById('numberReq').textContent =
                (numberValid ? '✓' : '✗') + ' At least one number';

            document.getElementById('letterReq').className =
                'requirement ' + (letterValid ? 'valid' : 'invalid');
            document.getElementById('letterReq').textContent =
                (letterValid ? '✓' : '✗') + ' At least one letter';

            validateMatch();
        }

        function validateMatch() {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            const match = password === confirmPassword && password !== '';

            document.getElementById('matchReq').className =
                'requirement ' + (match ? 'valid' : 'invalid');
            document.getElementById('matchReq').textContent =
                (match ? '✓' : '✗') + ' Passwords must match';

            updateSubmitButton();
        }

        function updateSubmitButton() {
            const username = usernameInput.value.trim();
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            const usernameValid = username.match(/^[A-Za-z0-9_]{3,30}$/);
            const passwordValid = password.length >= 6 && /[0-9]/.test(password) && /[a-zA-Z]/.test(password);
            const matchValid = password === confirmPassword && password !== '';

            submitBtn.disabled = !(usernameValid && passwordValid && matchValid);
        }

        passwordInput.addEventListener('input', validatePassword);
        confirmPasswordInput.addEventListener('input', validateMatch);
        usernameInput.addEventListener('input', updateSubmitButton);

        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const username = usernameInput.value.trim();
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!username.match(/^[A-Za-z0-9_]{3,30}$/)) {
                e.preventDefault();
                alert('Username must be 3-30 characters (letters, numbers, underscore only)');
                return;
            }

            if (password.length < 6) {
                e.preventDefault();
                alert('Password must be at least 6 characters');
                return;
            }

            if (!/[0-9]/.test(password)) {
                e.preventDefault();
                alert('Password must contain at least one number');
                return;
            }

            if (!/[a-zA-Z]/.test(password)) {
                e.preventDefault();
                alert('Password must contain at least one letter');
                return;
            }

            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Passwords do not match');
                return;
            }
        });

        // Initial button state
        updateSubmitButton();
    </script>
</body>
</html>

