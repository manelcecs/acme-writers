<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<display:table name="actors" id="actor" requestURI="${requestURI}" pagesize="5">
	<display:column titleKey="administrator.listActors.users" ><jstl:out value="${actor.userAccount.username}" /></display:column>
	<display:column><acme:button url="actor/administrator/display.do?idActor=${actor.id}" type="button" code="administrator.listActors.profile"/> </display:column>
</display:table>
