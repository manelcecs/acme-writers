<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:if test="${actor.userAccount.id == userLogged.id}">

	<acme:button url="actor/edit.do" type="button" code="actor.edit" />
	<br />

</jstl:if>
<jstl:choose>

	<jstl:when test="${writer != null }" >
		<acme:button url="/actor/listWriters.do" type="button" code="actor.back"/>
	</jstl:when>
		
	<jstl:when test="${writer == null}" >
		<acme:button url="/actor/listPublishers.do" type="button" code="actor.back"/>
	</jstl:when>
</jstl:choose>

<br/>

<jstl:choose>
	<jstl:when test="${authority == 'WRITER'}">
	
		<security:authorize access="hasRole('READER')">
			<jstl:if test="${!followed}" >
				<acme:button type="button" code="reader.writer.follow" url="/reader/follow.do?writerId=${writer.id}" />
			</jstl:if>
			<jstl:if test="${followed}" >
				<acme:button type="button" code="reader.writer.unfollow" url="/reader/unfollow.do?writerId=${writer.id}" />
			</jstl:if>
		</security:authorize>
		<hr />

		<b><spring:message code="actor.name" /></b>:
		<jstl:out value="${writer.name }" />
		<br />
		<b><spring:message code="actor.surname" /></b>:
		<jstl:out value="${writer.surname }" />
		<br />
		<b><spring:message code="actor.photoURL" /></b>:
		<jstl:out value="${writer.photoURL }" />
		<br />
		<b><spring:message code="actor.email" /></b>:
		<jstl:out value="${writer.email }" />
		<br />
		<b><spring:message code="actor.address" /></b>:
		<jstl:out value="${writer.address }" />
		<br />
		<b><spring:message code="actor.phoneNumber" /></b>:
		<jstl:out value="${writer.phoneNumber }" />
		<br />

		<br />
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<b><spring:message code="actor.spammer" /></b>
			<jstl:if test="${ writer.spammer eq null}">
				<jstl:out value="N/A" />
			</jstl:if>
			<jstl:if test="${ writer.spammer != null }">
				<jstl:out value="${writer.spammer }" />
			</jstl:if>

		<br />
		</security:authorize>


	</jstl:when>
	
	<jstl:when test="${authority == 'ADMINISTRATOR'}">
		<b><spring:message code="actor.name" /></b>:
		<jstl:out value="${administrator.name }" />
		<br />
		<b><spring:message code="actor.surname" /></b>:
			<jstl:out value="${administrator.surname }" />
		<br />
		<b><spring:message code="actor.photoURL" /></b>:
		<jstl:out value="${administrator.photoURL }" />
		<br />
		<b><spring:message code="actor.email" /></b>:
		<jstl:out value="${administrator.email }" />
		<br />
		<b><spring:message code="actor.address" /></b>:
		<jstl:out value="${administrator.address }" />
		<br />
		<b><spring:message code="actor.phoneNumber" /></b>:
		<jstl:out value="${administrator.phoneNumber }" />
		<br />

		<security:authorize access="hasRole('ADMINISTRATOR')">
			<b><spring:message code="actor.spammer" /></b>
			<jstl:if test="${ administrator.spammer eq null}">
				<jstl:out value="N/A" />
			</jstl:if>
			<jstl:if test="${ administrator.spammer != null }">
				<jstl:out value="${administrator.spammer }" />
			</jstl:if>
		</security:authorize>


		<br />

	</jstl:when>

	<jstl:when test="${authority == 'READER'}">
		<b><spring:message code="actor.name" /></b>:
		<jstl:out value="${reader.name }" />
		<br />
		<b><spring:message code="actor.surname" /></b>:
			<jstl:out value="${reader.surname }" />
		<br />
		<b><spring:message code="actor.photoURL" /></b>:
		<jstl:out value="${reader.photoURL }" />
		<br />
		<b><spring:message code="actor.email" /></b>:
		<jstl:out value="${reader.email }" />
		<br />
		<b><spring:message code="actor.address" /></b>:
		<jstl:out value="${reader.address }" />
		<br />
		<b><spring:message code="actor.phoneNumber" /></b>:
		<jstl:out value="${reader.phoneNumber }" />

		<br />
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<b><spring:message code="actor.spammer" /></b>
			<jstl:if test="${ reader.spammer eq null}">
				<jstl:out value="N/A" />
			</jstl:if>
			<jstl:if test="${ reader.spammer != null }">
				<jstl:out value="${reader.spammer }" />
			</jstl:if>
		</security:authorize>

		<br />

	</jstl:when>
	
	<jstl:when test="${authority == 'PUBLISHER'}">
		<b><spring:message code="actor.name" /></b>:
		<jstl:out value="${publisher.name }" />
		<br />
		<b><spring:message code="actor.surname" /></b>:
			<jstl:out value="${publisher.surname }" />
		<br />
		<b><spring:message code="actor.publisher.commercialName" /></b>:
		<jstl:out value="${publisher.commercialName }" />
		<br />
		<b><spring:message code="actor.photoURL" /></b>:
		<jstl:out value="${publisher.photoURL }" />
		<br />
		<b><spring:message code="actor.email" /></b>:
		<jstl:out value="${publisher.email }" />
		<br />
		<b><spring:message code="actor.address" /></b>:
		<jstl:out value="${publisher.address }" />
		<br />
		<b><spring:message code="actor.phoneNumber" /></b>:
		<jstl:out value="${publisher.phoneNumber }" />
		<br/>
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<b><spring:message code="actor.spammer" /></b>
			<jstl:if test="${ publisher.spammer eq null}">
				<jstl:out value="N/A" />
			</jstl:if>
			<jstl:if test="${ publisher.spammer != null }">
				<jstl:out value="${publisher.spammer }" />
			</jstl:if>
		</security:authorize>

		<br />

	</jstl:when>
	
	<jstl:when test="${authority == 'SPONSOR'}">
		<b><spring:message code="actor.name" /></b>:
		<jstl:out value="${sponsor.name }" />
		<br />
		<b><spring:message code="actor.surname" /></b>:
			<jstl:out value="${sponsor.surname }" />
		<br />
		<b><spring:message code="actor.photoURL" /></b>:
		<jstl:out value="${sponsor.photoURL }" />
		<br />
		<b><spring:message code="actor.email" /></b>:
		<jstl:out value="${sponsor.email }" />
		<br />
		<b><spring:message code="actor.sponsor.companyName" /></b>:
		<jstl:out value="${sponsor.companyName }" />
		<br />
		<b><spring:message code="actor.address" /></b>:
		<jstl:out value="${sponsor.address }" />
		<br />
		<b><spring:message code="actor.phoneNumber" /></b>:
		<jstl:out value="${sponsor.phoneNumber }" />

		<br />
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<b><spring:message code="actor.spammer" /></b>
			<jstl:if test="${ sponsor.spammer eq null}">
				<jstl:out value="N/A" />
			</jstl:if>
			<jstl:if test="${ sponsor.spammer != null }">
				<jstl:out value="${sponsor.spammer }" />
			</jstl:if>
		</security:authorize>

		<br />

	</jstl:when>
	
</jstl:choose>

<!-- Social profiles table -->
<b><spring:message code="actor.socialProfile" /></b>
<display:table pagesize="5" name="${socialProfiles}" id="profile"
	requestURI="${requestURI}">
	<display:column titleKey="actor.socialProfiles.name">
		<jstl:out value="${ profile.nick}" />
	</display:column>
	<display:column titleKey="actor.socialProfiles.network">
		<jstl:out value="${profile.nameSocialNetwork}" />
	</display:column>
	<display:column titleKey="actor.socialProfiles.link">
		<jstl:out value="${ profile.link}" />
	</display:column>
	<security:authorize access="isAuthenticated()">
   	 <jstl:if test="${actor.userAccount.id == userLogged.id}">
   		 <display:column titleKey="actor.socialProfiles.edit">
   			<acme:button url="/socialProfile/edit.do?socialProfileId=${profile.id}" type="button" code="actor.socialProfiles.edit" />
   		 </display:column>
   		 <display:column titleKey="actor.socialProfiles.delete">
   			 <acme:button
   				 url="/socialProfile/delete.do?socialProfileId=${profile.id}"
   				 type="button" code="actor.socialProfiles.delete" />
   		 </display:column>

   	 </jstl:if>
    </security:authorize>

</display:table>
<br />


<jstl:if test="${actor.userAccount.id == userLogged.id}">
    <acme:button url="socialProfile/create.do" type="button"
   	 code="actor.socialProfile.create" />
</jstl:if>



