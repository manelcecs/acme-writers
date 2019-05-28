<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:cancel url="opinion/reader/list.do" code="opinion.display.back"/>

<acme:text label="opinion.display.book" value="${opinion.book.title}"/>
<acme:text label="opinion.display.moment" value="${opinion.moment}"/>
<acme:text label="opinion.display.review" value="${opinion.review}"/>


<jstl:choose>
	<jstl:when test="${opinion.positiveOpinion}">
	<img height="50px" alt="http://pngimg.com/uploads/like/like_PNG17.png" src="http://pngimg.com/uploads/like/like_PNG17.png">
	</jstl:when>
	<jstl:otherwise>
	<img height="50px" alt="https://www.freeiconspng.com/uploads/facebook-dislike-transparent-25.png" src="https://www.freeiconspng.com/uploads/facebook-dislike-transparent-25.png">
	</jstl:otherwise>
</jstl:choose>


