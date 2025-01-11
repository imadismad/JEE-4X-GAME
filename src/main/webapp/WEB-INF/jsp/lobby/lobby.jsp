<!DOCTYPE html>
<html>
<head>
    <title>Game Lobby</title>
    <script>
        function refreshLobby() {
            fetch('<%= request.getContextPath() %>/lobby/refresh?gameCode=<%= request.getAttribute("gameCode") %>')
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to refresh lobby state");
                    }
                    return response.text();
                })
                .then(html => {
                    document.getElementById('lobby-state').innerHTML = html;
                })
                .catch(error => console.error('Error refreshing lobby:', error));
        }

        // Refresh every 5 seconds
        setInterval(refreshLobby, 5000);
        window.onload = refreshLobby; // Initial load
    </script>
</head>
<body>
    <h2>Game Lobby</h2>
    <p>Game Code: <%= request.getAttribute("gameCode") %></p>
    <div id="lobby-state">
        <!-- This section will be dynamically replaced by lobbyContent.jsp -->
        <p>Loading lobby state...</p>
    </div>
    <form action="<%= request.getContextPath() %>/lobby/quit" method="post">
        <input type="hidden" name="gameCode" value="<%= request.getAttribute("gameCode") %>">
        <button type="submit">Quit Lobby</button>
    </form>
</body>
</html>
