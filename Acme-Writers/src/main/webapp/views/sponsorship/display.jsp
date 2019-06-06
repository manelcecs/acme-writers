<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:cancel url="sponsorship/sponsor/list.do" code="sponsorship.display.back" />

<section id="main">

	<section id="displaySponsorship">
	
		<h3><spring:message code="sponsorship.display.info"/></h3>
		<hr>	
		
		
		<div>
		
		<p><strong><spring:message code="sponsorship.display.status"/></strong> <jstl:out value="${status}"/> | <spring:message code="sponsorship.display.views1"/> <jstl:out value="${sponsorship.views}"/> <spring:message code="sponsorship.display.views2"/></p>
	 	<strong><spring:message code="sponsorship.display.targetPageURL"/></strong> <a target="_blank" href="${sponsorship.targetPageURL}"> <jstl:out value="${sponsorship.targetPageURL}"></jstl:out></a>
		<p><strong><spring:message code="sponsorship.display.flatRateApplied"/></strong> <jstl:out value="${sponsorship.flatRateApplied}"></jstl:out> | <strong><spring:message code="sponsorship.display.flatRateAppliedWithVAT"/></strong> <jstl:out value="${flatRateAppliedWithVAT}"></jstl:out> </p>
		<p><strong><spring:message code="sponsorship.display.banner"/></strong></p>
		<img style="width: 100%;"  src="${sponsorship.bannerURL}"/>
		
		</div>

		
		<hr>
		
	
 
	</section>
	
	<section id="displayContests">
		
	
		<h3><spring:message code="sponsorship.display.contests"/></h3>
		<hr>
		
		<display:table pagesize="5" name="sponsorship.contests" id="contest" requestURI="${requestURI}">
 				<display:column titleKey="sponsorship.display.contestTitle"><jstl:out value="${contest.title}"/></display:column>
	   		 	<display:column titleKey="sponsorship.display.contestDisplay"><acme:button url="contest/display.do?idContest=${contest.id}&urlBack=${requestURI}" type="button" code="sponsorship.display.contestDisplay"/></display:column>
		</display:table>
		
		<hr>
	
	</section>
	
	
		



<hr>
</section>



<style>
 #main {
	float: left;
	width: 100%
}
 #main > hr{
	float: left;
	margin-top: 50px;
	width: 100%;
}

#displaySponsorship {
	float: left;
	width: 45%;
	padding: 1% 2.5%;
}

#displayContests {
	float: left;
	width: 45%;
	padding: 1% 2.5%;
}


 .botones{
  	margin-left: 70px;
  	float: right;
}

.botones > button{
	margin-left: 10px;
}
</style>

