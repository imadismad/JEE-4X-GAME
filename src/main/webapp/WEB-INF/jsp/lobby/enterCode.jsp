<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Game Code</title>
    <style>
        /* Reset styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Global styles */
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
        }

        h2 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 20px;
        }

        /* Form container */
        .form-container {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 400px;
            width: 90%;
        }

        /* Form elements */
        form label {
            display: block;
            font-weight: bold;
            margin-bottom: 10px;
            text-align: left;
        }

        form input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        form input:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }

        form button {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s;
        }

        form button:hover {
            background-color: #0056b3;
        }

        /* Error message */
        .error {
            color: #d9534f;
            font-size: 14px;
            margin-top: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Join a Game</h2>
        <form action="<%= request.getContextPath() %>/lobby/join" method="get">
            <label for="gameCode">Game Code:</label>
            <input type="text" id="gameCode" name="gameCode" placeholder="Enter game code" required>
            <button type="submit">Join</button>
        </form>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
    </div>
</body>
</html>
