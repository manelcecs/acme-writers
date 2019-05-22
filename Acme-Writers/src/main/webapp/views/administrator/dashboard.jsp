<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<h3 id="position"><spring:message code="administrator.dashboard.position" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfPositionsPerCompany"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfPositionsPerCompany'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfPositionsPerCompany'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfPositionsPerCompany'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfPositionsPerCompany}"/>	</td>
		<td> <jstl:out value="${minimumOfPositionsPerCompany}"/>	</td>
		<td> <jstl:out value="${maximumOfPositionsPerCompany}"/>	</td>
		<td> <jstl:out value="${sDOfPositionsPerCompany}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="companiesWithMoreOffersOfPositions" id="companyWithMoreOffersOfPositions" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.companiesWithMoreOffersOfPositions">- <jstl:out value="${companyWithMoreOffersOfPositions.companyName}"/>
		<jstl:out value="(${companyWithMoreOffersOfPositions.id}) : ${maximumOfPositionsPerCompany}"/></display:column>
</display:table>

<hr>

<h3 id="application"><spring:message code="administrator.dashboard.application" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfApplicationsPerRookie"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfApplicationsPerRookie'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfApplicationsPerRookie'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfApplicationsPerRookie'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfApplicationsPerRookie}"/>	</td>
		<td> <jstl:out value="${minimumOfApplicationsPerRookie}"/>	</td>
		<td> <jstl:out value="${maximumOfApplicationsPerRookie}"/>	</td>
		<td> <jstl:out value="${sDOfApplicationsPerRookie}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="rookiesWithMoreApplications" id="rookieWithMoreApplications" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.rookiesWithMoreApplications">- <jstl:out value="${rookieWithMoreApplications.name}"/>
				<jstl:forEach var="surname" items="${rookieWithMoreApplications.surnames}" >
					<jstl:out value=" ${surname}"/>
				</jstl:forEach>
				<jstl:out value="(${rookieWithMoreApplications.id}): ${maximumOfApplicationsPerRookie}"/></display:column>
</display:table>	
	
<hr>

<h3 id="salary"><spring:message code="administrator.dashboard.salary" /> </h3>
<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfSalariesOffered"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfSalariesOffered'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfSalariesOffered'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfSalariesOffered'/> 	  </th>
		<th> <spring:message code="administrator.dashboard.avgOfSalaryOfPositionWithTheHighestAvgOfAuditScore"/> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfSalariesOffered}"/>	</td>
		<td> <jstl:out value="${minimumOfSalariesOffered}"/>	</td>
		<td> <jstl:out value="${maximumOfSalariesOffered}"/>	</td>
		<td> <jstl:out value="${sDOfSalariesOffered}"/>	</td>
		<td> <jstl:out value="${avgOfSalaryOfPositionWithTheHighestAvgOfAuditScore}"/>	</td>
		
	</tr>
</table>

<display:table pagesize="5" name="positionsWithTheBestSalary" id="positionWithTheBestSalary" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.positionsWithTheBestSalary">
		- <jstl:out value="${positionWithTheBestSalary.title} (${positionWithTheBestSalary.ticker.identifier}): ${positionWithTheBestSalary.salaryOffered}"/>
	</display:column>
</display:table>	

<display:table pagesize="5" name="positionsWithTheWorstSalary" id="positionWithTheWorstSalary" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.positionsWithTheWorstSalary">
		- <jstl:out value="${positionWithTheWorstSalary.title} (${positionWithTheWorstSalary.ticker.identifier}): ${positionWithTheWorstSalary.salaryOffered}"/>
	</display:column>
</display:table>

<hr>

<h3 id="curricula"><spring:message code="administrator.dashboard.curricula" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfCurriculaPerRookie"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfCurriculaPerRookie'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfCurriculaPerRookie'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfCurriculaPerRookie'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfCurriculaPerRookie}"/>	</td>
		<td> <jstl:out value="${minimumOfCurriculaPerRookie}"/>	</td>
		<td> <jstl:out value="${maximumOfCurriculaPerRookie}"/>	</td>
		<td> <jstl:out value="${sDOfCurriculaPerRookie}"/>	</td>
	</tr>
</table>

<hr>

<h3 id="finder"><spring:message code="administrator.dashboard.finder" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfResultsInFinders"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfResultsInFinders'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfResultsInFinders'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfResultsInFinders'/> 	  </th>
		<th> <spring:message code='administrator.dashboard.ratioOfEmptyVsNotEmptyFinders'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfResultsInFinders}"/>	</td>
		<td> <jstl:out value="${minimumOfResultsInFinders}"/>	</td>
		<td> <jstl:out value="${maximumOfResultsInFinders}"/>	</td>
		<td> <jstl:out value="${sDOfResultsInFinders}"/>	</td>
		<td> <jstl:out value="${ratioOfEmptyVsNotEmptyFinders}"/>	</td>
	</tr>
</table>

<hr>

<h3 id="auditScore"><spring:message code="administrator.dashboard.audit.score" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfAuditScoreOfPosition"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfAuditScoreOfPosition'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfAuditScoreOfPosition'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfAuditScoreOfPosition'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfAuditScoreOfPosition}"/>	</td>
		<td> <jstl:out value="${minimumOfAuditScoreOfPosition}"/>	</td>
		<td> <jstl:out value="${maximumOfAuditScoreOfPosition}"/>	</td>
		<td> <jstl:out value="${sDOfAuditScoreOfPosition}"/>	</td>
	</tr>
</table>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfAuditScoreOfCompany"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfAuditScoreOfCompany'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfAuditScoreOfCompany'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfAuditScoreOfCompany'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfAuditScoreOfCompany}"/>	</td>
		<td> <jstl:out value="${minimumOfAuditScoreOfCompany}"/>	</td>
		<td> <jstl:out value="${maximumOfAuditScoreOfCompany}"/>	</td>
		<td> <jstl:out value="${sDOfAuditScoreOfCompany}"/>	</td>

	</tr>
</table>

<display:table pagesize="5" name="companiesWithTheHighestAuditScore" id="companyWithTheHighestAuditScore" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.companiesWithTheHighestAuditScore">- <jstl:out value="${companyWithTheHighestAuditScore.companyName}"/>
		<jstl:out value="(${companyWithTheHighestAuditScore.id}) : ${maximumOfAuditScoreOfCompany}"/></display:column>
</display:table> 
<hr>

<h3 id="sponsorship"><spring:message code="administrator.dashboard.sponsorship" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfSponsorshipsPerProvider"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfSponsorshipsPerProvider'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfSponsorshipsPerProvider'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfSponsorshipsPerProvider'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfSponsorshipsPerProvider}"/>	</td>
		<td> <jstl:out value="${minimumOfSponsorshipsPerProvider}"/>	</td>
		<td> <jstl:out value="${maximumOfSponsorshipsPerProvider}"/>	</td>
		<td> <jstl:out value="${sDOfSponsorshipsPerProvider}"/>	</td>
	</tr>
</table>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfSponsorshipsPerPosition"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfSponsorshipsPerPosition'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfSponsorshipsPerPosition'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfSponsorshipsPerPosition'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfSponsorshipsPerPosition}"/>	</td>
		<td> <jstl:out value="${minimumOfSponsorshipsPerPosition}"/>	</td>
		<td> <jstl:out value="${maximumOfSponsorshipsPerPosition}"/>	</td>
		<td> <jstl:out value="${sDOfSponsorshipsPerPosition}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="providers10RateOfAvgOfSponsorships" id="provider10RateOfAvgOfSponsorships" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.providers10RateOfAvgOfSponsorships">- <jstl:out value="${provider10RateOfAvgOfSponsorships.name}"/>
				<jstl:forEach var="surname" items="${provider10RateOfAvgOfSponsorships.surnames}" >
					<jstl:out value=" ${surname}"/>
				</jstl:forEach>
				<jstl:out value="(${provider10RateOfAvgOfSponsorships.id}): ${maximumOfSponsorshipsPerPosition}"/></display:column>
</display:table>

<hr>

<h3 id="item"><spring:message code="administrator.dashboard.item" /> </h3>

<table>
	<tr>
		<th> <spring:message code="administrator.dashboard.avgOfItemsPerProvider"/> </th>
		<th> <spring:message code='administrator.dashboard.minimumOfItemsPerProvider'/> </th>
		<th> <spring:message code='administrator.dashboard.maximumOfItemsPerProvider'/> </th>
		<th> <spring:message code='administrator.dashboard.sDOfItemsPerProvider'/> 	  </th>
	</tr>
	<tr>
		<td> <jstl:out value="${avgOfItemsPerProvider}"/>	</td>
		<td> <jstl:out value="${minimumOfItemsPerProvider}"/>	</td>
		<td> <jstl:out value="${maximumOfItemsPerProvider}"/>	</td>
		<td> <jstl:out value="${sDOfItemsPerProvider}"/>	</td>
	</tr>
</table>

<display:table pagesize="5" name="top5OfProvidersByItems" id="top5OfProviderByItems" requestURI="${requestURI}">
	<display:column titleKey="administrator.dashboard.top5OfProvidersByItems">- <jstl:out value="${top5OfProviderByItems[0].name}"/>
				<jstl:forEach var="surname" items="${top5OfProviderByItems[0].surnames}" >
					<jstl:out value=" ${surname}"/>
				</jstl:forEach>
				<jstl:out value="(${top5OfProviderByItems[0].id}): ${top5OfProviderByItems[1]}"/></display:column>
</display:table>


