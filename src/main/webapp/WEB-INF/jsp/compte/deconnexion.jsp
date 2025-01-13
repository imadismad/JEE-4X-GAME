<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Invalidation de la session
    session.invalidate();
    response.sendRedirect("${pageContext.request.contextPath}/compte/connexion");
%>