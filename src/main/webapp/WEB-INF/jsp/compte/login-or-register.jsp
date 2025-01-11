<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion ou Création de Compte</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general/style.css">
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
            text-align: center;
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 400px;
        }
        h1 {
            font-size: 2.5em;
            color: #2c3e50;
            margin-bottom: 20px;
        }
        p {
            font-size: 1.2em;
            color: #34495e;
            margin-bottom: 30px;
        }
        .button {
            display: block;
            margin: 10px auto;
            padding: 15px 20px;
            font-size: 1.2em;
            color: white;
            background-color: #3498db;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .button:hover {
            background-color: #2980b9;
        }
        .button.secondary {
            background-color: #27ae60;
        }
        .button.secondary:hover {
            background-color: #2ecc71;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="container">
        <h1>Bienvenue à JEE 4X GAME</h1>
        <p>Pour continuer, veuillez vous connecter ou créer un compte.</p>
        <a href="${pageContext.request.contextPath}/compte/connexion" class="button">Se connecter</a>
        <a href="${pageContext.request.contextPath}/compte/creation" class="button secondary">Créer un compte</a>
    </div>

    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
