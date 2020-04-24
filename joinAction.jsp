<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.userDAO" %>
<%@ page import="java.io.PrintWriter" %>
<%request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" /> 
<jsp:setProperty name="user" property="userID" />
<jsp:setProperty name="user" property="userPassword" />
<jsp:setProperty name="user" property="userName" />
<jsp:setProperty name="user" property="userGender" />
<jsp:setProperty name="user" property="userEmail" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자바칩 프라푸치노</title>
</head>
<body>
	<%
		if(user.getUserID()==null||user.getUserPassword()==null||user.getUserName()==null||
				user.getUserGender()==null||user.getUserEmail()==null){
			PrintWriter script = response.getWriter();
	 		script.println("<script>");
	 		script.println("alert('입력이 안 된 사항이 있습니다.')");
	 		script.println("history.back()"); //사용자가 다시 고친 후 사용할 수 있게 함.
	 		script.println("</script>");	
		}else {
			userDAO userDAO = new userDAO();
		 	int result= userDAO.join(user);
		 	if (result==-1){
		 		PrintWriter script = response.getWriter();
		 		script.println("<script>");
		 		script.println("alert('이미 존재하는 아이디입니다.')");
		 		script.println("history.back()");
		 		script.println("</script>");
		 	} 
		 	else if(result==0){
		 		PrintWriter script = response.getWriter();
		 		script.println("<script>");
		 		script.println("location.href='main.jsp'");
		 		script.println("</script>");
		 	}
		}
		
	%>
</body>
</html>