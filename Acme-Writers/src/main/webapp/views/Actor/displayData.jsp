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

<acme:button url="actor/deleteData.do" type="button"
	code="actor.displayData.deleteAll" />

<hr>

<acme:button url="actor/saveData.do" type="button"
	code="actor.displayData.saveData" />
<hr>

<input type="button"
	value="<spring:message code="actor.exportData.export"/>"
	onclick="printHTML()" />
<hr>



<div id="print">
	<jstl:choose>
		<jstl:when test="${authority == 'WRITER'}">

			<acme:text value="${writer.name}" label="actor.displayData.name" />
			<acme:text value="${writer.userAccount.username}"
				label="actor.displayData.username" />
			<acme:text value="${writer.surname}"
				label="actor.displayData.surnames" />
			<acme:text value="${writer.photoURL}" label="actor.displayData.photo" />
			<acme:text value="${writer.email}" label="actor.displayData.email" />
			<acme:text value="${writer.address}"
				label="actor.displayData.address" />
			<acme:text value="${writer.phoneNumber}"
				label="actor.displayData.phoneNumber" />

			<br />
			<acme:text value="${writer.creditCard.holder }" label="actor.creditCard.holder" />
			<acme:text value="${writer.creditCard.make }" label="actor.creditCard.make" />
			<acme:text value="${writer.creditCard.number }" label="actor.creditCard.number" />
			<acme:text value="${writer.creditCard.expirationMonth }" label="actor.creditCard.expirationMonth" />
			<acme:text value="${writer.creditCard.expirationYear }" label="actor.creditCard.expirationYear" />
			<acme:text value="${writer.creditCard.cvv }" label="actor.creditCard.cvv" />
			<br /> 
 
 
			<b><spring:message code="writer.books" /></b>
			<display:table name="books" id="book">
				<display:column titleKey="book.ticker">
					<jstl:out value="${book.ticker.identifier }" />
				</display:column>
				<display:column titleKey="book.title">
					<jstl:out value="${book.title }" />
				</display:column>
				<display:column titleKey="book.description">
					<jstl:out value="${book.description }" />
				</display:column>
				<display:column titleKey="book.language">
					<jstl:out value="${book.language }" />
				</display:column>
				<display:column titleKey="book.numWords">
					<jstl:out value="${book.numWords }" />
				</display:column>
				<display:column titleKey="book.cover">
					<jstl:out value="${book.cover }" />
				</display:column>
				<display:column titleKey="book.genre">
					<jstl:out value="${book.genre.nameEN }" /> /
					<jstl:out value="${book.genre.nameES }" />
				</display:column>
				<display:column titleKey="book.publisher">
					<jstl:out value="${book.publisher.commercialName }" />
				</display:column>
				<display:column titleKey="book.score">
					<jstl:out value="${book.score }" />
				</display:column>
			</display:table>
			<br />

			<b><spring:message code="writer.chapters" /></b>
			<display:table name="chapters" id="chapter">
				<display:column titleKey="chapter.title">
					<jstl:out value="${chapter.title }" />
				</display:column>
				<display:column titleKey="chapter.number">
					<jstl:out value="${chapter.number }" />
				</display:column>
				<display:column titleKey="chapter.text">
					<jstl:out value="${chapter.text }" />
				</display:column>
				<display:column titleKey="chapter.book">
					<jstl:out value="${chapter.book.title}" />
				</display:column>
			</display:table>
			<br />

			<b><spring:message code="writer.participations" /></b>
			<display:table name="participations" id="participation">
				<display:column titleKey="participation.contest.name">
					<jstl:out value="${participation.contest.title }" />
				</display:column>
				<display:column titleKey="participation.book">
					<jstl:out value="${participation.book.title }" />
				</display:column>
				<display:column titleKey="participation.position">
					<jstl:out value="${participation.position }" />
				</display:column>
				<display:column titleKey="participation.moment">
					<jstl:out value="${participation.moment}" />
				</display:column>
				<display:column titleKey="participation.status">
					<jstl:out value="${participation.status}" />
				</display:column>
				<display:column titleKey="participation.comment">
					<jstl:out value="${participation.comment}" />
				</display:column>
			</display:table>
			<br />

			<b><spring:message code="writer.announcements" /></b>
			<display:table name="announcements" id="ann">
				<display:column titleKey="announcement.moment">
					<jstl:out value="${ann.moment}" />
				</display:column>
				<display:column titleKey="announcement.text">
					<jstl:out value="${ann.text}" />
				</display:column>
			</display:table>
			<br />

			<!-- Social profiles table -->
			<b><spring:message code="actor.displayData.socialProfile" /></b>
			<display:table name="socialProfiles" id="profile">
				<spring:message code="actor.displayData.socialProfilesName"
					var="name" />
				<display:column title="${name }">
					<jstl:out value="${ profile.nick}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesNetwork"
					var="network" />
				<display:column title="${network }">
					<jstl:out value="${profile.nameSocialNetwork}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesLink"
					var="link" />
				<display:column title="${link }">
					<jstl:out value="${ profile.link}" />
				</display:column>
			</display:table>

			<br>

			<!-- Table with messages -->
			<b><spring:message code="actor.displayData.messages" /></b>
			<display:table name="messages" id="message">
				<display:column titleKey="actor.displayData.messageSender">
					<jstl:out value="${message.sender.email}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageSubject">
					<jstl:out value="${message.subject}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageMoment">
					<jstl:out value="${message.moment}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageBody">
					<jstl:out value="${message.body}" />
				</display:column>
				<display:column titleKey="actor.displayData.messagePriority">
					<jstl:out value="${message.priority}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageTags">
					<jstl:forEach var="tag" items="${message.tags}">
						<jstl:out value="${tag}" />
						<br />
					</jstl:forEach>
				</display:column>
			</display:table>
			<br />



		</jstl:when>


		<jstl:when test="${authority == 'READER'}">
			<acme:text value="${reader.name}" label="actor.displayData.name" />
			<acme:text value="${reader.userAccount.username}"
				label="actor.displayData.username" />
			<acme:text value="${reader.surname}"
				label="actor.displayData.surnames" />
			<acme:text value="${reader.photoURL}" label="actor.displayData.photo" />
			<acme:text value="${reader.email}" label="actor.displayData.email" />
			<acme:text value="${reader.address}"
				label="actor.displayData.address" />
			<acme:text value="${reader.phoneNumber}"
				label="actor.displayData.phoneNumber" />

			<br />
			<b><spring:message code="reader.displayData.opinions" /></b>
			<display:table name="opinions" id="opinion">

				<display:column titleKey="opinion.book">
					<jstl:out value="${opinion.book.title}" />
				</display:column>

				<display:column titleKey="opinion.moment">
					<jstl:out value="${opinion.moment}" />
				</display:column>

				<display:column titleKey="opinion.positive">
					<jstl:out value="${opinion.positiveOpinion}" />
				</display:column>

				<display:column titleKey="opinion.review">
					<jstl:out value="${opinion.review}" />
				</display:column>
			</display:table>
			<br />

			<!-- Social profiles table -->
			<b><spring:message code="actor.displayData.socialProfile" /></b>
			<display:table name="socialProfiles" id="profile">
				<spring:message code="actor.displayData.socialProfilesName"
					var="name" />
				<display:column title="${name }">
					<jstl:out value="${ profile.nick}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesNetwork"
					var="network" />
				<display:column title="${network }">
					<jstl:out value="${profile.nameSocialNetwork}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesLink"
					var="link" />
				<display:column title="${link }">
					<jstl:out value="${ profile.link}" />
				</display:column>
			</display:table>

			<br>

			<!-- Table with messages -->
			<b><spring:message code="actor.displayData.messages" /></b>
			<display:table name="messages" id="message">
				<display:column titleKey="actor.displayData.messageSender">
					<jstl:out value="${message.sender.email}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageSubject">
					<jstl:out value="${message.subject}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageMoment">
					<jstl:out value="${message.moment}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageBody">
					<jstl:out value="${message.body}" />
				</display:column>
				<display:column titleKey="actor.displayData.messagePriority">
					<jstl:out value="${message.priority}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageTags">
					<jstl:forEach var="tag" items="${message.tags}">
						<jstl:out value="${tag}" />
						<br />
					</jstl:forEach>
				</display:column>
			</display:table>
			<br />


		</jstl:when>

		<jstl:when test="${authority == 'ADMINISTRATOR'}">
			<acme:text value="${administrator.name}"
				label="actor.displayData.name" />
			<acme:text value="${administrator.userAccount.username}"
				label="actor.displayData.username" />
			<acme:text value="${administrator.surname}"
				label="actor.displayData.surnames" />
			<acme:text value="${administrator.photoURL}"
				label="actor.displayData.photo" />
			<acme:text value="${administrator.email}"
				label="actor.displayData.email" />
			<acme:text value="${administrator.address}"
				label="actor.displayData.address" />
			<acme:text value="${administrator.phoneNumber}"
				label="actor.displayData.phoneNumber" />




			<!-- Social profiles table -->
			<b><spring:message code="actor.displayData.socialProfile" /></b>
			<display:table name="socialProfiles" id="profile">
				<spring:message code="actor.displayData.socialProfilesName"
					var="name" />
				<display:column title="${name }">
					<jstl:out value="${ profile.nick}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesNetwork"
					var="network" />
				<display:column title="${network }">
					<jstl:out value="${profile.nameSocialNetwork}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesLink"
					var="link" />
				<display:column title="${link }">
					<jstl:out value="${ profile.link}" />
				</display:column>
			</display:table>

			<br>

			<!-- Table with messages -->
			<b><spring:message code="actor.displayData.messages" /></b>
			<display:table pagesize="5" name="messages" id="message"
				requestURI="actor/displayAllData.do">
				<display:column titleKey="actor.displayData.messageSender">
					<jstl:out value="${message.sender.email}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageSubject">
					<jstl:out value="${message.subject}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageMoment">
					<jstl:out value="${message.moment}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageBody">
					<jstl:out value="${message.body}" />
				</display:column>
				<display:column titleKey="actor.displayData.messagePriority">
					<jstl:out value="${message.priority}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageTags">
					<jstl:forEach var="tag" items="${message.tags}">
						<jstl:out value="${tag}" />
						<br />
					</jstl:forEach>
				</display:column>
			</display:table>
			<br />


		</jstl:when>

		<jstl:when test="${authority == 'PUBLISHER'}">
			<acme:text value="${publisher.name}" label="actor.displayData.name" />
			<acme:text value="${publisher.userAccount.username}"
				label="actor.displayData.username" />
			<acme:text value="${publisher.surname}"
				label="actor.displayData.surnames" />
			<acme:text value="${publisher.photoURL}" label="actor.displayData.photo" />
			<acme:text value="${publisher.email}" label="actor.displayData.email" />
			<acme:text value="${publisher.address}"
				label="actor.displayData.address" />
			<acme:text value="${publisher.phoneNumber}"
				label="actor.displayData.phoneNumber" />

			<br />
			<acme:text value="${publisher.creditCard.holder }" label="actor.creditCard.holder" />
			<acme:text value="${publisher.creditCard.make }" label="actor.creditCard.make" />
			<acme:text value="${publisher.creditCard.number }" label="actor.creditCard.number" />
			<acme:text value="${publisher.creditCard.expirationMonth }" label="actor.creditCard.expirationMonth" />
			<acme:text value="${publisher.creditCard.expirationYear }" label="actor.creditCard.expirationYear" />
			<acme:text value="${publisher.creditCard.cvv }" label="actor.creditCard.cvv" />
			<br /> 

			<b><spring:message code="publisher.contests" /></b>
			<display:table name="contests" id="contest">
				<display:column titleKey="contest.title">
					<jstl:out value="${contest.title }" />
				</display:column>
				<display:column titleKey="contest.description">
					<jstl:out value="${contest.description }" />
				</display:column>
				<display:column titleKey="contest.prize">
					<jstl:out value="${contest.prize }" />
				</display:column>
				<display:column titleKey="contest.rules">
					<jstl:forEach items="${contest.rules }" var="rule">
						<jstl:out value="${rule}" />
					</jstl:forEach>
				</display:column>
				<display:column titleKey="contest.deadline">
					<jstl:out value="${contest.deadline}" />
				</display:column>
			</display:table>
			<br />

			<!-- Social profiles table -->
			<b><spring:message code="actor.displayData.socialProfile" /></b>
			<display:table name="socialProfiles" id="profile">
				<spring:message code="actor.displayData.socialProfilesName"
					var="name" />
				<display:column title="${name }">
					<jstl:out value="${ profile.nick}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesNetwork"
					var="network" />
				<display:column title="${network }">
					<jstl:out value="${profile.nameSocialNetwork}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesLink"
					var="link" />
				<display:column title="${link }">
					<jstl:out value="${ profile.link}" />
				</display:column>
			</display:table>

			<br>

			<!-- Table with messages -->
			<b><spring:message code="actor.displayData.messages" /></b>
			<display:table pagesize="5" name="messages" id="message"
				requestURI="actor/displayAllData.do">
				<display:column titleKey="actor.displayData.messageSender">
					<jstl:out value="${message.sender.email}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageSubject">
					<jstl:out value="${message.subject}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageMoment">
					<jstl:out value="${message.moment}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageBody">
					<jstl:out value="${message.body}" />
				</display:column>
				<display:column titleKey="actor.displayData.messagePriority">
					<jstl:out value="${message.priority}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageTags">
					<jstl:forEach var="tag" items="${message.tags}">
						<jstl:out value="${tag}" />
						<br />
					</jstl:forEach>
				</display:column>
			</display:table>
			<br />


		</jstl:when>

		<jstl:when test="${authority == 'SPONSOR'}">
			<acme:text value="${sponsor.name}" label="actor.displayData.name" />
			<acme:text value="${sponsor.userAccount.username}"
				label="actor.displayData.username" />
			<acme:text value="${sponsor.surname}"
				label="actor.displayData.surnames" />
			<acme:text value="${sponsor.photoURL}" label="actor.displayData.photo" />
			<acme:text value="${sponsor.email}" label="actor.displayData.email" />
			<acme:text value="${sponsor.address}"
				label="actor.displayData.address" />
			<acme:text value="${sponsor.phoneNumber}"
				label="actor.displayData.phoneNumber" />
			<acme:text value="${sponsor.companyName}"
				label="actor.displayData.companyName" />
				

			<br />
			<acme:text value="${sponsor.creditCard.holder }" label="actor.creditCard.holder" />
			<acme:text value="${sponsor.creditCard.make }" label="actor.creditCard.make" />
			<acme:text value="${sponsor.creditCard.number }" label="actor.creditCard.number" />
			<acme:text value="${sponsor.creditCard.expirationMonth }" label="actor.creditCard.expirationMonth" />
			<acme:text value="${sponsor.creditCard.expirationYear }" label="actor.creditCard.expirationYear" />
			<acme:text value="${sponsor.creditCard.cvv }" label="actor.creditCard.cvv" />
			<br /> 
			<b><spring:message code="sponsor.displayData.sponsorships" /></b>
			<display:table name="sponsorships" id="sponsorship">

				<display:column titleKey="sponsorship.banner">
					<jstl:out value="${sponsorship.bannerURL}" />
				</display:column>

				<display:column titleKey="sponsorship.target">
					<jstl:out value="${sponsorship.targetPageURL}" />
				</display:column>

				<display:column titleKey="sponsorship.flatRate">
					<jstl:out value="${sponsorship.flatRateApplied}" />
				</display:column>
				
				<display:column titleKey="sponsorship.views">
					<jstl:out value="${sponsorship.views}" />
				</display:column>
			</display:table>
			<br />


			<!-- Social profiles table -->
			<b><spring:message code="actor.displayData.socialProfile" /></b>
			<display:table name="socialProfiles" id="profile">
				<spring:message code="actor.displayData.socialProfilesName"
					var="name" />
				<display:column title="${name }">
					<jstl:out value="${ profile.nick}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesNetwork"
					var="network" />
				<display:column title="${network }">
					<jstl:out value="${profile.nameSocialNetwork}" />
				</display:column>
				<spring:message code="actor.displayData.socialProfilesLink"
					var="link" />
				<display:column title="${link }">
					<jstl:out value="${ profile.link}" />
				</display:column>
			</display:table>

			<br>

			<!-- Table with messages -->
			<b><spring:message code="actor.displayData.messages" /></b>
			<display:table pagesize="5" name="messages" id="message"
				requestURI="actor/displayAllData.do">
				<display:column titleKey="actor.displayData.messageSender">
					<jstl:out value="${message.sender.email}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageSubject">
					<jstl:out value="${message.subject}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageMoment">
					<jstl:out value="${message.moment}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageBody">
					<jstl:out value="${message.body}" />
				</display:column>
				<display:column titleKey="actor.displayData.messagePriority">
					<jstl:out value="${message.priority}" />
				</display:column>
				<display:column titleKey="actor.displayData.messageTags">
					<jstl:forEach var="tag" items="${message.tags}">
						<jstl:out value="${tag}" />
						<br />
					</jstl:forEach>
				</display:column>
			</display:table>
			<br />


		</jstl:when>


	</jstl:choose>
</div>


<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.2/jspdf.min.js"></script>

<script>
	function printHTML() {
		var pdf = new jsPDF('p', 'pt', 'letter');

		source = $('#print')[0];

		specialElementHandlers = {
			// element with id of "bypass" - jQuery style selector
			'#bypassme' : function(element, renderer) {
				// true = "handled elsewhere, bypass text extraction"
				return true
			}
		};
		margins = {
			top : 80,
			bottom : 60,
			left : 40,
			width : 522
		};
		// all coords and widths are in jsPDF instance's declared units
		// 'inches' in this case
		pdf.fromHTML(source, // HTML string or DOM elem ref.
		margins.left, // x coord
		margins.top, { // y coord
			'width' : margins.width, // max width of content on PDF
			'elementHandlers' : specialElementHandlers
		},

		function(dispose) {
			// dispose: object with X, Y of the last line add to the PDF 
			//          this allow the insertion of new lines after html
			pdf.save('Test.pdf');
		}, margins);
	}
</script>





