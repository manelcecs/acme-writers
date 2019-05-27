<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${publisher}">
	<acme:button code="contest.list.create" type="button"  url="contest/publisher/create.do"/>
</jstl:if>

<display:table pagesize="5" name="contests" id="contest" requestURI="${requestURI}">
			<display:column titleKey="contest.list.title"><jstl:out value="${contest.title}"/></display:column>
			<display:column titleKey="contest.list.prize"><jstl:out value="${contest.prize}"/></display:column>
			<display:column titleKey="contest.list.deadline"><jstl:out value="${contest.deadline}"/></display:column>
		
			<jstl:if test="${publisher}">
				<display:column titleKey="contest.list.seeMore">
					<acme:button url="contest/publisher/display.do?idContest=${contest.id}" type="button" code="contest.list.seeMore"/>
				</display:column>

				<display:column titleKey="contest.list.delete">
					<jstl:if test="${actual.before(contest.deadline)}">
						<acme:button url="contest/publisher/delete.do?idContest=${contest.id}" type="button" code="contest.list.delete"/>
					</jstl:if>
				</display:column>
				
			</jstl:if>
			<jstl:if test="${viewAll}">
				<display:column titleKey="contest.list.seeMore">
					<acme:button url="contest/display.do?idContest=${contest.id}" type="button" code="contest.list.seeMore"/>
				</display:column>
				
				<display:column titleKey="contest.list.publisher">
						<jstl:out value="${contest.publisher.commercialName}"/>
				</display:column>
								
¡				<display:column titleKey="contest.list.viewPublisher">
						<acme:button url="publisher/display.do?publisherId=${contest.publisher.id }" type="button" code="contest.list.viewPublisher"/>
				</display:column>
			</jstl:if>
			<security:authorize access="hasRole('WRITER')">
					<display:column titleKey="contest.list.participate">
				<jstl:if test="${actual.before(contest.deadline) && canParticipate.contains(contest)}">
						<acme:button url="participation/writer/create.do?idContest=${contest.id}" type="button" code="contest.list.participate"/>
				</jstl:if>
					</display:column>
			</security:authorize>
			
</display:table>