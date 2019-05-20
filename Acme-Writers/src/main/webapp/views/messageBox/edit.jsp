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

	<form:form action="messageBox/edit.do" modelAttribute="messageBox">
		
		<p><spring:message code="messageBox.edit.notLikeOriginalName"/><p>
		
		<acme:hidden path="id"/>
		
		<acme:textbox code="messageBox.edit.nameBox" path="name"/>
		
		<jstl:choose>
			<jstl:when test="${empty posibleParents}">
				<acme:hidden path="parent"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:select items="${posibleParents}" itemLabel="name" code="messageBox.edit.parent" path="parent"/>
			</jstl:otherwise>
		</jstl:choose>

		<acme:submit name="save" code="messageBox.edit.save"/>
		<acme:cancel url="messageBox/list.do" code="messageBox.edit.cancel"/>
		
	</form:form>

</section>