<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<b><spring:message code="announcement.writer"/></b>: <em><jstl:out value="${announcement.writer.name }" /></em>
<br/>
<em><spring:message code="announcement.moment"/></em>: <jstl:out value="${announcement.moment }" />
<hr />
<b><spring:message code="announcement.writer"/></b>
<br />
<jstl:out value="${announcement.text}" />
<br />
<jstl:if test="${back}">
	<acme:button code="announcement.back" type="button" url="/announcement/writer/list.do" />
</jstl:if>