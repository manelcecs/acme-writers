
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:choose>
<jstl:when test="${myChapter}">
 	<acme:button url="book/writer/display.do?idBook=${chapter.book.id}" type="button" code="book.display.back"/>
</jstl:when>

<jstl:when test="${publisher}">
 	<acme:button url="book/publisher/display.do?idBook=${chapter.book.id}" type="button" code="book.display.back"/>
</jstl:when>
<jstl:otherwise>
	<button onclick="window.location.href=document.referrer"><spring:message code="book.display.back"/></button>
</jstl:otherwise>
</jstl:choose>

<acme:text label="chapter.display.book" value="${chapter.book.title}"/>
<acme:text label="chapter.display.number" value="${chapter.number}"/>
<acme:text label="chapter.display.title" value="${chapter.title}"/>
<jstl:out value="${chapter.text}"/>


