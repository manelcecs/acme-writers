<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
			

<h3 id="book"><spring:message code="administrator.dashboard.book" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfBooksPerWriter"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfBooksPerWriter'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfBooksPerWriter'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfBooksPerWriter'/> </th>
		<th> <spring:message code='administrator.dashboard.ratioOfBooksWithPublisherVsBooksIndependients'/> </th>
		<th> <spring:message code='administrator.dashboard.ratioOfBooksAcceptedVsBooksRejected'/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfBooksPerWriter}"/>	</td>
		<td> <jstl:out value="${minimumOfBooksPerWriter}"/>	</td>
		<td> <jstl:out value="${maximumOfBooksPerWriter}"/>	</td>
		<td> <jstl:out value="${sDOfBooksPerWriter}"/>	</td>
		<td> <jstl:out value="${ratioOfBooksWithPublisherVsBooksIndependients}"/>	</td>
		<td> <jstl:out value="${ratioOfBooksAcceptedVsBooksRejected}"/>	</td>
	</tr>
</table>

		<canvas id="myChart" width="10" height="2">
		</canvas>
		
		<script>
		    var ctx = document.getElementById('myChart').getContext('2d');
		    var chart = new Chart(ctx, {
		
		    type: 'bar',
		
		    data: {
		    	
		
		        labels: 
		        	<jstl:choose>
						<jstl:when test="${cookie.language.value == 'es'}">
				        	[<jstl:forEach var="histogramValue" items="${histogramData}"><jstl:if test="${histogramData.indexOf(histogramValue) != 0}"><jstl:out value=", "/></jstl:if>"<jstl:out value="${histogramValue[1].nameES}"/>"</jstl:forEach>],
						</jstl:when>
						
						<jstl:otherwise>				        	
							[<jstl:forEach var="histogramValue" items="${histogramData}"><jstl:if test="${histogramData.indexOf(histogramValue) != 0}"><jstl:out value=", "/></jstl:if>"<jstl:out value="${histogramValue[1].nameEN}"/>"</jstl:forEach>],
						</jstl:otherwise>
					</jstl:choose>
		        datasets: [{
		            label: "<spring:message code="administrator.dashboard.histogram" />",
		            backgroundColor: 'rgb(118, 223, 113, 0.5)',
		            data: [<jstl:forEach var="histogramValue" items="${histogramData}"><jstl:if test="${histogramData.indexOf(histogramValue) != 0}"><jstl:out value=", "/></jstl:if>"<jstl:out value="${histogramValue[0]}"/>"</jstl:forEach>],
		            borderColor: 'rgb(100, 189, 96)',
		            borderWidth: 2,}]
		    },
		
		    // Configuration options go here
		    options: {}
		    });
		</script>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfChaptersPerBook"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfChaptersPerBook'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfChaptersPerBook'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfChaptersPerBook'/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfChaptersPerBook}"/>	</td>
		<td> <jstl:out value="${minimumOfChaptersPerBook}"/>	</td>
		<td> <jstl:out value="${maximumOfChaptersPerBook}"/>	</td>
		<td> <jstl:out value="${sDOfChaptersPerBook}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="writersWithMoreBooks" id="writerWithMoreBooks" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.writersWithMoreBooks">- <jstl:out value="${writerWithMoreBooks.name} ${writerWithMoreBooks.surname} : ${maximumOfBooksPerWriter}"/></display:column>
</display:table> 

<display:table pagesize="5" name="writersWithLessBooks" id="writerWithLessBooks" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.writersWithLessBooks">- <jstl:out value="${writerWithLessBooks.name} ${writerWithLessBooks.surname} : ${minimumOfBooksPerWriter}"/></display:column>
</display:table> 

<hr/>

<h3 id="contest"><spring:message code="administrator.dashboard.contest" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfContestPerPublisher"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfContestPerPublisher'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfContestPerPublisher'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfContestPerPublisher'/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfContestPerPublisher}"/>	</td>
		<td> <jstl:out value="${minimumOfContestPerPublisher}"/>	</td>
		<td> <jstl:out value="${maximumOfContestPerPublisher}"/>	</td>
		<td> <jstl:out value="${sDOfContestPerPublisher}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="contestsWithMoreParticipations" id="contestWithMoreParticipations" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.contestsWithMoreParticipations">- <jstl:out value="${contestWithMoreParticipations.description} : ${maximumOfParticipationsContest}"/></display:column>
</display:table> 

<hr/>

<h3 id="sponsorship"><spring:message code="administrator.dashboard.sponsorship" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfSponsorshipsPerSponsor"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfSponsorshipsPerSponsor'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfSponsorshipsPerSponsor'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfSponsorshipsPerSponsor'/> </th>
		<th> <spring:message code='administrator.dashboard.ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled'/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfSponsorshipsPerSponsor}"/>	</td>
		<td> <jstl:out value="${minimumOfSponsorshipsPerSponsor}"/>	</td>
		<td> <jstl:out value="${maximumOfSponsorshipsPerSponsor}"/>	</td>
		<td> <jstl:out value="${sDOfSponsorshipsPerSponsor}"/>	</td>
		<td> <jstl:out value="${ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled}"/>	</td>
	</tr>
</table>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfViewsPerSponsorship"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfViewsPerSponsorship'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfViewsPerSponsorship'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfViewsPerSponsorship'/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfViewsPerSponsorship}"/>	</td>
		<td> <jstl:out value="${minimumOfViewsPerSponsorship}"/>	</td>
		<td> <jstl:out value="${maximumOfViewsPerSponsorship}"/>	</td>
		<td> <jstl:out value="${sDOfViewsPerSponsorship}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="contestsWithMoreSponsorships" id="contestWithMoreSponsorships" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.contestsWithMoreSponsorships">- <jstl:out value="${contestWithMoreSponsorships.description} : ${maximumOfSponsorshipsContest}"/></display:column>
</display:table> 

<display:table pagesize="5" name="sponsorsWithMoreSponsorships" id="sponsorWithMoreSponsorships" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.sponsorsWithMoreSponsorships">- <jstl:out value="${sponsorWithMoreSponsorships.name} ${sponsorWithMoreSponsorships.surname} : ${maximumOfSponsorshipsPerSponsor}"/></display:column>
</display:table> 







