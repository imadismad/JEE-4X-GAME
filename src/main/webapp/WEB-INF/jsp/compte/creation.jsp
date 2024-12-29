<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Créer un compte</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .error-message {
            color: red;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            border: none;
            border-radius: 4px;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Créer un compte</h2>
        <%
            // Récupérer le message d'erreur de la requête
            String messageErreur = (String) request.getAttribute("messageErreur");
            if (messageErreur != null && !messageErreur.isBlank()) {
        %>
            <div class="error-message">
                <%= messageErreur %>
            </div>
        <%
            }
        %>
        <form action="<%= getServletContext().getContextPath() %>/compte/creation" method="post">
            <label for="nomUtilisateur">Nom d'utilisateur :</label>
            <input type="text" id="nomUtilisateur" name="nomUtilisateur" required>

            <label for="motDePasse">Mot de passe :</label>
            <input type="password" id="motDePasse" name="motDePasse" required>

            <input type="submit" value="Créer un compte">
        </form>
    </div>
</body>
</html>