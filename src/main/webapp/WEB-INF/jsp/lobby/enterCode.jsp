<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Enter Game Code</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
</head>
<body>
    <h2>Join a Game</h2>
    <form action="<%= request.getContextPath() %>/lobby/join" method="get">
        <label for="gameCode">Game Code:</label>
        <input type="text" id="gameCode" name="gameCode" required>
        <button type="submit">Join</button>
    </form>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>
</body>
</html>
