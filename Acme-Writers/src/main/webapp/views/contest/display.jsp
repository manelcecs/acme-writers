<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:button code="contest.display.back" type="button" url="/contest/publisher/list.do"/>

<acme:text label="contest.display.description" value="${contest.description}"/>
<acme:text label="contest.display.prize" value="${contest.prize}"/>
<acme:text label="contest.display.deadline" value="${contest.deadline}"/>
<acme:text label="contest.display.publisher" value="${contest.publisher.commercialName}"/>

<p><strong><spring:message code="contest.display.rules" />:</strong> </p> 
<ul>
	<jstl:forEach var="rule" items="${contest.rules}">
		<li><jstl:out value="${rule}"/></li>
	</jstl:forEach>
</ul>

<hr>

<div id="sponsor" style="width: 50px;">
	<a target="_blank" href="${sponsorshipRandom.targetPageURL}" ><img style="width: 200px;" src="${sponsorshipRandom.bannerURL}" alt="${sponsorshipRandom.targetPageURL}"/></a>
</div>


