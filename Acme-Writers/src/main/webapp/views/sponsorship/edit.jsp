<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<section>

	<jstl:if test="${sponsorshipForm.id == 0}">
		<p><spring:message code="sponsorship.edit.fare" /> <jstl:out value="${flatRate}"/> | <jstl:out value="${flatRateWithVAT}"/> (<spring:message code="sponsorship.edit.withVAT" />) </p>
	</jstl:if>

	<form:form action="sponsorship/sponsor/edit.do" modelAttribute="sponsorshipForm">
		
		<acme:hidden path="id"/>
		
		<acme:textbox code="sponsorship.edit.targetPageURL" path="targetPageURL"/>
		<acme:textbox code="sponsorship.edit.bannerURL" path="bannerURL"/>
		
	
		<acme:select items="${posibleContests}" itemLabel="description" code="sponsorship.edit.contests" path="contests"/>
		

		
		<acme:submit name="save" code="sponsorship.edit.save"/>
		<acme:cancel url="sponsorship/sponsor/list.do" code="sponsorship.edit.cancel"/>
		
	</form:form>

</section>