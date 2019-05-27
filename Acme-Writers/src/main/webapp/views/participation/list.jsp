<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table pagesize="5" name="participations" id="participation" requestURI="${requestURI}">
	<display:column titleKey="participation.list.contest"><jstl:out value="${participation.contest.title}"/></display:column>
	<display:column titleKey="participation.list.status"><jstl:out value="${participation.status}"/></display:column>
	<display:column titleKey="participation.list.position"><jstl:out value="${participation.position}"/></display:column>
	
	<jstl:if test="${publisher}">
	
		<display:column titleKey="participation.list.edit">
			<jstl:if test="${participation != null}">
				<jstl:if test="${actual.before(participation.contest.deadline) && participation.status == 'PENDING'}">
					<acme:button url="participation/publisher/edit.do?idParticipation=${participation.id}" type="button" code="participation.list.changeStatus"/>
				</jstl:if>
				<jstl:if test="${!actual.before(participation.contest.deadline) && participation.status == 'ACCEPTED'}">
					<acme:button url="participation/publisher/edit.do?idParticipation=${participation.id}" type="button" code="participation.list.changePosition"/>
				</jstl:if>
			</jstl:if>
		</display:column>
		
		<display:column titleKey="participation.list.viewContest">
			<acme:button url="contest/display.do?idContest=${participation.contest.id}&urlBack=${requestURI}" type="button" code="participation.list.viewContest"/>
		</display:column>
	</jstl:if>			
		<display:column titleKey="participation.list.seeMore">
			<acme:button url="participation/publisher,writer/display.do?idParticipation=${participation.id}" type="button" code="participation.list.seeMore"/>
		</display:column>
									
	<security:authorize access="hasRole('WRITER')">
		<jstl:if test="${participation != null}">
			<jstl:if test="${actual.before(participation.contest.deadline)}">
				<display:column titleKey="participation.list.delete">
						<acme:button url="participation/writer/delete.do?idParticipation=${participation.id}" type="button" code="participation.list.delete"/>
				</display:column>
			</jstl:if>
		</jstl:if>
		
		<display:column titleKey="participation.list.viewContest">
			<acme:button url="contest/display.do?idContest=${participation.contest.id}&urlBack=${requestURI}" type="button" code="participation.list.viewContest"/>
		</display:column>
	</security:authorize>

			
</display:table>