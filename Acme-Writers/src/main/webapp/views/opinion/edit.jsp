<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="opinion" action="opinion/reader/edit.do">
		<acme:hidden path="id"/>
		<acme:hidden path="version"/>
		<acme:hidden path="book"/>
	
		<p>
			<acme:textarea code="opinion.edit.review" path="review"/>
		</p>
		
		<acme:checkbox code="opinion.edit.like" path="positiveOpinion"/>	
		
		<acme:submit name="save" code="opinion.edit.save"/>
			<jstl:choose>
			<jstl:when test="${opinion.id == 0}">
				<acme:cancel url="${urlBack}" code="opinion.edit.back"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:cancel url="opinion/reader/list.do" code="opinion.edit.back"/>
			</jstl:otherwise>
		</jstl:choose>
	</form:form>
	
