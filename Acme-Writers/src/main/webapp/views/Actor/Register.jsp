<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<form:form action="" atributte="actor">

	<acme:textbox path="username" code="actor.username"/>
	<acme:password path="password" code="actor.password"/>
	<acme:password path="password" code="actor.password"/>
	
	<jstl:if test="${pageContext.response.locale.language} == 'es'"> 
		<a href="/termsAndConditions/es" target="_blanck" ><spring:message code="register.terms" />: </a><input type="checkbox" name="${terms}" />
	</jstl:if>
	<jstl:if test="${pageContext.response.locale.language} == 'en'"> 
		<a href="/termsAndConditions/en" target="_blanck" ><spring:message code="register.terms" />: </a><input type="checkbox" name="${terms}" />
	</jstl:if>
	
	
	
</form:form>