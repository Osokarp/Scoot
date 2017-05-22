<%
    if (session.getAttribute("user") == null || request.getRequestURL().toString().contains("validatelogin.jsp")){
        response.sendRedirect("index.html");
    }
    %>