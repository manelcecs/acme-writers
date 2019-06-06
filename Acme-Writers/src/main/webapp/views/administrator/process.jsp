<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<hr>

<h3>
	<spring:message code="administrator.process.cancelCreditCardCaducate" />
</h3>

<acme:button code="administrator.process.accion" type="button" url="administrator/cancelSponsorship.do" />


<hr>

<h3>
	<spring:message code="administrator.process.computeScore" />
</h3>

<acme:button code="administrator.process.compute" type="button" url="administrator/computeScore.do" />


<hr>


<h3>
	<spring:message code="administrator.process.spam" />
</h3>

<acme:button code="administrator.process.updateSpam" type="button" url="administrator/updateSpam.do" />

<br>

<display:table pagesize="5" name="spamActors" id="actor" requestURI="${requestURI}">

	<display:column titleKey="administrator.process.userName">
		<jstl:out value="${actor.userAccount.username}"/>
	</display:column>
	
	<jstl:if test="${actor.userAccount != actorLogged}">
		<jstl:if test="${actor.banned}">
			<display:column>
				<acme:button url="administrator/unban.do?idActor=${actor.id }" type="button" code="administrator.process.unban"/>
			</display:column>
		</jstl:if>
		
		<jstl:if test="${not actor.banned}">
			<display:column>
				<acme:button url="administrator/ban.do?idActor=${actor.id}" type="button" code="administrator.process.ban"/>
			</display:column>
		</jstl:if>
	</jstl:if>	
	
	<jstl:if test="${actor.userAccount == actorLogged}">
		<display:column>
		</display:column>
	</jstl:if>	
	
</display:table>