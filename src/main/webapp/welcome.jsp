<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Bienvenue - JEE 4X GAME</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general/style.css">
</head>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            margin: 50px auto;
            padding: 20px;
            max-width: 600px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-size: 3em;
            color: #2c3e50;
            margin-bottom: 20px;
        }
        p {
            font-size: 1.2em;
            color: #34495e;
        }
        .play-button {
            display: inline-block;
            margin-top: 20px;
            padding: 15px 30px;
            font-size: 1.5em;
            color: white;
            background-color: #27ae60;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .play-button:hover {
            background-color: #2ecc71;
        }
        .names-section {
            margin-top: 40px;
            padding: 10px;
            border-top: 1px solid #ddd;
            font-size: 1em;
            color: #7f8c8d;
        }
          .names-section h2 {
                margin-bottom: 10px;
          }
         </style>
<body>
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="container">
        <h1>Bienvenue à JEE 4X GAME</h1>
        <p>Plongez dans un monde de stratégie et de conquête. Êtes-vous prêt à dominer le monde ?</p>
        <a href="${pageContext.request.contextPath}/compte/login-or-register" class="play-button">JOUER</a>
    </div>
     <div class="names-section">
     <h2>Développé par :</h2>
     <h3>Tom PETIT</h3>
     <h3>Théo KALIFAT</h3>
     <h3>Nathan FOUCHET</h3>
     <h3>Imad-Eddine BOUAAMRI</h3>
     <h4>-</h4>
     <h4>GIA1</h4>
     <h4>Professeur : Mohamed-Lamine BENZAGOUTA</h4>
     
	 </div>

    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
