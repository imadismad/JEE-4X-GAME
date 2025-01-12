<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Game Code</title>

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
    
</head>
<body>
	<%@ include file="/WEB-INF/jsp/templates/header.jsp" %>
    <div class="form-container main-container">
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
    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
