<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('BAN')">
		<h3><spring:message code="welcome.banned" /></h3>
</security:authorize>

<security:authorize access="not(hasRole('BAN'))">
	<p>
		<spring:message code="lng" var="lng"/>
		<jstl:choose>
			<jstl:when test="${lng == 'es'}">
				<jstl:out value="${welcomeMsgEs}"/> 
			</jstl:when>
			
			<jstl:otherwise>
				<jstl:out value="${welcomeMsgEn}"/> 
			</jstl:otherwise>
		</jstl:choose>
	
	</p>
	
	
	<p><spring:message code="welcome.greeting.prefix" /><jstl:out value="${name}"/><spring:message code="welcome.greeting.suffix" /></p>

	<acme:text value="${moment}" label="welcome.greeting.current.time"/>

</security:authorize>