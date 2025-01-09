<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    List<String> chatMessages = (List<String>) request.getAttribute("chatMessages");
%>
<div id="chat">
    <h3>Chat du jeu</h3>
    <ul id="console">
        
    </ul>
</div>
