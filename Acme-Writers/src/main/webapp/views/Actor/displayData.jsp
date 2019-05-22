<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>




<hr>
	
	<acme:button url="actor/deleteData.do" type="button" code="actor.displayData.deleteAll"/>

<hr>

	<acme:button url="actor/saveData.do" type="button" code="actor.displayData.saveData"/>
<hr>

<input type="button" value="<spring:message code="actor.exportData.export"/>" onclick="printHTML()"/>
<hr>



<div id="print">
<jstl:choose>
	<jstl:when test="${authority == 'ROOKIE'}">
		
		<acme:text value="${rookie.name}" label="actor.displayData.name"/>
		<acme:text value="${rookie.userAccount.username}" label="actor.displayData.username"/>
		<acme:text value="${rookie.surnames}" label="actor.displayData.surnames"/>
		<acme:text value="${rookie.photo}" label="actor.displayData.photo"/>
		<acme:text value="${rookie.email}" label="actor.displayData.email"/>
		<acme:text value="${rookie.address}" label="actor.displayData.address"/>
		<acme:text value="${rookie.phoneNumber}" label="actor.displayData.phoneNumber"/>
		<acme:text value="${rookie.vatNumber}" label="actor.displayData.vatNumber"/>

			<!-- Social profiles table -->
		<b><spring:message code="actor.displayData.socialProfile" /></b>
		<display:table name="socialProfiles" id="profile">
			<spring:message code="actor.displayData.socialProfilesName" var="name" />
			<display:column title="${name }">
				<jstl:out value="${ profile.nick}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesNetwork" var="network" />
			<display:column title="${network }">
				<jstl:out value="${profile.nameSocialNetwork}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesLink" var="link" />
			<display:column title="${link }">
				<jstl:out value="${ profile.link}" />
			</display:column>
		</display:table>
		
		<br>
		
		<!-- Table with messages -->
		<b><spring:message code="actor.displayData.messages" /></b>
		<display:table name="messages" id="message" >
			<display:column titleKey="actor.displayData.messageSender"><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="actor.displayData.messageSubject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="actor.displayData.messageMoment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="actor.displayData.messageBody" ><jstl:out value="${message.body}"/></display:column>
			<display:column titleKey="actor.displayData.messagePriority"><jstl:out value="${message.priority}"/></display:column>
			<display:column titleKey="actor.displayData.messageTags">
				<jstl:forEach var="tag" items="${message.tags}">
					<jstl:out value="${tag}"/><br/>
				</jstl:forEach>
			</display:column>
		</display:table><br/>
		
		
		<!-- Table with cv -->
		
		<b><spring:message code="actor.displayData.personalData" /></b>
		<display:table name="personalDatas" id="personalData">
			<display:column titleKey="actor.displayData.cv.title"><jstl:out value="${personalData.curricula.title}"/></display:column>
			<display:column titleKey="actor.displayData.personalData.fullName"><jstl:out value="${personalData.fullName}"/></display:column>
			<display:column titleKey="actor.displayData.personalData.statement"><jstl:out value="${personalData.statement}"/></display:column>
			<display:column titleKey="actor.displayData.personalData.phoneNumber"><jstl:out value="${personalData.phoneNumber}"/></display:column>
			<display:column titleKey="actor.displayData.personalData.gitHubProfile"><jstl:out value="${personalData.gitHubProfile}"/></display:column>
			<display:column titleKey="actor.displayData.personalData.linkedinProfile"><jstl:out value="${personalData.linkedinProfile}"/></display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.positionData" /></b>
		<display:table name="positionDatas" id="positionData">
			<display:column titleKey="actor.displayData.cv.title"><jstl:out value="${positionData.curricula.title}"/></display:column>
			<display:column titleKey="actor.displayData.positionData.title"><jstl:out value="${positionData.title}"/></display:column>
			<display:column titleKey="actor.displayData.positionData.description"><jstl:out value="${positionData.description}"/></display:column>
			<display:column titleKey="actor.displayData.positionData.startDate"><jstl:out value="${positionData.startDate}"/></display:column>
			<display:column titleKey="actor.displayData.positionData.endDate"><jstl:out value="${positionData.endDate}"/></display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.educationData" /></b>
		<display:table name="educationDatas" id="educationData">
			<display:column titleKey="actor.displayData.cv.title"><jstl:out value="${personalData.curricula.title}"/></display:column>
			<display:column titleKey="actor.displayData.educationData.degree"><jstl:out value="${educationData.degree}"/></display:column>
			<display:column titleKey="actor.displayData.educationData.institution"><jstl:out value="${educationData.institution}"/></display:column>
			<display:column titleKey="actor.displayData.educationData.mark"><jstl:out value="${educationData.mark}"/></display:column>
			<display:column titleKey="actor.displayData.educationData.startDate"><jstl:out value="${educationData.startDate}"/></display:column>
			<display:column titleKey="actor.displayData.educationData.endDate"><jstl:out value="${educationData.endDate}"/></display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.miscellaneousData" /></b>
		<display:table name="miscellaneousDatas" id="miscellaneousData">
			<display:column titleKey="actor.displayData.cv.title"><jstl:out value="${miscellaneousData.curricula.title}"/></display:column>
			<display:column titleKey="actor.displayData.miscellaneousData.text"><jstl:out value="${miscellaneousData.text}"/></display:column>
			<display:column titleKey="actor.displayData.miscellaneousData.attachments"><jstl:out value="${miscellaneousData.attachments}"/></display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.applications" /></b>
		<display:table name="applications" id="appli">
			<display:column titleKey="actor.displayData.application.problem"><jstl:out value="${appli.problem.id}"/></display:column>
			<display:column titleKey="actor.displayData.application.curricula"><jstl:out value="${appli.curricula.id}"/></display:column>
			<display:column titleKey="actor.displayData.application.status"><jstl:out value="${appli.status}"/></display:column>
			<display:column titleKey="actor.displayData.application.momentSubmit"><jstl:out value="${appli.momentSubmit}"/></display:column>
			<display:column titleKey="actor.displayData.application.moment"><jstl:out value="${appli.moment}"/></display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.answers" /></b>
		<display:table name="answersOfApplications" id="answer">
			<display:column titleKey="actor.displayData.application.id"><jstl:out value="${answer.application.id}"/></display:column>
			<display:column titleKey="actor.displayData.answer.explanation"><jstl:out value="${answer.explanation}"/></display:column>
			<display:column titleKey="actor.displayData.answer.link"><jstl:out value="${answer.link}"/></display:column>
		</display:table><br/>
	
	
				
	</jstl:when>
	
	
	<jstl:when test="${authority == 'COMPANY'}">
		<acme:text value="${company.name}" label="actor.displayData.name"/>
		<acme:text value="${company.userAccount.username}" label="actor.displayData.username"/>
		<acme:text value="${company.surnames}" label="actor.displayData.surnames"/>
		<acme:text value="${company.photo}" label="actor.displayData.photo"/>
		<acme:text value="${company.email}" label="actor.displayData.email"/>
		<acme:text value="${company.address}" label="actor.displayData.address"/>
		<acme:text value="${company.phoneNumber}" label="actor.displayData.phoneNumber"/>
		<acme:text value="${company.vatNumber}" label="actor.displayData.vatNumber"/>

		

		
		<!-- Social profiles table -->
		<b><spring:message code="actor.displayData.socialProfile" /></b>
		<display:table name="socialProfiles" id="profile">
			<spring:message code="actor.displayData.socialProfilesName" var="name" />
			<display:column title="${name }">
				<jstl:out value="${ profile.nick}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesNetwork" var="network" />
			<display:column title="${network }">
				<jstl:out value="${profile.nameSocialNetwork}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesLink" var="link" />
			<display:column title="${link }">
				<jstl:out value="${ profile.link}" />
			</display:column>
		</display:table>
		
		<br>
		
		<!-- Table with messages -->
		<b><spring:message code="actor.displayData.messages" /></b>
		<display:table name="messages" id="message">
			<display:column titleKey="actor.displayData.messageSender"><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="actor.displayData.messageSubject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="actor.displayData.messageMoment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="actor.displayData.messageBody" ><jstl:out value="${message.body}"/></display:column>
			<display:column titleKey="actor.displayData.messagePriority"><jstl:out value="${message.priority}"/></display:column>
			<display:column titleKey="actor.displayData.messageTags">
				<jstl:forEach var="tag" items="${message.tags}">
					<jstl:out value="${tag}"/><br/>
				</jstl:forEach>
			</display:column>
		</display:table><br/>
		
		<b><spring:message code="actor.displayData.positions" /></b>
		<display:table name="positions" id="position">
			<display:column titleKey="actor.displayData.position.ticker"><jstl:out value="${position.ticker.identifier}"/></display:column>
			<display:column titleKey="actor.displayData.position.title"><jstl:out value="${position.title}"/></display:column>
			<display:column titleKey="actor.displayData.position.description"><jstl:out value="${position.description}"/></display:column>
			<display:column titleKey="actor.displayData.position.deadline"><jstl:out value="${position.deadline}"/></display:column>
			<display:column titleKey="actor.displayData.position.profileRequired"><jstl:out value="${position.profileRequired}"/></display:column>
			<display:column titleKey="actor.displayData.position.skillsRequired"><jstl:out value="${position.skillsRequired}"/></display:column>
			<display:column titleKey="actor.displayData.position.technologiesRequired"><jstl:out value="${position.technologiesRequired}"/></display:column>
			<display:column titleKey="actor.displayData.position.salaryOffered"><jstl:out value="${position.salaryOffered}"/></display:column>
			<display:column titleKey="actor.displayData.position.draft"><jstl:out value="${position.draft}"/></display:column>
			<display:column titleKey="actor.displayData.position.cancelled"><jstl:out value="${position.cancelled}"/></display:column>
		</display:table><br/>
		
				<b><spring:message code="actor.displayData.problems" /></b>
		<display:table name="problems" id="problem">
			<display:column titleKey="actor.displayData.problem.position"><jstl:out value="${problem.position.id}"/></display:column>
			<display:column titleKey="actor.displayData.problem.title"><jstl:out value="${problem.title}"/></display:column>
			<display:column titleKey="actor.displayData.problem.statement"><jstl:out value="${problem.statement}"/></display:column>
			<display:column titleKey="actor.displayData.problem.hint"><jstl:out value="${problem.hint}"/></display:column>
			<display:column titleKey="actor.displayData.problem.attachments"><jstl:out value="${problem.attachments}"/></display:column>
			<display:column titleKey="actor.displayData.problem.draft"><jstl:out value="${problem.draft}"/></display:column>
		</display:table><br/>
	
	
	
	</jstl:when>
	
	<jstl:when test="${authority == 'ADMINISTRATOR'}">
		<acme:text value="${administrator.name}" label="actor.displayData.name"/>
		<acme:text value="${administrator.userAccount.username}" label="actor.displayData.username"/>
		<acme:text value="${administrator.surnames}" label="actor.displayData.surnames"/>
		<acme:text value="${administrator.photo}" label="actor.displayData.photo"/>
		<acme:text value="${administrator.email}" label="actor.displayData.email"/>
		<acme:text value="${administrator.address}" label="actor.displayData.address"/>
		<acme:text value="${administrator.phoneNumber}" label="actor.displayData.phoneNumber"/>
		<acme:text value="${administrator.vatNumber}" label="actor.displayData.vatNumber"/>

		

		
		<!-- Social profiles table -->
		<b><spring:message code="actor.displayData.socialProfile" /></b>
		<display:table name="socialProfiles" id="profile">
			<spring:message code="actor.displayData.socialProfilesName" var="name" />
			<display:column title="${name }">
				<jstl:out value="${ profile.nick}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesNetwork" var="network" />
			<display:column title="${network }">
				<jstl:out value="${profile.nameSocialNetwork}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesLink" var="link" />
			<display:column title="${link }">
				<jstl:out value="${ profile.link}" />
			</display:column>
		</display:table>
		
		<br>
		
		<!-- Table with messages -->
		<b><spring:message code="actor.displayData.messages" /></b>
		<display:table pagesize="5" name="messages" id="message" requestURI="actor/displayAllData.do">
			<display:column titleKey="actor.displayData.messageSender"><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="actor.displayData.messageSubject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="actor.displayData.messageMoment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="actor.displayData.messageBody" ><jstl:out value="${message.body}"/></display:column>
			<display:column titleKey="actor.displayData.messagePriority"><jstl:out value="${message.priority}"/></display:column>
			<display:column titleKey="actor.displayData.messageTags">
				<jstl:forEach var="tag" items="${message.tags}">
					<jstl:out value="${tag}"/><br/>
				</jstl:forEach>
			</display:column>
		</display:table><br/>
	
	
	</jstl:when>
	
	<jstl:when test="${authority == 'PROVIDER'}">
		<acme:text value="${provider.name}" label="actor.displayData.name"/>
		<acme:text value="${provider.userAccount.username}" label="actor.displayData.username"/>
		<acme:text value="${provider.surnames}" label="actor.displayData.surnames"/>
		<acme:text value="${provider.photo}" label="actor.displayData.photo"/>
		<acme:text value="${provider.email}" label="actor.displayData.email"/>
		<acme:text value="${provider.address}" label="actor.displayData.address"/>
		<acme:text value="${provider.phoneNumber}" label="actor.displayData.phoneNumber"/>
		<acme:text value="${provider.vatNumber}" label="actor.displayData.vatNumber"/>

		
		<b><spring:message code="provider.items" /></b>
		<display:table name="items" id="item">
			<display:column titleKey="provider.item.name">
				<jstl:out value="${item.name}" />
			</display:column>
			<display:column titleKey="provider.item.description">
				<jstl:out value="${item.description}" />
			</display:column>
			<display:column titleKey="provider.item.links">
				<jstl:forEach var="link" items="${item.links}">
					<a href='<jstl:out value="${link}" />'><jstl:out value="${link}" /></a>				
					</jstl:forEach>
			</display:column>
			<display:column titleKey="provider.item.pictures">
				<jstl:forEach var="pic" items="${item.pictures}">
					<a href='<jstl:out value="${pic}" />'><jstl:out value="${pic}" /></a>				
				</jstl:forEach>
			</display:column>
		</display:table>
		
		<br />
		<!-- Social profiles table -->
		<b><spring:message code="actor.displayData.socialProfile" /></b>
		<display:table name="socialProfiles" id="profile">
			<spring:message code="actor.displayData.socialProfilesName" var="name" />
			<display:column title="${name }">
				<jstl:out value="${ profile.nick}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesNetwork" var="network" />
			<display:column title="${network }">
				<jstl:out value="${profile.nameSocialNetwork}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesLink" var="link" />
			<display:column title="${link }">
				<jstl:out value="${ profile.link}" />
			</display:column>
		</display:table>
		
		<br>
		
		<!-- Table with messages -->
		<b><spring:message code="actor.displayData.messages" /></b>
		<display:table pagesize="5" name="messages" id="message" requestURI="actor/displayAllData.do">
			<display:column titleKey="actor.displayData.messageSender"><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="actor.displayData.messageSubject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="actor.displayData.messageMoment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="actor.displayData.messageBody" ><jstl:out value="${message.body}"/></display:column>
			<display:column titleKey="actor.displayData.messagePriority"><jstl:out value="${message.priority}"/></display:column>
			<display:column titleKey="actor.displayData.messageTags">
				<jstl:forEach var="tag" items="${message.tags}">
					<jstl:out value="${tag}"/><br/>
				</jstl:forEach>
			</display:column>
		</display:table><br/>
	
	
	</jstl:when>
	
	<jstl:when test="${authority == 'AUDITOR'}">
		<acme:text value="${auditor.name}" label="actor.displayData.name"/>
		<acme:text value="${auditor.userAccount.username}" label="actor.displayData.username"/>
		<acme:text value="${auditor.surnames}" label="actor.displayData.surnames"/>
		<acme:text value="${auditor.photo}" label="actor.displayData.photo"/>
		<acme:text value="${auditor.email}" label="actor.displayData.email"/>
		<acme:text value="${auditor.address}" label="actor.displayData.address"/>
		<acme:text value="${auditor.phoneNumber}" label="actor.displayData.phoneNumber"/>
		<acme:text value="${auditor.vatNumber}" label="actor.displayData.vatNumber"/>

		
		<b><spring:message code="auditor.audits" /></b>
		<display:table name="auditions" id="audit">
			<display:column titleKey="auditor.audit.position">
				<jstl:out value="${audit.position}" />
			</display:column>
			<display:column titleKey="auditor.audit.draft">
				<jstl:out value="${audit.draft }" />
			</display:column>
			<display:column titleKey="auditor.audit.score">
				<jstl:out value="${audit.score }" />
			</display:column>
			<display:column titleKey="auditor.audit.moment">
				<jstl:out value="${audit.moment }" />
			</display:column>
			<display:column titleKey="auditor.audit.text">
				<jstl:out value="${audit.text }" />
			</display:column>
		</display:table>
		<br/>
		<!-- Social profiles table -->
		<b><spring:message code="actor.displayData.socialProfile" /></b>
		<display:table name="socialProfiles" id="profile">
			<spring:message code="actor.displayData.socialProfilesName" var="name" />
			<display:column title="${name }">
				<jstl:out value="${ profile.nick}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesNetwork" var="network" />
			<display:column title="${network }">
				<jstl:out value="${profile.nameSocialNetwork}" />
			</display:column>
			<spring:message code="actor.displayData.socialProfilesLink" var="link" />
			<display:column title="${link }">
				<jstl:out value="${ profile.link}" />
			</display:column>
		</display:table>
		
		<br>
		
		<!-- Table with messages -->
		<b><spring:message code="actor.displayData.messages" /></b>
		<display:table pagesize="5" name="messages" id="message" requestURI="actor/displayAllData.do">
			<display:column titleKey="actor.displayData.messageSender"><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="actor.displayData.messageSubject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="actor.displayData.messageMoment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="actor.displayData.messageBody" ><jstl:out value="${message.body}"/></display:column>
			<display:column titleKey="actor.displayData.messagePriority"><jstl:out value="${message.priority}"/></display:column>
			<display:column titleKey="actor.displayData.messageTags">
				<jstl:forEach var="tag" items="${message.tags}">
					<jstl:out value="${tag}"/><br/>
				</jstl:forEach>
			</display:column>
		</display:table><br/>
	
	
	</jstl:when>


</jstl:choose>
</div>


<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.2/jspdf.min.js"></script>

<script>
    function printHTML() {
        var pdf = new jsPDF('p', 'pt', 'letter');
        
        source = $('#print')[0];

        specialElementHandlers = {
            // element with id of "bypass" - jQuery style selector
            '#bypassme': function (element, renderer) {
                // true = "handled elsewhere, bypass text extraction"
                return true
            }
        };
        margins = {
            top: 80,
            bottom: 60,
            left: 40,
            width: 522
        };
        // all coords and widths are in jsPDF instance's declared units
        // 'inches' in this case
        pdf.fromHTML(
            source, // HTML string or DOM elem ref.
            margins.left, // x coord
            margins.top, { // y coord
                'width': margins.width, // max width of content on PDF
                'elementHandlers': specialElementHandlers
            },

            function (dispose) {
                // dispose: object with X, Y of the last line add to the PDF 
                //          this allow the insertion of new lines after html
                pdf.save('Test.pdf');
            }, margins
        );
    }
</script>





