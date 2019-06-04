<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<html>
<body>
<h2>Hello World!</h2>
</body>
<%--<a href="${pageContext.request.contextPath}/product/findAll.do">查询所有的产品信息</a>--%>
<jsp:forward page="/pages/main.jsp"></jsp:forward>
</html>

