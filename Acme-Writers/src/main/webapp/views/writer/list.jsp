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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table pagesize="5" name="writers" id="writer" requestURI="${requestURI}">
			<display:column titleKey="writer.list.name"><jstl:out value="${writer.name}"/></display:column>
			<display:column titleKey="writer.list.surname"><jstl:out value="${writer.surname}"/></display:column>
			<display:column titleKey="writer.list.seeMore"><acme:button code="writer.list.seeMore" type="button" url="/writer/display.do?writerId=${writer.id}"/></display:column>
</display:table>