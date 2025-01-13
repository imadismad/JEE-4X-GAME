<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Game Lobby</title>
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
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            padding: 20px;
        }

        h2 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 20px;
        }

        p {
            font-size: 16px;
            color: #34495e;
            margin-bottom: 15px;
        }

        #lobby-state {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        button {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
    <script>
        function refreshLobby() {
            fetch('<%= request.getContextPath() %>/lobby/refresh?gameCode=<%= request.getAttribute("gameCode") %>')
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to refresh lobby state");
                    }
                    console.log(response);
                    if(response.redirected === true) {
                    	window.location.href = response.url;
                    	return;
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
