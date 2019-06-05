

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<display:table pagesize="5" name="actors" id="actor" requestURI="${requestURI}">
		<jstl:if test="${rolView == 'WRITER'}">
   		<display:column titleKey="actor.list.writer"><jstl:out value="${actor.name}"/> <jstl:out value="${actor.surname}"/></display:column>
   		<display:column titleKey="actor.list.viewBooks">
   			<acme:button url="book/listByWriter.do?idWriter=${actor.id}" type="button" code="actor.list.viewBooks"/>
		</display:column>
   		<display:column titleKey="writer.list.seeMore"><acme:button code="writer.list.seeMore" type="button" url="/writer/display.do?writerId=${actor.id}&targetURL=${targetURL}"/></display:column>
		</jstl:if>
		<jstl:if test="${rolView == 'PUBLISHER'}">
   		<display:column titleKey="actor.list.publisher"><jstl:out value="${actor.commercialName}"/></display:column>
   		<display:column titleKey="actor.list.viewBooks">
   			<acme:button url="book/listByPublisher.do?idPublisher=${actor.id}" type="button" code="actor.list.viewBooks"/>
		</display:column>
		<display:column titleKey="writer.list.seeMore"><acme:button code="writer.list.seeMore" type="button" url="publisher/display.do?publisherId=${actor.id}&targetURL=${targetURL}"/></display:column>
		</jstl:if>
</display:table>