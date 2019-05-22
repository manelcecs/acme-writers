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
	
	<section>
	
		<display:table name="adminConfig" id="adminConfig">
			<display:column titleKey="adminConfig.display.cacheFinder" ><acme:text value="${adminConfig.finderCacheTime}"/></display:column>
			<display:column titleKey="adminConfig.display.resultsFinder" ><acme:text value="${adminConfig.finderResults}"/></display:column>
			<display:column titleKey="adminConfig.display.VAT" ><acme:text value="${adminConfig.VAT}"/></display:column>
			<display:column titleKey="adminConfig.display.flatRate" ><acme:text value="${adminConfig.flatRate}"/></display:column>
			<display:column titleKey="adminConfig.display.systemName" ><acme:text value="${adminConfig.systemName}"/></display:column>
			<display:column titleKey="adminConfig.display.countryCode" ><acme:text value="${adminConfig.countryCode}"/></display:column>
 			<display:column titleKey="adminConfig.display.bannerURL" ><acme:link value="${adminConfig.bannerURL}"/></display:column>
 			<display:column titleKey="adminConfig.display.spammerPercentage" ><acme:text value="${adminConfig.spammerPercentage}"/></display:column>
			<display:column titleKey="adminConfig.display.welcomeMessage" > <acme:text value="${adminConfig.welcomeMessageEN}"/><acme:text value="${adminConfig.welcomeMessageES}"/></display:column>
			<display:column titleKey="adminConfig.display.spamWords" ><acme:text value="${adminConfig.spamWords}"/></display:column>
			<display:column titleKey="adminConfig.display.creditCardMakes" ><acme:text value="${adminConfig.creditCardMakes}"/></display:column>
		</display:table>
		
	<acme:button url="adminConfig/administrator/edit.do" type="button" code="adminConfig.display.edit"/>
	
	</section>
	


