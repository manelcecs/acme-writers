<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:button code="participation.display.back" type="button" url="/participation/writer/list.do"/>

<acme:text label="participation.display.comment" value="${participation.comment}"/>
<acme:text label="participation.display.moment" value="${participation.moment}"/>
<acme:text label="participation.display.status" value="${participation.status}"/>
<acme:text label="participation.display.position" value="${participation.position}"/>
<acme:text label="participation.display.contest" value="${participation.contest.description}"/>
<acme:text label="participation.display.book" value="${participation.book.title}"/>



