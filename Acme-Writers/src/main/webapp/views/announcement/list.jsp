<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${writer}">
	<acme:button code="announcement.list.create" type="button"
		url="announcement/writer/create.do" />
</jstl:if>

<display:table pagesize="5" name="announcements" id="announcement"
	requestURI="${requestURI}">
	<display:column titleKey="announcement.list.writer">
		<jstl:out value="${announcement.writer.name}" />
	</display:column>
	<display:column titleKey="announcement.list.moment">
		<jstl:out value="${announcement.moment}" />
	</display:column>
	<jstl:if test="${!writer}">
		<display:column titleKey="announcement.list.seeMore">
			<acme:button type="button" code="announcement.list.seeMore"
				url="/announcement/reader/display.do?announcementId=${announcement.id}" />
		</display:column>
	</jstl:if>
	<jstl:if test="${writer}">
		<display:column titleKey="announcement.list.seeMore">
			<acme:button type="button" code="announcement.list.seeMore"
				url="/announcement/writer/display.do?announcementId=${announcement.id}" />
		</display:column>
		<display:column titleKey="announcement.list.delete">
			<acme:button
				url="/announcement/writer/delete.do?announcementId=${announcement.id}"
				type="button" code="announcement.list.delete" />

		</display:column>

	</jstl:if>
</display:table>