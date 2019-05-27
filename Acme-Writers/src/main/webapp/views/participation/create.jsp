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

<form:form modelAttribute="participation" action="participation/writer/create.do">
		<acme:hidden path="contest"/>
		<p>
			<acme:textarea code="participation.create.comment" path="comment"/>
		</p>
		<acme:select items="${books}" itemLabel="title" code="participation.create.book" path="book" optional="false"/>			
		<acme:submit name="save" code="participation.create.save" />
		<acme:cancel url="/contest/list.do" code="participation.create.cancel"/>
</form:form>